package com.myapps.vincekearney.todooey;

import javax.persistence.*;
import java.util.Date;

public class ToDoItem
{
    private Integer todoid;
    private Boolean completed;
    private Date date;
    private String todotext;

    public ToDoItem () {}

    public ToDoItem(String todotext, Integer todoid, Boolean completed, Date date)
    {
        this.todotext = todotext;
        this.todoid = todoid;
        this.completed = completed;
        this.date = date;
    }

    public String getTodotext() { return this.todotext; }
    public void setTodotext(String todotext) { this.todotext = todotext; }

    public Integer getId() { return this.todoid; }
    public void setId(Integer todoid) { this.todoid = todoid; }

    public Boolean getCompleted() { return this.completed; }
    public void setCompleted(Boolean completed) { this.completed = completed; }

    public Date getDate() { return this.date; }
    public void setDate(Date date) {this.date = date; }

    @Override
    public String toString() {
        return "ToDoItem{" +
                "id = " + this.todoid +
                "/ntext = " + this.todotext +
                "/ndate = " + this.date +
                "/ncompleted = " + this.completed + '}';
    }

    //==============================END OF CLASS==============================
}
