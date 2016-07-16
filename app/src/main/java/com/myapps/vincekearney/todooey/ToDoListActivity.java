package com.myapps.vincekearney.todooey;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ToDoListActivity extends AppCompatActivity implements ToDoListAdapter.ToDoListAdapterListener, DeleteToDoDialog.DeleteDialogListener
{
    private final int TODO_ADDED = 1;
    private static final String TAG = "ToDoListActivity";
    private ToDoDBHelper dbHelper;
    private ToDoListAdapter toDoAdapter;
    private DeleteToDoDialog deleteToDoDialog;
    private ListView toDoList;
    private List<ToDoItem> toDoListItems = new ArrayList<>();

    //==================================================================================================
    private ActionBar actionBar;
    private ListView drawerList;
    private ArrayAdapter<String> navArrayAdapter;
    private ActionBarDrawerToggle drawerToggle;
    private DrawerLayout drawerLayout;
    private String activityTitle;
    //==================================================================================================

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

        //==================================================================================================
        this.actionBar = getSupportActionBar();
        if(this.actionBar != null) {
            this.actionBar.setDisplayHomeAsUpEnabled(true); // Why NPE?
            this.actionBar.setHomeButtonEnabled(true);
        }

        this.drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        this.activityTitle = this.getTitle().toString();
        this.populateNavDrawer();
        this.setupDrawer();
        //==================================================================================================

        this.dbHelper = new ToDoDBHelper(this);
        this.toDoListItems = dbHelper.getAllToDos();
        this.toDoAdapter = new ToDoListAdapter(this, this.toDoListItems);
        this.toDoAdapter.setToDoListAdapterListener(this);
        this.toDoList.setAdapter(this.toDoAdapter);
        this.deleteToDoDialog = new DeleteToDoDialog(this);
        this.deleteToDoDialog.setListener(this);
    }

    // My on click events
    public void startIntent(View view)
    {
        Intent addToDoIntent = new Intent(ToDoListActivity.this, AddToDoActivity.class);
        startActivityForResult(addToDoIntent,TODO_ADDED);
    }

    public void deleteAllToDoItems(View view)
    {
        this.deleteToDoDialog.setDialogToDo(null).show();
    }

    // ToDoListAdapterListener methods
    @Override
    public void OnClickItem(ToDoItem item) {
        Log.i(TAG, "Clicked a check box and received listener event.");
        dbHelper.updateCompleted(item.getId(), !item.getCompleted());
    }

    @Override
    public void DeleteItem(ToDoItem item) {
        this.deleteToDoDialog.setDialogToDo(item).show();
    }

    // DeleteToDoDialogListener methods
    @Override
    public void DeleteToDo(ToDoItem item)
    {
        dbHelper.toDo(item.getId(), ToDoDBHelper.getOrDelete.DELETE_TODO); // This returns null.
        toDoListItems.remove(item);
        toDoAdapter.notifyDataSetChanged();
    }

    @Override
    public void DeleteAllToDos()
    {
        dbHelper.deleteAllToDos();
        this.toDoListItems = dbHelper.getAllToDos();
        this.toDoAdapter.setToDoList(this.toDoListItems);
        this.toDoAdapter.notifyDataSetChanged();
    }

    // This is the callback when AddToDoActivity finishes - Passes an Intent with data that we can use.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i(TAG, "Got a result back from the add activity.");
        // We need to make sure the requestCode matches the task that we asked for above.
        // If result is OK - We have something we need to look at.
        if (requestCode == TODO_ADDED && resultCode == RESULT_OK)
        {
            // Get the string value that has the ID entered in the parameter.
            String toDo = data.getStringExtra(AddToDoActivity.ToDo_Desc);
            this.toDoListItems.add(dbHelper.addToDo(toDo));
            this.toDoAdapter.notifyDataSetChanged();
        }
    }

    // Other
    private void populateNavDrawer()
    {
        this.drawerList = (ListView) findViewById(R.id.navList);
        String[] osArray = { "All", "Completed", "Not Completed" };
        this.navArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, osArray);
        this.drawerList.setAdapter(this.navArrayAdapter);

        this.drawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(ToDoListActivity.this, "We want to change the to do's we show", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupDrawer()
    {
        this.drawerToggle = new ActionBarDrawerToggle(this, this.drawerLayout, R.string.drawer_open, R.string.drawer_close)
        {

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView)
            {
                super.onDrawerOpened(drawerView);
                if(actionBar != null) actionBar.setTitle(R.string.choose_to_do_list);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view)
            {
                super.onDrawerClosed(view);
                if(actionBar != null) actionBar.setTitle(activityTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        this.drawerToggle.setDrawerIndicatorEnabled(true);
        this.drawerLayout.setDrawerListener(this.drawerToggle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState)
    {
        super.onPostCreate(savedInstanceState);
        this.drawerToggle.syncState();
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

        // If the navigation drawer is not set up - Let's just catch the NPE here.
        if(this.drawerToggle == null) {
            Toast.makeText(ToDoListActivity.this, "The drawer toggle is null. Would throw NPE.", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (this.drawerToggle.onOptionsItemSelected(item))
            return true;

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
            return true;

        return super.onOptionsItemSelected(item);
    }

    // To manage orientation changing with dialogs showing.
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        this.drawerToggle.onConfigurationChanged(newConfig);
    }

    //==============================END OF CLASS==============================
}
