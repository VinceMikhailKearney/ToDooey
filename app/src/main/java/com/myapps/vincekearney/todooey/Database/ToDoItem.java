package com.myapps.vincekearney.todooey.Database;

import java.util.Date;

public class ToDoItem {
    /* ---- Properties ---- */
    private Boolean completed;
    private Date date;
    private String todoid;
    private String todotext;

    public ToDoItem () {}

    // The below are organised corresponding to their place in the DB table (0 - 3)
    public void setId(String todoid) { this.todoid = todoid; }
    public void setTodotext(String todotext) { this.todotext = todotext; }
    public void setCompleted(Boolean completed) { this.completed = completed; }
    public void setDate(Date date) {this.date = date; }

    public String getId() { return this.todoid; }
    public String getTodotext() { return this.todotext; }
    public Boolean getCompleted() { return this.completed; }
    public Date getDate() { return this.date; }

    /* ---- Override toString to print out ToDoItem ---- */
    @Override
    public String toString()
    {
        return "\nToDoItem{" +
                "\nid = " + this.todoid +
                "\ntext = " + this.todotext +
                "\ndate = " + this.date +
                "\ncompleted = " + this.completed + '}';
    }
}
