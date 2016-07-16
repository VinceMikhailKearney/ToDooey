package com.myapps.vincekearney.todooey;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class ToDoDBHelper extends DBManager
{
    private static final String TAG = "ToDoDataBaseHelper";

    public ToDoDBHelper(Context context)
    {
        super(context);
    }

    // Adding a To-Do item
    public ToDoItem addToDo(String id, String text, Boolean completed)
    {
        Log.i(TAG, "Adding a ToDo item.");

        ContentValues toDoValues = new ContentValues();
        toDoValues.put(ToDoDBHelper.COLUMN_NAME_TODO_ID, id); // Do I need? I can delete based upon text.
        toDoValues.put(ToDoDBHelper.COLUMN_NAME_TODO_TEXT, text);
        toDoValues.put(ToDoDBHelper.COLUMN_NAME_COMPLETED, completed ? 1 : 0);

        thisDataBase().insert(ToDoDBHelper.TO_DO_ITEMS_TABlE, null, toDoValues);
        closeDBManger();

        return getToDo(text);
    }

    public void updateCompleted(String text, Boolean completed)
    {
        Log.i(TAG, "ToDoText = " + text +"\nCompleted = " + completed);
        ContentValues newValues = new ContentValues();
        newValues.put(ToDoDBHelper.COLUMN_NAME_COMPLETED, completed);

        String searchString = String.format("%s = %s%s%s",ToDoDBHelper.COLUMN_NAME_TODO_TEXT,"'",text,"'");
        Log.i(TAG, "Search string = " + searchString);
        thisDataBase().update(TO_DO_ITEMS_TABlE, newValues, searchString, null);
    }

    public ToDoItem getToDo(String text)
    {
        Log.i(TAG, "Asking for To-Do with text: " + text);
        // Query sets to select ALL from the To-Do table.
        String searchString = String.format("%s%s%s","'",text,"'");
        String query = "SELECT * FROM " + TO_DO_ITEMS_TABlE + " WHERE " + COLUMN_NAME_TODO_TEXT + " = " + searchString;
        Log.i(TAG, "The query for getting a to do: " + query);
        Cursor cursor = thisDataBase().rawQuery(query, null);

        if (cursor.moveToFirst() && cursor.getCount() == 1)
        {
            ToDoItem item = createToDoFrom(cursor);
            cursor.close();
            closeDBManger();
            return item;
        }
        else // The count was greater than 1
        {
            throw new IllegalStateException("Only supposed to fetch one. Count was -> " + cursor.getCount());
        }
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
        Log.i(TAG, "Deleting all to do items.");

        // Query sets to select ALL from the To-Do table.
        String query = "SELECT * FROM " + TO_DO_ITEMS_TABlE;
        Cursor cursor = thisDataBase().rawQuery(query, null);
        // Starting at the first row, continue to move until past the last row.
        cursor.moveToFirst();
        while(!cursor.isAfterLast())
        {
            String text = cursor.getString(1);
            Log.i(TAG, "String - " + text);
            thisDataBase().delete(TO_DO_ITEMS_TABlE, COLUMN_NAME_TODO_TEXT +" = \"" + text +"\"", null);
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
        todo.setDate(null);
        todo.setCompleted(cursor.getInt(2) == 1);
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

    //==============================END OF CLASS==============================
}
