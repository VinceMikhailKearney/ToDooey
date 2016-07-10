package com.myapps.vincekearney.todooey;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBManager extends SQLiteOpenHelper
{

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "ToDooey.db";
    public static final String TO_DO_ITEMS_TABlE = "ToDoItems";
    public static final String COLUMN_NAME_TODO_ID = "todoid";
    public static final String COLUMN_NAME_TODO_TEXT = "todotext";
    public static final String COLUMN_NAME_COMPLETED = "completed";

    private static final String CREATE_DATABASE =
                    "CREATE TABLE IF NOT EXISTS " +TO_DO_ITEMS_TABlE +"("
                    + COLUMN_NAME_TODO_ID + " INTEGER PRIMARY KEY, "
                    + COLUMN_NAME_TODO_TEXT + " TEXT, "
                    + COLUMN_NAME_COMPLETED + " INTEGER, " + ")";

    public DBManager(Context context)
    {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(CREATE_DATABASE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        // If we ever upgrade, which we won't for now, we do the shiz here.
    }
}
