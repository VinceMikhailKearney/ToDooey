package com.myapps.vincekearney.todooey;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

public class ToDoListAdapter extends BaseAdapter
{
    private static final String TAG = "ToDoAdapter";
    /* ---- Properties ---- */
    private LayoutInflater inflater;
    private List<ToDoItem> todoList;
    private ToDoListAdapterListener toDoListener;

    // The interface (or protocol in iOS for delegate) that states the methods the listener implements.
    public interface ToDoListAdapterListener
    {
        void OnClickItem(ToDoItem item);
        void DeleteItem(ToDoItem item);
    }

    /* ---- Constructor and setter methods ---- */
    public ToDoListAdapter(Context context, List<ToDoItem> items)
    {
        this.todoList = items;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setToDoList(List<ToDoItem> list) {
        this.todoList = list;
    }

    public void setToDoListAdapterListener(ToDoListAdapterListener listener) {
        this.toDoListener = listener;
    }

    /* ---- Adapter populating and data methods ---- */
    // Essentially numberOfRowsInSection
    @Override
    public int getCount() {
        return this.todoList.size();
    }

    // Basically the cell that we use in cellForRowAtIndexPath
    // Todo - See if there are any other performance improvements that could be made.
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        Log.i(TAG, "getView");
        ToDoViewHolder holder;
        if(convertView == null)
        {
            holder = new ToDoViewHolder(this.inflater.inflate(R.layout.to_do_list_row, parent, false));
            convertView = holder.itemView;
            convertView.setTag(holder);
        } else {
            holder = (ToDoViewHolder) convertView.getTag();
        }

        holder.bind((ToDoItem) getItem(position));
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
        return this.todoList.get(position);
    }

    /* ---- A ViewHolder class that simplifies and improves efficiency of setting up views for the ListView ---- */
    public class ToDoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener
    {
        protected CheckBox checkBox;
        protected TextView date;
        private ToDoItem toDoItem;

        /* ---- Constructor ---- */
        public ToDoViewHolder(View itemView)
        {
            super(itemView);
            Log.i(TAG, "ToDoViewHolder");
            this.checkBox = (CheckBox) itemView.findViewById(R.id.checkBox);
            this.date = (TextView) itemView.findViewById(R.id.dateText);
            this.checkBox.setOnClickListener(this);
            this.checkBox.setOnLongClickListener(this);
        }

        public void bind(ToDoItem item)
        {
            Log.i(TAG, "bind");
            this.toDoItem = item;
            this.checkBox.setChecked(item.getCompleted());
            this.checkBox.setText(item.getTodotext());
            this.date.setText(formatDate(this.toDoItem.getDate()));
        }

        /* ---- OnClick/LongClick Listener methods ---- */
        @Override
        public void onClick(View v)
        {
            if(toDoListener != null) {
                Log.i(TAG, "onClick --> toDoListener is not null");
                toDoListener.OnClickItem(this.toDoItem);
            }
        }

        @Override
        public boolean onLongClick(View v)
        {
            if(toDoListener != null)
                toDoListener.DeleteItem(this.toDoItem);
            return false;
        }

        /* ---- Helper method ---- */
        private String formatDate(Date newDate) {
            return DateUtils.formatSameDayTime(newDate.getTime(), (new Date().getTime()), DateFormat.SHORT, DateFormat.SHORT).toString();
        }

        /* ===============END OF VIEW HOLDER CLASS=============== */
    }

    /* ===============END OF CLASS=============== */
}
