package com.myapps.vincekearney.todooey;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
//import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;
//import android.view.WindowManager;

public class ToDoListActivity extends AppCompatActivity implements ToDoListAdapter.ToDoListAdapterListener
{
    private final int TODO_ADDED = 1;
    private static final String TAG = "ToDoListActivity";
    private ToDoDBHelper dbHelper;
    private ToDoListAdapter toDoAdapter;
    private ListView toDoList;
    private List<ToDoItem> toDoListItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        // Here we are telling the app that we want to be full screen - I.e. The menu bar at the top is gone (date and battery thang).
        // getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_to_do_list);
        this.toDoList = (ListView) findViewById(R.id.toDoList);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.dbHelper = new ToDoDBHelper(this);
        this.toDoListItems = dbHelper.getAllToDos();
        this.toDoAdapter = new ToDoListAdapter(this, this.toDoListItems);
        this.toDoAdapter.setToDoListAdapterListener(this);
        this.toDoList.setAdapter(this.toDoAdapter);
    }

    public void startIntent(View view)
    {
        Intent addToDoIntent = new Intent(ToDoListActivity.this, AddToDoActivity.class);
        startActivityForResult(addToDoIntent,TODO_ADDED);
    }
    // My on click events
    public void deleteAllToDoItems(View view)
    {
        dbHelper.deleteAllToDos();
        this.toDoListItems = dbHelper.getAllToDos();
        this.toDoAdapter.setToDoList(this.toDoListItems);
        this.toDoAdapter.notifyDataSetChanged();
    }

    // Delete single to do
    private void delete(final ToDoItem toDoItem)
    {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ToDoListActivity.this);
        alertDialogBuilder.setTitle(R.string.delete_to_do);
        alertDialogBuilder
                .setMessage(toDoItem.getTodotext())
                .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dbHelper.toDo(toDoItem.getId(), ToDoDBHelper.getOrDelete.DELETE_TODO); // This returns null.
                        toDoListItems.remove(toDoItem);
                        toDoAdapter.notifyDataSetChanged();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                }).show();
    }

    // Overriding of the ToDoListAdapaterListener method.
    @Override
    public void OnClickItem(ToDoItem item) {
        Log.i(TAG, "Clicked a check box and received listener event.");
        dbHelper.updateCompleted(item.getId(), !item.getCompleted());
    }

    @Override
    public void DeleteItem(ToDoItem item) {
        this.delete(item);
    }

    // This is the callback when AddToDoActivity finishes - Passes an Intent with data that we can use.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        // We need to make sure the requestCode matches the task that we asked for above.
        // If result is OK - We have something we need to look at.
        if (requestCode == TODO_ADDED && resultCode == RESULT_OK)
        {
            // Get the string value that has the ID entered in the parameter.
            String toDo = data.getStringExtra(AddToDoActivity.ToDo_Desc);
            this.toDoListItems.add(dbHelper.addToDo(toDo));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_to_do_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
            return true;

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    //==============================END OF CLASS==============================
}
