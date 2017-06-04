package com.myapps.vincekearney.todooey;

import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.myapps.vincekearney.todooey.Database.ToDoItem;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

public class ToDoListAdapter extends RecyclerView.Adapter<ToDoListAdapter.ToDoViewHolder> {
    private static final String TAG = "ToDoAdapter";
    /* ---- Properties ---- */
    private List<ToDoItem> todoList;
    private ToDoListAdapterListener toDoListener;

    // The interface (or protocol in iOS for delegate) that states the methods the listener implements.
    public interface ToDoListAdapterListener {
        void OnClickItem(ToDoItem item);

        void DeleteItem(ToDoItem item);
    }

    /* ---- Constructor and setter methods ---- */
    public ToDoListAdapter() {}

    public void setToDoList(List<ToDoItem> list) {
        Log.i(TAG, "Setting to do list on the adapter");
        this.todoList = list;
        this.notifyDataSetChanged();
    }

    public void setToDoListAdapterListener(ToDoListAdapterListener listener) {
        this.toDoListener = listener;
    }

    /* ---- Adapter populating and data methods ---- */
    // Basically the cell that we use in cellForRowAtIndexPath
    @Override
    public ToDoListAdapter.ToDoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.i(TAG, "onCreateViewHolder");
        final View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.to_do_list_row, parent, false);
        return new ToDoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ToDoViewHolder holder, int position) {
        Log.i(TAG, "onBindViewHolder");
        holder.setToDoItem(getItem(position));
        holder.checkBox.setChecked(holder.toDoItem.getCompleted());
        holder.checkBox.setText(holder.toDoItem.getTodotext());
        holder.date.setText(holder.formatDate(holder.toDoItem.getDate()));
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return this.todoList.size();
    }

    public ToDoItem getItem(int position) {
        return this.todoList.get(position);
    }

    /* ---- A ViewHolder class that simplifies and improves efficiency of setting up views for the ListView ---- */
    public class ToDoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        protected CheckBox checkBox;
        protected TextView date;
        protected ToDoItem toDoItem;

        public ToDoViewHolder(View itemView) {
            super(itemView);
            Log.i(TAG, "ToDoViewHolder");
            this.checkBox = (CheckBox) itemView.findViewById(R.id.checkBox);
            this.date = (TextView) itemView.findViewById(R.id.dateText);
            this.checkBox.setOnClickListener(this);
            this.checkBox.setOnLongClickListener(this);
        }

        public void setToDoItem(ToDoItem item) {
            this.toDoItem = item;
        }

        private String formatDate(Date newDate) {
            return DateUtils.formatSameDayTime(newDate.getTime(), (new Date().getTime()), DateFormat.SHORT, DateFormat.SHORT).toString();
        }

        @Override
        public void onClick(View v) {
            if (toDoListener != null) {
                Log.i(TAG, "onClick --> toDoListener is not null");
                toDoListener.OnClickItem(this.toDoItem);
            }
        }

        @Override
        public boolean onLongClick(View v) {
            if (toDoListener != null)
                toDoListener.DeleteItem(this.toDoItem);
            return false;
        }
    }
}
