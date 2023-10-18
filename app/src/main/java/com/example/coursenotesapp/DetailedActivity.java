package com.example.coursenotesapp;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Date;

public class DetailedActivity extends AppCompatActivity {

    private EditText CourseIdSave, TitleSave, CourseNoteSave;
    private Note note;
    private ActivityResultLauncher<Intent> activityResultLauncher;
    private String originalId, originalTitle, originalNoteText;
    private Note originalNote;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed);

        Toolbar toolbarTest = findViewById(R.id.toolbarTest);
        toolbarTest.setTitle("CourseNotesApp");
        toolbarTest.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbarTest);

        // Initialize EditText fields
        CourseIdSave = findViewById(R.id.CourseIdSave);
        TitleSave = findViewById(R.id.TitleSave);
        CourseNoteSave = findViewById(R.id.CourseNoteSave);

        // Load data from Intent if editing an existing note
        Intent intent = getIntent();

        if (intent.hasExtra("NOTE")) {
            originalNote = (Note) intent.getSerializableExtra("NOTE");
            CourseIdSave.setText(originalNote.getCourseId());
            TitleSave.setText(originalNote.getTitle());
            CourseNoteSave.setText(originalNote.getNoteText());

            // Store original note data to check for modifications later
            originalId = originalNote.getCourseId();
            originalTitle = originalNote.getTitle();
            originalNoteText = originalNote.getNoteText();

        } else {
            Toast.makeText(this, "ADDING NOTES", Toast.LENGTH_SHORT).show();
            // Initialize original data as null since it's a new note
            originalId = null;
            originalTitle = null;
            originalNoteText = null;
        }
    }

    public void doSave(View v){

        String id = CourseIdSave.getText().toString();
        String title = TitleSave.getText().toString();
        String CourseNote = CourseNoteSave.getText().toString();

        if(title.trim().isEmpty()) {
            // Show an alert dialog
            new AlertDialog.Builder(this)
                    .setIcon(R.drawable.icon)
                    .setTitle("Title is Missing")
                    .setMessage("Note cannot be saved without a title.")
                    .setPositiveButton("OK", (dialog, which) -> finish())
                    .setNegativeButton("Cancel", null)
                    .show();
            return;
        }

        if(id.trim().isEmpty()  || CourseNote.trim().isEmpty()) {
            Toast.makeText(this, "Cannot save note without course id and title", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Check for changes in case of editing an existing note
        if(originalId != null && originalTitle != null && originalNoteText != null) {
            if(id.equals(originalId) && title.equals(originalTitle) && CourseNote.equals(originalNoteText)) {
                Toast.makeText(this, "No changes made", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }
        }
        Note note = new Note(id, title, CourseNote, new Date().toString());
        Intent data = new Intent(); // Used to hold results data to be returned to original activity
        data.putExtra("NOTE", note);

        setResult(RESULT_OK, data);
        finish(); // This closes the current activity, returning us to the original activity
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menutest,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId()==R.id.save){
            doSave(null);
            Toast.makeText(this, "save ", Toast.LENGTH_SHORT).show();


        } else {
            return super.onOptionsItemSelected(item);
        }

        return true;
    }

    @Override
    public void onBackPressed() {

        String id = CourseIdSave.getText().toString();
        String title = TitleSave.getText().toString();
        String courseNote = CourseNoteSave.getText().toString();

        // Case 1: All fields are empty
        if (id.isEmpty() && title.isEmpty() && courseNote.isEmpty()) {
            Toast.makeText(this, "Empty note not saved", Toast.LENGTH_SHORT).show();
            setResult(RESULT_CANCELED);
            finish();
            return; // ensure we exit the method here
        }
        // Case 2: No changes have been made
        Intent intent = getIntent();
        if (intent.hasExtra("NOTE")) {
            Note originalNote = (Note) intent.getSerializableExtra("NOTE");
            if (originalNote.getCourseId().equals(id) &&
                    originalNote.getTitle().equals(title) &&
                    originalNote.getNoteText().equals(courseNote)) {

                Toast.makeText(this, "No changes made", Toast.LENGTH_SHORT).show();
                setResult(RESULT_CANCELED);
                finish();
                return;
            }
        }
//      case 3 : title or id is empty
        if (title.trim().isEmpty()|| id.trim().isEmpty()) {
            Toast.makeText(this, "Cannot save note without course id and title", Toast.LENGTH_SHORT).show();

            new AlertDialog.Builder(this)
                    .setIcon(R.drawable.icon)
                    .setTitle("Title is Missing")
                    .setMessage("Note cannot be saved without a title.")
                    .setPositiveButton("OK", (dialog, which) -> finish())
                    .setNegativeButton("Cancel", null)
                    .show();
            return;
        }

//      Changes Detected
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Save Changes?");
        builder.setMessage("Do you wanna save the changes made to the note?");
        //        set the icon
        builder.setIcon(R.drawable.icon);

        builder.setCancelable(false);

        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
//              save extra inputs
                Note note = new Note(id, title, courseNote, new Date().toString());
                Intent data = new Intent();
                data.putExtra("NOTE", note);
                setResult(RESULT_OK, data);
                finish();
            }
        });

        builder.setNegativeButton("Discard", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

    }

}