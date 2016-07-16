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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ToDoListAdapter extends BaseAdapter
{
    private static final String TAG = "ToDoAdapter";
    private Context context;
    private LayoutInflater inflater;
    private List<ToDoItem> todoList;

    private ToDoListAdapterListener toDoListener;

    public interface ToDoListAdapterListener
    {
        void OnClickItem(ToDoItem item);
        void DeleteItem(ToDoItem item);
    }

    public ToDoListAdapter(Context context, List<ToDoItem> items) {
        this.context = context;
        this.todoList = items;
        this.inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setToDoList(List<ToDoItem> list)
    {
        this.todoList = list;
    }

    public void setToDoListAdapterListener(ToDoListAdapterListener listener)
    {
        this.toDoListener = listener;
    }

    public class ToDoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener
    {
        private ToDoItem toDoItem;
        protected CheckBox checkBox;
        protected TextView date;

        public ToDoViewHolder(View itemView) {
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

        @Override
        public void onClick(View v) {
            if(toDoListener != null) {
                Log.i(TAG, "onClick --> toDoListener is not null");
                toDoListener.OnClickItem(this.toDoItem);
            }
        }

        @Override
        public boolean onLongClick(View v) {
            if(toDoListener != null)
                toDoListener.DeleteItem(this.toDoItem);
            return false;
        }

        private String formatDate(Date newDate)
        {
            Log.i(TAG, "[DATE] newDate in milliseconds is: " + newDate.getTime());
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(newDate.getTime());

            Date calendarDate = calendar.getTime();
            Log.i(TAG, "[DATE] The calendarDate time is: " + calendarDate.getTime());

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
            String formattedDate = dateFormat.format(calendar.getTime());
            Log.i(TAG, "[DATE] formattedDate: " + formattedDate);

            return DateUtils.formatSameDayTime(newDate.getTime(), (new Date().getTime()), DateFormat.SHORT, DateFormat.SHORT).toString();

//            if(DateUtils.isToday(newDate.getTime()))
//                return "Test";
//
//            return newDate.toString();
        }
    }

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
        }
        else
        {
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
}
