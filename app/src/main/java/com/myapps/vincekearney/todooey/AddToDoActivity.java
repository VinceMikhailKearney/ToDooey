package com.myapps.vincekearney.todooey;

import android.content.Intent;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Button;
import android.view.View;

public class AddToDoActivity extends AppCompatActivity {

    public static final String ToDo_Desc = "todo";

    private EditText descView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_do);
        descView = (EditText) findViewById(R.id.toDoText);
    }

    public void addToDoClicked(View view)
    {
        // Here we need to get the text from the to text view
        String toDo = descView.getText().toString();

        if(toDo.isEmpty())
        {
            // As the to do item is empty we are simply cancelling. Set the result code to cancelled.
            setResult(RESULT_CANCELED);
        }
        else
        {
            // Here we have data that we want to pass back.
            // We create an Intent that is used to hold the data and set the result code
            // to OK to indicate that there is something we need to look at.
            Intent result = new Intent();
            result.putExtra(ToDo_Desc, toDo);
            setResult(RESULT_OK, result);
        }

        // Destroy the activity - We are done with it.
        finish();
    }
    //==============================END OF CLASS==============================
}
