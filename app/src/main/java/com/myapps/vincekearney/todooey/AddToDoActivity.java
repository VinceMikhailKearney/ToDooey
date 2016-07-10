package com.myapps.vincekearney.todooey;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Button;
import android.view.View;

public class AddToDoActivity extends AppCompatActivity {

    public static final String TASK_DESC = "task";

    private EditText descView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_do);
        descView = (EditText) findViewById(R.id.toDoText);
    }

    public void addToDoClicked(View view)
    {

    }
}
