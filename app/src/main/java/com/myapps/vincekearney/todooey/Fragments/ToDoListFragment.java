package com.myapps.vincekearney.todooey.Fragments;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.myapps.vincekearney.todooey.Database.DatabaseManager;
import com.myapps.vincekearney.todooey.Database.ToDoItem;
import com.myapps.vincekearney.todooey.DeleteToDoDialog;
import com.myapps.vincekearney.todooey.R;
import com.myapps.vincekearney.todooey.ToDoListAdapter;

import java.util.ArrayList;
import java.util.List;

public class ToDoListFragment extends Fragment implements ToDoListAdapter.ToDoListAdapterListener, DeleteToDoDialog.DeleteDialogListener {
    // private final int TODO_ADDED = 1; This is still in the MainActivity
    private static final String TAG = "ToDoListFragment";
    private static final String KEY_HIDE_ITEMS = "pref_key_hide";
    private static final String KEY_HIDE_ALL = "pref_key_hide_all";
    /* ---- Properties ---- */
    private DeleteToDoDialog deleteToDoDialog;
    private List<ToDoItem> toDoListItems = new ArrayList<>();
    private TextView currentToDoList;
    public ToDoListAdapter toDoAdapter;
    private SharedPreferences sharedPreferences;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.to_do_list_fragment, container, false);

        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        RecyclerView toDoList = (RecyclerView) view.findViewById(R.id.toDoList);
        toDoList.setHasFixedSize(true);
        this.currentToDoList = (TextView) view.findViewById(R.id.currentToDoList);
        if (this.currentToDoList != null)
            this.currentToDoList.setText(R.string.all);

        // ToDoAdapter
        this.toDoAdapter = new ToDoListAdapter(this.toDoListItems);
        this.toDoAdapter.setToDoListAdapterListener(this);
        toDoList.setLayoutManager(new LinearLayoutManager(getActivity()));
        toDoList.setAdapter(this.toDoAdapter);

        // Set up other
        Log.i(TAG, "About to try and refresh the ToDos here");
        this.refreshToDos(0); // The default list, at the minute, is ALL. So refresh for this.
        Log.i(TAG, "Well? Did we?");

        // Delete Dialog
        // https://stackoverflow.com/questions/7933206/android-unable-to-add-window-token-null-is-not-for-an-application-exception
        this.deleteToDoDialog = new DeleteToDoDialog(getActivity());
        this.deleteToDoDialog.setListener(this);

        return view;
    }

    /* ---- onClick methods ---- */
    public void deleteAllToDoItems() {
        this.deleteToDoDialog.showAlert(null);
    }

    /* ---- Helper methods ---- */
    public void refreshToDos(int position) {
        switch (position) {
            case 0:
                if (this.sharedPreferences.getBoolean(KEY_HIDE_ALL, false) == true && this.sharedPreferences.getBoolean(KEY_HIDE_ITEMS,false) == true)
                    this.toDoListItems = DatabaseManager.toDoItemHelper().getToDosCompleted(false);
                else
                    this.toDoListItems = DatabaseManager.toDoItemHelper().getAllToDos();
                this.currentToDoList.setText(R.string.all);
                break;
            case 1:
                this.toDoListItems = DatabaseManager.toDoItemHelper().getToDosCompleted(true);
                this.currentToDoList.setText(R.string.completed);
                break;
            case 2:
                this.toDoListItems = DatabaseManager.toDoItemHelper().getToDosCompleted(false);
                this.currentToDoList.setText(R.string.not_completed);
                break;
            case 3:
                this.toDoListItems = DatabaseManager.toDoItemHelper().getToDosFromToday();
                this.currentToDoList.setText(R.string.today);
                break;
            default:
                break;
        }

        this.toDoAdapter.setToDoList(this.toDoListItems);
        this.toDoAdapter.notifyDataSetChanged();
    }

    /* ---- ToDoListAdapterListener methods ---- */
    @Override
    public void OnClickItem(ToDoItem item) {
        Log.i(TAG, "Clicked a check box and received listener event.");
        DatabaseManager.toDoItemHelper().updateCompleted(item.getId(), !item.getCompleted());
        this.toDoListItems = DatabaseManager.toDoItemHelper().getAllToDos();
        this.toDoAdapter.setToDoList(this.toDoListItems);
    }

    @Override
    public void DeleteItem(ToDoItem item) {
        this.deleteToDoDialog.showAlert(item);
    }

    /* ---- DeleteToDoDialogListener methods ---- */
    @Override
    public void DeleteToDo(ToDoItem item) {
        DatabaseManager.toDoItemHelper().deleteToDo(item.getId());
        this.toDoListItems.remove(item);
        this.toDoAdapter.setToDoList(this.toDoListItems);
    }

    @Override
    public void DeleteAllToDos() {
        DatabaseManager.toDoItemHelper().deleteAll();
        this.toDoListItems = DatabaseManager.toDoItemHelper().getAllToDos();
        this.toDoAdapter.setToDoList(this.toDoListItems);
    }

    /* --- Callback for startActivityForResult() ---- */
    // Passes an Intent with data that we can us.
    // THE ACTIVITY STILL STARTS THE INTENT AND HANDLES THIS - Need to figure out
    // how to get it here instead.
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data)
//    {
//        super.onActivityResult(requestCode, resultCode, data);
//        Log.i(TAG, "Got a result back from the add activity.");
//        // We need to make sure the requestCode matches the task that we asked for above.
//        // If result is OK - We have something we need to look at.
//        if (requestCode == TODO_ADDED && resultCode == Activity.RESULT_OK)
//        {
//            // Get the string value that has the ID entered in the parameter.
//            String to_Do = data.getStringExtra(AddToDoActivity.ToDo_Desc);
//            this.toDoListItems.add(dbHelper.addToDo(to_Do));
//            this.toDoAdapter.setToDoList(this.toDoListItems);
//        }
//    }
}
