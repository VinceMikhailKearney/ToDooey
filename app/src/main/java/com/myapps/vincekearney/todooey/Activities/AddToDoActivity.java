package com.myapps.vincekearney.todooey.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.EditText;
import android.view.View;
import android.widget.Toast;

import com.myapps.vincekearney.todooey.R;

public class AddToDoActivity extends AppCompatActivity {
    public static final String ToDo_Desc = "todo";
    /* ---- Properties ---- */
    private EditText toDoText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set content view and assign desc view
        setContentView(R.layout.activity_add_to_do);
        this.toDoText = (EditText) findViewById(R.id.toDoText);

        // Add an OnKeyListener to the toDoText.
        // This essentially adds a listener to any key press on the keyboard. I am interested if the 'Enter' button is pressed down.
        this.toDoText.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    addToDoClicked(v);
                    return true;
                }
                return false;
            }
        });
    }

    public void addToDoClicked(View view) {
        String toDo = this.toDoText.getText().toString();

        if (toDo.isEmpty()) {
            Log.i("AddToDo", "To do text empty");
            Toast.makeText(this, "No text entered for ToDo", Toast.LENGTH_SHORT).show();
        } else {
            Log.i("AddToDo", "We got text");
            /** Here we have data that we want to pass back.
             * We create an Intent that is used to hold the data and set the result code
             * to OK to indicate that there is something we need to look at.
             */
            Intent result = new Intent();
            result.putExtra(ToDo_Desc, toDo);
            setResult(RESULT_OK, result);
        }

        // Destroy the activity - We are done with it.
        finish();
    }

    /* ===============END OF CLASS=============== */
}
