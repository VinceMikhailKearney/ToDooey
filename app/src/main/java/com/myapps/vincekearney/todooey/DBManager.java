package com.myapps.vincekearney.todooey;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBManager extends SQLiteOpenHelper
{
    private static final String TAG = "DBManager";
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "ToDooey.db";
    public static final String TO_DO_ITEMS_TABlE = "ToDoItems";
    public static final String COLUMN_NAME_TODO_ID = "todoid";
    public static final String COLUMN_NAME_TODO_TEXT = "todotext";
    public static final String COLUMN_NAME_COMPLETED = "completed";
    public static final String COLUMN_NAME_DATE = "date";

    private static final String CREATE_DATABASE =
                    "CREATE TABLE IF NOT EXISTS " + TO_DO_ITEMS_TABlE +"("
                    + COLUMN_NAME_TODO_ID + " TEXT, "
                    + COLUMN_NAME_TODO_TEXT + " TEXT, "
                    + COLUMN_NAME_COMPLETED + " INTEGER, "
                    + COLUMN_NAME_DATE + " INTEGER" + ")";

    public DBManager(Context context)
    {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
        Log.i(TAG, "Just instantiating the DBManager.");
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        Log.i(TAG, "Creating the database - " + CREATE_DATABASE);
        db.execSQL(CREATE_DATABASE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        // If we ever upgrade, which we won't for now, we do the shiz here.
        Log.w(DBManager.class.getName(), "Upgrading DB from " + oldVersion + " to " + newVersion);
        db.execSQL("DROP TABLE IF EXISTS " + TO_DO_ITEMS_TABlE);
        onCreate(db);
    }
    //==============================END OF CLASS==============================
}
