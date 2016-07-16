package com.myapps.vincekearney.todooey;

import java.util.Date;

public class ToDoItem
{
    private String todoid;
    private Boolean completed;
    private Date date;
    private String todotext;

    public ToDoItem () {}
    // The methods below are used for setting the object up - Seems easier to read.g
    public String getTodotext() { return this.todotext; }
    public void setTodotext(String todotext) { this.todotext = todotext; }

    public String getId() { return this.todoid; }
    public void setId(String todoid) { this.todoid = todoid; }

    public Boolean getCompleted() { return this.completed; }
    public void setCompleted(Boolean completed) { this.completed = completed; }

    public Date getDate() { return this.date; }
    public void setDate(Date date) {this.date = date; }

    @Override
    public String toString() {
        return "\nToDoItem{" +
                "\nid = " + this.todoid +
                "\ntext = " + this.todotext +
                "\ndate = " + this.date +
                "\ncompleted = " + this.completed + '}';
    }

    //==============================END OF CLASS==============================
}
