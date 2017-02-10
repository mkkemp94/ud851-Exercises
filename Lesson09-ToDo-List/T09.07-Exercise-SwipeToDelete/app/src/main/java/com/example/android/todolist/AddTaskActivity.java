/*
* Copyright (C) 2016 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*      http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package com.example.android.todolist;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.android.todolist.data.TaskContract;


public class AddTaskActivity extends AppCompatActivity {

    // Declare a member variable to keep track of a task's selected mPriority
    private int mPriority;
    private boolean mExistingTask = false;
    private long mExistingTaskId = -2;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        Intent intent = getIntent();

        mExistingTaskId = intent.getLongExtra("id", -2);
        String description = intent.getStringExtra("description");
        int priority = intent.getIntExtra("priority", 0);

        Log.v("On click", "In AddTask, description: " + description);
        Log.v("On click", "In AddTask, priority: " + priority);

        if (mExistingTaskId != -2) {
            ((EditText) findViewById(R.id.editTextTaskDescription)).setText(description);
            mPriority = priority;
            //mExistingTask = true;
        } else {
            mPriority = 1;
        }

        // Initialize to highest mPriority by default (mPriority = 1)
        if (mPriority == 1) {
            ((RadioButton) findViewById(R.id.radButton1)).setChecked(true);
        } else if (mPriority == 2) {
            ((RadioButton) findViewById(R.id.radButton2)).setChecked(true);
        } else if (mPriority == 3) {
            ((RadioButton) findViewById(R.id.radButton3)).setChecked(true);
        }

    }


    /**
     * onClickAddTask is called when the "ADD" button is clicked.
     * It retrieves user input and inserts that new task data into the underlying database.
     */
    public void onClickAddTask(View view) {
        // Not yet implemented
        // Check if EditText is empty, if not retrieve input and store it in a ContentValues object
        // If the EditText input is empty -> don't create an entry
        String input = ((EditText) findViewById(R.id.editTextTaskDescription)).getText().toString();
        if (input.length() == 0) {
            return;
        }

        // Insert new task data via a ContentResolver
        // Create new empty ContentValues object
        ContentValues contentValues = new ContentValues();
        // Put the task description and selected mPriority into the ContentValues
        contentValues.put(TaskContract.TaskEntry.COLUMN_DESCRIPTION, input);
        contentValues.put(TaskContract.TaskEntry.COLUMN_PRIORITY, mPriority);



        if (mExistingTaskId != -2) {

            Uri updateUri = ContentUris.withAppendedId(TaskContract.TaskEntry.CONTENT_URI, mExistingTaskId);
            
            int rowsUpdated = getContentResolver().update(updateUri, contentValues, null, null);
            if (rowsUpdated >= 0) {
                Toast.makeText(this, rowsUpdated + " rows updated!", Toast.LENGTH_LONG).show();
            }

        } else {
            // Insert the content values via a ContentResolver
            Uri uri = getContentResolver().insert(TaskContract.TaskEntry.CONTENT_URI, contentValues);

            // Display the URI that's returned with a Toast
            // [Hint] Don't forget to call finish() to return to MainActivity after this insert is complete
            if(uri != null) {
                Toast.makeText(getBaseContext(), uri.toString(), Toast.LENGTH_LONG).show();
            }
        }



        // Finish activity (this returns back to MainActivity)
        finish();

    }


    /**
     * onPrioritySelected is called whenever a priority button is clicked.
     * It changes the value of mPriority based on the selected button.
     */
    public void onPrioritySelected(View view) {
        if (((RadioButton) findViewById(R.id.radButton1)).isChecked()) {
            mPriority = 1;
        } else if (((RadioButton) findViewById(R.id.radButton2)).isChecked()) {
            mPriority = 2;
        } else if (((RadioButton) findViewById(R.id.radButton3)).isChecked()) {
            mPriority = 3;
        }
    }
}
