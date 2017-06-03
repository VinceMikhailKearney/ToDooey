package com.myapps.vincekearney.todooey;

import android.app.Application;
import android.content.Context;

import com.myapps.vincekearney.todooey.Database.Database;
import com.myapps.vincekearney.todooey.Database.ToDoItemHelper;

/**
 * Created by vincekearney on 03/06/2017.
 */

public class ToDoApplication extends Application {

    private static final String TAG = "ToDoApplication";
    private static Context appContext;
    // Local Database
    private static Database database;
    private static ToDoItemHelper toDoItemHelper;

    @Override
    public void onCreate() {
        super.onCreate();
        this.database = new Database(this);
        this.toDoItemHelper = new ToDoItemHelper();
        this.appContext = getApplicationContext();
    }

    public static ToDoItemHelper getLocalItemHelper() {
        return toDoItemHelper;
    }

    public static Database getDatabase() {
        return database;
    }

    public static Context getAppContext() {
        return appContext;
    }
}
