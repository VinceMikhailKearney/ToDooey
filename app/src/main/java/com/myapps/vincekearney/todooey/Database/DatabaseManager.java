package com.myapps.vincekearney.todooey.Database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.myapps.vincekearney.todooey.ToDoApplication;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vincekearney on 03/06/2017.
 */

public class DatabaseManager {

    private static final String TAG = "DatabaseManager";
    private String tableName;
    private String searchingForString;

    /**
     * Retrieve the localDB through the getter method.
     * Done this way so that we can also set a Database when running tests
     */
    private static Database localDB;
    private static ToDoItemHelper itemHelper;

    // This can be used for testing - Won't be for now.
    public static void setLocalDB(Database manager) {
        localDB = manager;
        itemHelper = new ToDoItemHelper();
    }

    public void setLocalTableName(String table) {
        this.tableName = table;
    }

    public void setSearchingForString(String string) {
        this.searchingForString = string;
    }

    public static ToDoItemHelper toDoItemHelper() {
        if(itemHelper == null)
            return ToDoApplication.getLocalItemHelper();

        return itemHelper;
    }

    public int size() {
        return this.getObjectsWithQuery(null).size();
    }

    public void add(ContentValues values) {
        // Open the db - I.e. return a writeable instance of the database so that we can save to it
        writeableDatabase().insert(this.tableName, null, values);
    }

    public void update(ContentValues values, String search) {
        if(values == null) {
            Log.w(TAG, "update: Seems that we have null values");
            return;
        }
        writeableDatabase().update(this.tableName, values, search, null);
    }

    public ContentValues newValues(String columnName, Object data) {
        ContentValues values = new ContentValues();
        if (data instanceof String)
            values.put(columnName, (String) data);
        else if (data instanceof Boolean)
            values.put(columnName, (Boolean) data);
        else if (data instanceof byte[])
            values.put(columnName, (byte[]) data);

        return values;
    }

    public void delete(String string) {
        writeableDatabase().execSQL("DELETE FROM " + this.tableName + " WHERE " + this.searchingForString + " = ?", new String[] { string } );
    }

    public Object fetchSingleItem(String string) {
        Object item;
        Cursor cursor = createCursorUsingString(string);
        if (cursor.getCount() == 0)
            return null;
        else if (cursor.moveToFirst() && cursor.getCount() == 1)
            item = createObjectFrom(cursor);
        else
            throw new IllegalStateException("Only meant to return one. Count was == " + cursor.getCount());
        cursor.close();
        return item;
    }

    private Cursor createCursorUsingString(String string) {
        String search = String.format("%s%s%s", "'", string, "'");
        String query = "SELECT * FROM " + this.tableName + " WHERE " + this.searchingForString + " = " + search;
        return readableDatabase().rawQuery(query, null);
    }

    public List<Object> getObjectsWithQuery(String string) {
        Log.i(TAG,"Ok so we are at least getting the part where we look for the items");
        List<Object> allObjects = new ArrayList<>();
        Cursor cursor = readableDatabase().rawQuery("SELECT * FROM " + getLocalDatabase().TO_DO_ITEMS_TABlE, null);
        if(string != null)
            cursor = createCursorUsingString(string);

        if(cursor.getCount() == 0)
            return allObjects; // Return the empty array

        // Starting at the first row, continue to move until past the last row.
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            allObjects = createObjectAndAddToList(allObjects, cursor);
            cursor.moveToNext();
        }

        cursor.close();
        return allObjects;
    }

    public void deleteAll() {
        writeableDatabase().execSQL("DELETE FROM " + this.tableName);
    }

    public Database getLocalDatabase() {
        if(localDB == null)
            localDB = ToDoApplication.getDatabase();
        return localDB;
    }

    private SQLiteDatabase writeableDatabase() {
        if (localDB == null)
            localDB = ToDoApplication.getDatabase();
        return localDB.getWritableDatabase();
    }

    private SQLiteDatabase readableDatabase() {
        if (localDB == null)
            localDB = ToDoApplication.getDatabase();
        return localDB.getReadableDatabase();
    }

    /// These are overridden
    public List<Object> createObjectAndAddToList(List<Object> list, Cursor cursor) {
        return null;
    }

    public Object createObjectFrom(Cursor cursor) {
        return null;
    }
}
