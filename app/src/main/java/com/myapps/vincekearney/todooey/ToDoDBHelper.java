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

    public ToDoDBHelper(Context context)
    {
        super(context);
    }

    // Retrieving the list of To-Do items
//    public List<>

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
