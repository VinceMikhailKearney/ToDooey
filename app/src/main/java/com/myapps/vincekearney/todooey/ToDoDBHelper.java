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
    private SQLiteDatabase db;
    private final String[] allColumns =
    {
        ToDoDBHelper.COLUMN_NAME_TODO_ID,
        ToDoDBHelper.COLUMN_NAME_TODO_TEXT,
        ToDoDBHelper.COLUMN_NAME_COMPLETED
    };

    public ToDoDBHelper(Context context)
    {
        super(context);
    }

    // Adding a To-Do item
    public void addToDo(String id, String text, Boolean completed)
    {
        Log.i(TAG, "Adding a ToDo item.");

        ContentValues toDoValues = new ContentValues();
        toDoValues.put(ToDoDBHelper.COLUMN_NAME_TODO_ID, id);
        toDoValues.put(ToDoDBHelper.COLUMN_NAME_TODO_TEXT, text);
        toDoValues.put(ToDoDBHelper.COLUMN_NAME_COMPLETED, completed);

        Log.i(TAG, "Values = " + toDoValues);

        db.insert(ToDoDBHelper.TO_DO_ITEMS_TABlE, null, toDoValues);
    }

    // Retrieving the list of To-Do items
    public List<ToDoItem>  getAllToDos()
    {
        Log.i(TAG, "Asking for all ToDo items.");

        List<ToDoItem> todos = new ArrayList<>();
        // Query sets to select ALL from the To-Do table.
        String query = "SELECT * FROM " + TO_DO_ITEMS_TABlE;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        // Starting at the first row, continue to move until past the last row.
        cursor.moveToFirst();
        while(!cursor.isAfterLast())
        {
            ToDoItem todo = cursorToIntallToDo(cursor);
            todos.add(todo);
            cursor.moveToNext();
        }
        cursor.close();

        return todos;
    }

    public ToDoItem cursorToIntallToDo(Cursor cursor)
    {
        ToDoItem todo = new ToDoItem();
        todo.setId(cursor.getString(0));
        todo.setTodotext(cursor.getString(1));
        todo.setDate(null);
        todo.setCompleted(false);
        return todo;
    }

    // Convenience methods
    public void open()
    {
        db = super.getWritableDatabase();
    }

    public void close()
    {
        super.close();
    }



    //==============================END OF CLASS==============================
}
