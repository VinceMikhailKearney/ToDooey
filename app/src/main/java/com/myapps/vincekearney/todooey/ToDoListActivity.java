package com.myapps.vincekearney.todooey;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
//import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
//import android.view.WindowManager;

public class ToDoListActivity extends AppCompatActivity
{
    private final int TODO_ADDED = 1;
    private TextView label;
    private ToDoDBHelper dbHelper;
    private ListView toDoList;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        // Here we are telling the app that we want to be full screen - I.e. The menu bar at the top is gone (date and battery thang).
        // getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_to_do_list);
        label = (TextView) findViewById(R.id.mainLabel);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.dbHelper = new ToDoDBHelper(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if(fab != null) // Ensure the FAB has been created before adding a listener - Threw an error otherwise.
        {
            fab.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view) {
                    Intent addToDoIntent = new Intent(ToDoListActivity.this, AddToDoActivity.class);
                    startActivityForResult(addToDoIntent,TODO_ADDED);
                    // We are using the above so that the activity that we launch
                    // within this result gets passed data back. We can use information like
                    // this for certain things - Here we are using it to determine whether a To-Do
                    // item was added or not.
                }
            });
        }
    }

    public void deleteAllToDoItems(View view)
    {
        dbHelper.deleteAllToDos();
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
            List<ToDoItem> toDos = new ArrayList<>();
            // Get the string value that has the ID entered in the parameter.
            String toDo = data.getStringExtra(AddToDoActivity.ToDo_Desc);
            dbHelper.addToDo("1",toDo,false);
            for(ToDoItem item : dbHelper.getAllToDos())
                toDos.add(item);
            label.setText(toDos.toString());
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
    //==============================END OF CLASS==============================
}
