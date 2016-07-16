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
    private Context context;
    private LayoutInflater inflater;
    private List<ToDoItem> dataSource;

    private ToDoListAdapterListener toDoListener;

    public interface ToDoListAdapterListener
    {
        void OnClickItem(ToDoItem item);
    }

    public ToDoListAdapter(Context context, List<ToDoItem> items) {
        this.context = context;
        this.dataSource = items;
        this.inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
            this.checkBox = (CheckBox) itemView.findViewById(R.id.checkBox);
            this.checkBox.setOnClickListener(this);
        }

        public void bind(ToDoItem item)
        {
            this.toDoItem = item;
            this.checkBox.setChecked(item.getCompleted());
            this.checkBox.setText(item.getTodotext());
        }

        @Override
        public void onClick(View v) {
            if(toDoListener != null) {
                Log.i(TAG, "onClick --> toDoListener is not null");
                toDoListener.OnClickItem(this.toDoItem);
            }
        }
    }

    // Essentially numberOfRowsInSection
    @Override
    public int getCount() {
        return this.dataSource.size();
    }

    // Basically the cell that we use in cellForRowAtIndexPath
    // Todo - Only create a new ToDoViewHolder if we actually need to.
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        RecyclerView.ViewHolder holder = new ToDoViewHolder(this.inflater.inflate(R.layout.to_do_list_row, parent, false));
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
        return this.dataSource.get(position);
    }
}
