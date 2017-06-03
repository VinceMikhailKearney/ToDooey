package com.myapps.vincekearney.todooey.Activities;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
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

import com.myapps.vincekearney.todooey.Database.DatabaseManager;
import com.myapps.vincekearney.todooey.R;
import com.myapps.vincekearney.todooey.Fragments.SettingsFragment;
import com.myapps.vincekearney.todooey.Fragments.ToDoListFragment;

public class ToDoListActivity extends AppCompatActivity {
    private final int TODO_ADDED = 1;
    private static final String TAG = "ToDoListActivity";
    /* ---- Properties ---- */
    private ActionBar actionBar;
    private ActionBarDrawerToggle drawerToggle;
    private DrawerLayout drawerLayout;
    private String activityTitle;
    private ToDoListFragment toDoFragment;
    private int drawerPosition;

    /* --- Lifecycle methods ---- */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Here we are telling the app that we want to be full screen - I.e. The menu bar at the top is gone (date and battery thang).
        // getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Set content view and assign others
        setContentView(R.layout.activity_to_do_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        this.drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        this.drawerPosition = 0;

        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

        if (findViewById(R.id.fragment_container) != null) {
            if (savedInstanceState != null)
                return;

            this.toDoFragment = new ToDoListFragment();
            this.changeFragment(this.toDoFragment, false);
        }

        // Set up action bar
        setSupportActionBar(toolbar);
        this.actionBar = getSupportActionBar();
        if (this.actionBar != null) {
            this.actionBar.setDisplayHomeAsUpEnabled(true);
            this.actionBar.setHomeButtonEnabled(true);
        }

        // Set up and populate navigation drawer
        this.populateNavDrawer();
        this.setupDrawer();
        this.activityTitle = this.getTitle().toString();
    }

    public void changeFragment(Fragment frag, boolean addToBackStack) {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, frag);
        if (addToBackStack)
            fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        this.drawerToggle.syncState();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_to_do_list, menu);
        return true;
    }

    // Selecting something in the Menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        // If the navigation drawer is not set up - Let's just catch the NPE here.
        if (this.drawerToggle == null) {
            Toast.makeText(ToDoListActivity.this, "The drawer toggle is null. Would throw NPE.", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (this.drawerToggle.onOptionsItemSelected(item))
            return true;

        if (id == R.id.action_settings) {
            this.changeFragment(new SettingsFragment(), true);
            return true;
        }

        if (id == R.id.action_deleteAllToDos) {
            this.toDoFragment.deleteAllToDoItems();
            this.toDoFragment.refreshToDos(this.drawerPosition);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // To manage orientation changing with dialogs showing.
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        this.drawerToggle.onConfigurationChanged(newConfig);
    }

    /* ---- onClick methods ---- */
    // FAB
    public void startIntent(View view) {
        Intent addToDoIntent = new Intent(ToDoListActivity.this, AddToDoActivity.class);
        startActivityForResult(addToDoIntent, TODO_ADDED);
    }

    /* ---- Navigation Drawer ---- */
    private void populateNavDrawer() {
        ListView drawerList = (ListView) findViewById(R.id.navList);
        String[] osArray = {"All", "Completed", "Not Completed", "Today"};
        ArrayAdapter<String> navAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, osArray);
        if (drawerList != null) {
            drawerList.setAdapter(navAdapter);
            drawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    toDoFragment.refreshToDos(position);
                    drawerLayout.closeDrawers();
                }
            });
        }
    }

    private void setupDrawer() {
        this.drawerToggle = new ActionBarDrawerToggle(this, this.drawerLayout, R.string.drawer_open, R.string.drawer_close) {
            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if (actionBar != null) actionBar.setTitle(R.string.choose_to_do_list);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                if (actionBar != null) actionBar.setTitle(activityTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        this.drawerToggle.setDrawerIndicatorEnabled(true);
        this.drawerLayout.addDrawerListener(this.drawerToggle);
    }

    /* --- Callback for startActivityForResult() ---- */
    // Passes an Intent with data that can be used.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i(TAG, "Got a result back from the add activity.");
        // We need to make sure the requestCode matches the task that we asked for above.
        // If result is OK - We have something we need to look at.
        if (requestCode == TODO_ADDED && resultCode == RESULT_OK) {
            // Get the string value that has the ID entered in the parameter.
            DatabaseManager.toDoItemHelper().addToDo(data.getStringExtra(AddToDoActivity.ToDo_Desc));
            this.toDoFragment.refreshToDos(this.drawerPosition);
        }
    }
}
