package com.myapps.vincekearney.todooey;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.myapps.vincekearney.todooey.ToDoItem;

import java.util.ArrayList;
import java.util.List;

public class ToDoDBHelper extends DBManager
{
    private static final String TAG = "ToDoDataBaseHelper";
    private SQLiteDatabase db;
    private final String[] allColumns = {
            ToDoDBHelper.COLUMN_NAME_TODO_TEXT,
            ToDoDBHelper.COLUMN_NAME_COMPLETED,
            ToDoDBHelper.COLUMN_NAME_TODO_ID};

    public ToDoDBHelper(Context context)
    {
        super(context);
    }

    // Adding a To-Do item
    public void addToDo(String id, String text, Boolean completed)
    {
        ContentValues toDoValues = new ContentValues();
        toDoValues.put(ToDoDBHelper.COLUMN_NAME_TODO_ID, id);
        toDoValues.put(ToDoDBHelper.COLUMN_NAME_TODO_TEXT, text);
        toDoValues.put(ToDoDBHelper.COLUMN_NAME_COMPLETED, completed);

        db.insert(ToDoDBHelper.TO_DO_ITEMS_TABlE, null, toDoValues);
    }

    // Retrieving the list of To-Do items
    public List<ToDoItem>  getAllToDos()
    {
        List<ToDoItem> todos = new ArrayList<>();
        Cursor cursor = db.query(ToDoDBHelper.TO_DO_ITEMS_TABlE,
                allColumns,null, null, null, null, null );

        cursor.moveToFirst();
        while(!cursor.isAfterLast())
        {
            ToDoItem todo = cursorToIntallPackageInfo(cursor);
            todos.add(todo);
            cursor.moveToNext();
        }
        cursor.close();
        return todos;
    }

    public ToDoItem cursorToIntallPackageInfo(Cursor cursor)
    {
        ToDoItem todo = new ToDoItem();
        todo.setId(cursor.getString(0));
        todo.setTodotext(cursor.getString(1));
        todo.setDate(null);
        todo.setCompleted(cursor.getInt(3) == 1);
        return todo;
    }

    // Convenience methods
//    public void open()
//    {
//        db = dataManager.getWritableDatabase();
//    }
//
//    public void close()
//    {
//        dataManager.close();
//    }



    //==============================END OF CLASS==============================
}
