package com.myapps.vincekearney.todooey;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBManager extends SQLiteOpenHelper
{
    /* ---- TAG and Helper strings ---- */
    private static final String TAG = "DBManager";
    private static final String TYPE_TEXT = "TEXT";
    private static final String TYPE_INTEGER = "INTEGER";
    private static final String formatTextType = String.format(" %s, ",TYPE_TEXT);
    private static final String formatTextTypeEnd = String.format(" %s",TYPE_TEXT);
    private static final String formatIntegerType = String.format(" %s, ",TYPE_INTEGER);
    /* ---- Database ---- */
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "ToDooey.db";
    /* ---- Table and Columns (in order) ---- */
    public static final String TO_DO_ITEMS_TABlE = "ToDoItems";
    public static final String COLUMN_NAME_TODO_ID = "todoid";
    public static final String COLUMN_NAME_TODO_TEXT = "todotext";
    public static final String COLUMN_NAME_COMPLETED = "completed";
    public static final String COLUMN_NAME_DATE = "date";
    /* ---- Create table SQL string ---- */
    private static final String CREATE_DATABASE =
                    "CREATE TABLE IF NOT EXISTS " + TO_DO_ITEMS_TABlE +"("
                    + COLUMN_NAME_TODO_ID + formatTextType
                    + COLUMN_NAME_TODO_TEXT + formatTextType
                    + COLUMN_NAME_COMPLETED + formatIntegerType
                    + COLUMN_NAME_DATE + formatTextTypeEnd + ")";

    public DBManager(Context context)
    {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
        Log.i(TAG, "Setting up DBManager.");
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        Log.i(TAG, "Creating the database: " + CREATE_DATABASE);
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
    /* ===============END OF CLASS=============== */
}
