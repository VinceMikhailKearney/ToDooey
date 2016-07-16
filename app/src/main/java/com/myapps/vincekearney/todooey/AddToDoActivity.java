package com.myapps.vincekearney.todooey;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.EditText;
import android.view.View;

public class AddToDoActivity extends AppCompatActivity {

    public static final String ToDo_Desc = "todo";

    private EditText descView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_do);
        this.descView = (EditText) findViewById(R.id.toDoText);
        this.descView.setOnKeyListener(new View.OnKeyListener()
        {
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER)
                {
                    addToDoClicked(v);
                    return true;
                }
                return false;
            }
        });
    }

    public void addToDoClicked(View view)
    {
        // Here we need to get the text from the text view
        String toDo = this.descView.getText().toString();

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
