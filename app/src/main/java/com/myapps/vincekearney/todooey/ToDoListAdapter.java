package com.myapps.vincekearney.todooey;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;

import java.util.List;

public class ToDoListAdapter extends BaseAdapter
{
    private Context myContext;
    private LayoutInflater myInflater;
    private List<ToDoItem> myDataSource;
    private CheckBox toDoCheckBox;

    public ToDoListAdapter(Context context, List<ToDoItem> items) {
        myContext = context;
        myDataSource = items;
        myInflater = (LayoutInflater) myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    // Essentially numberOfRowsInSection
    @Override
    public int getCount() {
        return myDataSource.size();
    }

    // Basically the cell that we use in cellForRowAtIndexPath
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        if(convertView == null)
            convertView = myInflater.inflate(R.layout.to_do_list_row, parent, false);

        ToDoItem item = (ToDoItem) getItem(position);
        this.toDoCheckBox = (CheckBox) convertView.findViewById(R.id.checkBox);
        this.toDoCheckBox.setText(item.getTodotext());
        this.toDoCheckBox.setChecked(item.getCompleted());
        return convertView;
    }

    // So as far as I can see so far - indexPath.row
    @Override
    public long getItemId(int position) {
        return position;
    }

    // This just pulls out the object that we want for the row we're filling in from the data source we passed in.
    @Override
    public Object getItem(int position) {
        return myDataSource.get(position);
    }
}
