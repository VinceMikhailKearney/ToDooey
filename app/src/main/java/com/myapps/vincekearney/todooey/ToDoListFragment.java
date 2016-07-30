package com.myapps.vincekearney.todooey;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ToDoListFragment extends Fragment implements ToDoListAdapter.ToDoListAdapterListener, DeleteToDoDialog.DeleteDialogListener
{
    private final int TODO_ADDED = 1;
    private static final String TAG = "ToDoListFragment";
    /* ---- Properties ---- */
    private DeleteToDoDialog deleteToDoDialog;
    private List<ToDoItem> toDoListItems = new ArrayList<>();
    private TextView currentToDoList;
    private ToDoDBHelper dbHelper;
    private ToDoListAdapter toDoAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.to_do_list_fragment, container, false);
        RecyclerView toDoList = (RecyclerView) view.findViewById(R.id.toDoList);
        toDoList.setHasFixedSize(true);
        this.currentToDoList = (TextView) view.findViewById(R.id.currentToDoList);
        if(this.currentToDoList != null)
            this.currentToDoList.setText(R.string.all);

        // Set up other
        this.dbHelper = new ToDoDBHelper(getActivity());
        this.toDoListItems = dbHelper.getAllToDos();
        // ToDoAdapter
        this.toDoAdapter = new ToDoListAdapter(this.toDoListItems);
        this.toDoAdapter.setToDoList(this.toDoListItems);
        this.toDoAdapter.setToDoListAdapterListener(this);
        toDoList.setLayoutManager(new LinearLayoutManager(getActivity()));
        toDoList.setAdapter(this.toDoAdapter);

        // Delete Dialog
        this.deleteToDoDialog = new DeleteToDoDialog(getActivity());
        this.deleteToDoDialog.setListener(this);

        return view;
    }

    /* ---- onClick methods ---- */
    // Delete All
    public void deleteAllToDoItems(View view)
    {
        this.deleteToDoDialog.setDialogToDo(null).show();
    }

    /* ---- Helper methods ---- */
    public void refreshToDos(int position)
    {
        switch (position)
        {
            case 0:
                this.toDoListItems = dbHelper.getAllToDos();
                this.currentToDoList.setText(R.string.all);
                break;
            case 1:
                this.toDoListItems = dbHelper.getToDos(true);
                this.currentToDoList.setText(R.string.completed);
                break;
            case 2:
                this.toDoListItems = dbHelper.getToDos(false);
                this.currentToDoList.setText(R.string.not_completed);
                break;
            case 3:
                this.toDoListItems = dbHelper.getToDosFromToday();
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
        dbHelper.updateCompleted(item.getId(), !item.getCompleted());
        this.toDoListItems = dbHelper.getAllToDos();
        this.toDoAdapter.setToDoList(this.toDoListItems);
    }

    @Override
    public void DeleteItem(ToDoItem item) {
        this.deleteToDoDialog.setDialogToDo(item).show();
    }

    // /* ---- DeleteToDoDialogListener methods ---- */
    @Override
    public void DeleteToDo(ToDoItem item)
    {
        dbHelper.toDo(item.getId(), ToDoDBHelper.getOrDelete.DELETE_TODO); // This returns null.
        this.toDoListItems.remove(item);
        this.toDoAdapter.setToDoList(this.toDoListItems);
    }

    @Override
    public void DeleteAllToDos()
    {
        dbHelper.deleteAllToDos();
        this.toDoListItems = dbHelper.getAllToDos();
        this.toDoAdapter.setToDoList(this.toDoListItems);
    }

    /* --- Callback for startActivityForResult() ---- */
    // Passes an Intent with data that we can us.
    // THE ACTIVITY STILL STARTS THE INTENT AND HANDLES THIS - Need to figure out
    // how to get it here instead.
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i(TAG, "Got a result back from the add activity.");
        // We need to make sure the requestCode matches the task that we asked for above.
        // If result is OK - We have something we need to look at.
        if (requestCode == TODO_ADDED && resultCode == Activity.RESULT_OK)
        {
            // Get the string value that has the ID entered in the parameter.
            String toDo = data.getStringExtra(AddToDoActivity.ToDo_Desc);
            this.toDoListItems.add(dbHelper.addToDo(toDo));
            this.toDoAdapter.setToDoList(this.toDoListItems);
        }
    }

    /* ===============END OF CLASS=============== */
}
