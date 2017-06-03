package com.myapps.vincekearney.todooey.Database;

import android.content.ContentValues;
import android.database.Cursor;
import android.text.format.DateUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class ToDoItemHelper extends DatabaseManager {

    private static final String TAG = "ToDoDataBaseHelper";

    public ToDoItemHelper() {
        /** Please note that we do not set the 'searchingFor' string as we do that at time of search **/
        setLocalTableName(getLocalDatabase().TO_DO_ITEMS_TABlE);
    }

    /* ---- Adding methods ---- */
    public ToDoItem addToDo(String text) {
        Log.i(TAG, "Adding a ToDo item.");

        String toDoID = UUID.randomUUID().toString();
        ContentValues toDoValues = new ContentValues();
        toDoValues.put(getLocalDatabase().TODO_ID, toDoID);
        toDoValues.put(getLocalDatabase().TODO_TEXT, text);
        toDoValues.put(getLocalDatabase().COMPLETED, 0);
        toDoValues.put(getLocalDatabase().DATE, Long.toString(new Date().getTime()));

        add(toDoValues);
        return fetchToDo(toDoID);
    }

    /* ---- Update methods ---- */
    public void updateCompleted(String id, Boolean completed) {
        Log.i(TAG, "ToDo ID: " + id + "\nCompleted: " + completed);
        String searchString = String.format("%s = %s%s%s", getLocalDatabase().TODO_ID, "'", id, "'");
        update(newValues(getLocalDatabase().COMPLETED, completed), searchString);
    }

    /* ---- Fetch methods ---- */
    public ToDoItem fetchToDo(String id) {
        setSearchingForString(getLocalDatabase().TODO_ID);
        return (ToDoItem) fetchSingleItem(id);
    }

    public List<ToDoItem> getAllToDos() {
        Log.i(TAG, "Asking for all ToDo items.");
        return (List<ToDoItem>)(List<?>) getObjectsWithQuery(null);
    }

    public List<ToDoItem> getToDosCompleted(Boolean completed) {
        Log.i(TAG, "Asking for todos completed: " + completed);
        setSearchingForString(getLocalDatabase().COMPLETED);
        return (List<ToDoItem>)(List<?>) getObjectsWithQuery(completed ? "1" : "0");
    }

    public List<ToDoItem> getToDosFromToday() {
        Log.i(TAG, "Asking for all ToDo items that were made today.");
        List<ToDoItem> toDosToday = new ArrayList<>();
        for (ToDoItem item : (List<ToDoItem>)(List<?>) getObjectsWithQuery(null)) {
            if (DateUtils.isToday(item.getDate().getTime()))
                toDosToday.add(item);
        }
        return toDosToday;
    }

    /* ---- Delete methods ---- */
    public void deleteToDo(String id) {
        setSearchingForString(getLocalDatabase().TODO_ID);
        delete(id);
    }

    /* ---- Overridden methods ---- */
    @Override
    public List<Object> createObjectAndAddToList(List<Object> list, Cursor cursor) {
        list.add(createObjectFrom(cursor));
        return list;
    }

    @Override
    public ToDoItem createObjectFrom(Cursor cursor) {
        ToDoItem todo = new ToDoItem();
        todo.setId(cursor.getString(0));
        todo.setTodotext(cursor.getString(1));
        todo.setCompleted(cursor.getInt(2) == 1);
        todo.setDate(new Date(Long.parseLong(cursor.getString(3))));
        return todo;
    }

    /* ===============END OF CLASS=============== */
}
