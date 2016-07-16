package com.myapps.vincekearney.todooey;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class ToDoDBHelper extends DBManager
{
    public enum getOrDelete
    {
        FETCH_TODO,DELETE_TODO
    }
    private static final String TAG = "ToDoDataBaseHelper";

    public ToDoDBHelper(Context context)
    {
        super(context);
    }

    // Adding a To-Do item
    public ToDoItem addToDo(String text)
    {
        Log.i(TAG, "Adding a ToDo item.");

        String toDoID = UUID.randomUUID().toString();
        ContentValues toDoValues = new ContentValues();
        toDoValues.put(ToDoDBHelper.COLUMN_NAME_TODO_ID, toDoID);
        toDoValues.put(ToDoDBHelper.COLUMN_NAME_TODO_TEXT, text);
        toDoValues.put(ToDoDBHelper.COLUMN_NAME_COMPLETED, 0);
        toDoValues.put(ToDoDBHelper.COLUMN_NAME_DATE, Long.toString(new Date().getTime()));

        thisDataBase().insert(ToDoDBHelper.TO_DO_ITEMS_TABlE, null, toDoValues);
        closeDBManger();

        return toDo(toDoID, getOrDelete.FETCH_TODO);
    }

    public void updateCompleted(String id, Boolean completed)
    {
        Log.i(TAG, "ToDo ID: " + id +"\nCompleted: " + completed);
        ContentValues newValues = new ContentValues();
        newValues.put(ToDoDBHelper.COLUMN_NAME_COMPLETED, completed);

        String searchString = String.format("%s = %s%s%s",ToDoDBHelper.COLUMN_NAME_TODO_ID,"'",id,"'");
        thisDataBase().update(TO_DO_ITEMS_TABlE, newValues, searchString, null);
    }

    public ToDoItem toDo(String id, getOrDelete state)
    {
        ToDoItem item = null;
        Cursor cursor = fetchSingleToDo(id);
        if (cursor.moveToFirst() && cursor.getCount() == 1)
        {
            String toDoId = cursor.getString(0);
            if(state == getOrDelete.DELETE_TODO)
            {
                Log.i(TAG, "Deleting to do with ID: " + id);
                thisDataBase().delete(TO_DO_ITEMS_TABlE, COLUMN_NAME_TODO_ID +" = \"" + toDoId +"\"", null);
            }
            else
            {
                item = createToDoFrom(cursor);
                Log.i(TAG, "Got To-Do with ID: " + toDoId);
            }
        }
        else // We didn't fetch just ONE item.
        {
            throw new IllegalStateException("Only supposed to fetch one. Count was -> " + cursor.getCount());
        }

        cursor.close();
        closeDBManger();
        return item;
    }

    // Retrieving the list of To-Do items
    public List<ToDoItem>  getAllToDos()
    {
        Log.i(TAG, "Asking for all ToDo items.");

        List<ToDoItem> todos = new ArrayList<>();
        // Query sets to select ALL from the To-Do table.
        String query = "SELECT * FROM " + TO_DO_ITEMS_TABlE;
        Cursor cursor = thisDataBase().rawQuery(query, null);
        // Starting at the first row, continue to move until past the last row.
        cursor.moveToFirst();
        while(!cursor.isAfterLast())
        {
            ToDoItem todo = createToDoFrom(cursor);
            todos.add(todo);
            cursor.moveToNext();
        }

        cursor.close();
        closeDBManger();
        return todos;
    }

    // Retrieving the list of To-Do items
    public void deleteAllToDos()
    {
        // Query sets to select ALL from the To-Do table.
        String query = "SELECT * FROM " + TO_DO_ITEMS_TABlE;
        Cursor cursor = thisDataBase().rawQuery(query, null);
        // Starting at the first row, continue to move until past the last row.
        cursor.moveToFirst();
        while(!cursor.isAfterLast())
        {
            String toDoId = cursor.getString(0);
            Log.i(TAG, "Deleting all to do's. This ID - " + toDoId);
            thisDataBase().delete(TO_DO_ITEMS_TABlE, COLUMN_NAME_TODO_ID +" = \"" + toDoId +"\"", null);
            cursor.moveToNext();
        }

        cursor.close();
        closeDBManger();
    }

    public ToDoItem createToDoFrom(Cursor cursor)
    {
        ToDoItem todo = new ToDoItem();
        todo.setId(cursor.getString(0));
        todo.setTodotext(cursor.getString(1));
        todo.setCompleted(cursor.getInt(2) == 1);
        todo.setDate(new Date(Long.parseLong(cursor.getString(3))));
        return todo;
    }

    // Convenience methods
    public SQLiteDatabase thisDataBase()
    {
        return super.getWritableDatabase();
    }

    public void closeDBManger()
    {
        // Need to close down the DBManager (SQLiteOpenHelper).
        super.close();
    }

    public Cursor fetchSingleToDo(String id)
    {
        // Query sets to select ALL from the To-Do table.
        String searchString = String.format("%s%s%s","'",id,"'");
        String query = "SELECT * FROM " + TO_DO_ITEMS_TABlE + " WHERE " + COLUMN_NAME_TODO_ID + " = " + searchString;
        return thisDataBase().rawQuery(query, null);
    }

    //==============================END OF CLASS==============================
}
