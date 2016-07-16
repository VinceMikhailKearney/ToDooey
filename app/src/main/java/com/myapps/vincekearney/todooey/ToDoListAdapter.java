package com.myapps.vincekearney.todooey;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;

import java.util.List;

public class ToDoListAdapter extends BaseAdapter
{
    private static final String TAG = "ToDoAdapter";
    private Context myContext;
    private LayoutInflater myInflater;
    private List<ToDoItem> myDataSource;
    private CheckBox toDoCheckBox;

    private ToDoListAdapterListener toDoListener;

    public interface ToDoListAdapterListener
    {
        void OnClickItem(ToDoItem item);
    }

    public ToDoListAdapter(Context context, List<ToDoItem> items) {
        myContext = context;
        myDataSource = items;
        myInflater = (LayoutInflater) myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setToDoListAdapterListener(ToDoListAdapterListener listener)
    {
        this.toDoListener = listener;
    }

    public class ToDoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        private ToDoItem toDoItem;
        protected CheckBox checkBox;

        public ToDoViewHolder(View itemView) {
            super(itemView);
            Log.i(TAG, "ToDoViewHolder");
            this.checkBox = (CheckBox) itemView.findViewById(R.id.checkBox);
            this.checkBox.setOnClickListener(this);
        }

        public void bind(ToDoItem item)
        {
            Log.i(TAG, "bind");
            this.toDoItem = item;
            checkBox.setChecked(item.getCompleted());
            checkBox.setText(item.getTodotext());
        }

        @Override
        public void onClick(View v) {
            Log.i(TAG, "onClick");
            if(toDoListener != null) {
                Log.i(TAG, "onClick --> toDoListener is not null");
                toDoListener.OnClickItem(this.toDoItem);
            }
        }
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
        Log.i(TAG, "getView");
        RecyclerView.ViewHolder holder = new ToDoViewHolder(myInflater.inflate(R.layout.to_do_list_row, parent, false));
        convertView = holder.itemView;
        ((ToDoViewHolder) holder).bind((ToDoItem) getItem(position));
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
