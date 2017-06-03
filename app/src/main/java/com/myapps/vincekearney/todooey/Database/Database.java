package com.myapps.vincekearney.todooey.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class Database extends SQLiteOpenHelper {
    /* ---- TAG and Helper strings ---- */
    private static final String TAG = "Database";
    private static final String TYPE_TEXT = "TEXT";
    private static final String TYPE_INTEGER = "INTEGER";
    private static final String formatTextType = String.format(" %s, ", TYPE_TEXT);
    private static final String formatTextTypeEnd = String.format(" %s", TYPE_TEXT);
    private static final String formatIntegerType = String.format(" %s, ", TYPE_INTEGER);
    /* ---- Database ---- */
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "ToDooey.db";
    /* ---- Table and Columns (in order) ---- */
    public final String TO_DO_ITEMS_TABlE = "ToDoItems";
    public final String COLUMN_NAME_TODO_ID = "todoid";
    public final String COLUMN_NAME_TODO_TEXT = "todotext";
    public final String COLUMN_NAME_COMPLETED = "completed";
    public final String COLUMN_NAME_DATE = "date";
    /* ---- Create table SQL string ---- */
    private final String CREATE_DATABASE =
            "CREATE TABLE IF NOT EXISTS " + TO_DO_ITEMS_TABlE + "("
                    + COLUMN_NAME_TODO_ID + formatTextType
                    + COLUMN_NAME_TODO_TEXT + formatTextType
                    + COLUMN_NAME_COMPLETED + formatIntegerType
                    + COLUMN_NAME_DATE + formatTextTypeEnd + ")";

    /**
     * Above ^^^
     * When creating the SQL database table we need to form a string that matches an exact pattern.
     * I found this just a slightly easier way to read it when doing it in ToDooey :D.
     * Essentially it just makes sure that the table is not already present in the DB (not to override it)
     * then gives the table a name, assigns the columns that we want to use with their respective data type.
     */
    public Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.i(TAG, "Setting up Database.");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(TAG, "Creating the database: " + CREATE_DATABASE);
        db.execSQL(CREATE_DATABASE);
    }

    /**
     * For onUpgrade()
     * Note: No point in using for Dev - We are better using 'Clear Data' in android settings.
     * Then just running it so the version number does not need to increase.
     * For release we need to migrate everything.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // If we ever upgrade, which we won't for now, we do the shiz here.
        Log.w(Database.class.getName(), "Upgrading DB from " + oldVersion + " to " + newVersion);
        db.execSQL("ALTER TABLE " + TO_DO_ITEMS_TABlE);
        // Then anything else like ADD/DELETE/MODIFY columns in table
    }
    /* ===============END OF CLASS=============== */
}
