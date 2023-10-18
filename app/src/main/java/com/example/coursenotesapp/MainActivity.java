package com.example.coursenotesapp;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements View.OnClickListener,View.OnLongClickListener{
    private static final String TAG = "MainActivity";
    private final ArrayList<Note> notesList = new ArrayList<>();
    private RecyclerView recyclerView;
    private NoteAdapter noteAdapter;
    private ActivityResultLauncher<Intent> activityResultLauncherEdit;
    private ActivityResultLauncher<Intent> activityResultLauncherAdd;
    private int selectedPosition;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activityResultLauncherEdit = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                this::handleEditResult);
        activityResultLauncherAdd = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                this::handleAddResult);
        Toolbar myToolbar = findViewById(R.id.toolbar);
        myToolbar.setTitle("CourseNotesApp");
        myToolbar.setTitleTextColor(Color.WHITE);


        setSupportActionBar(myToolbar);
        recyclerView = findViewById(R.id.notesRecycler);
        noteAdapter = new NoteAdapter(notesList, this);
        recyclerView.setAdapter(noteAdapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        myToolbar.setSubtitle("Note: "+notesList.size());
        myToolbar.setSubtitleTextColor(Color.WHITE);
        loadNotes();
        updateSubtitle();


    }

    private void updateSubtitle() {
        Toolbar myToolbar = findViewById(R.id.toolbar);
        myToolbar.setSubtitle("Notes: " + notesList.size());
    }


    //    save notes to json file
    private void saveNotes(){
        JSONArray jsonArray = new JSONArray();
        for (Note note : notesList) {
            jsonArray.put(note.toJSON());
        }
        String jsonString = jsonArray.toString();
        try (FileOutputStream fos = getApplicationContext()
                .openFileOutput("notes.json", Context.MODE_PRIVATE)){
            fos.write(jsonString.getBytes());
        } catch(IOException e){
            e.printStackTrace();
        }
    }

//  load notes from json file
    private void loadNotes() {
        try (FileInputStream fis = getApplicationContext().openFileInput("notes.json")){
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader reader = new BufferedReader(isr);
            StringBuilder builder = new StringBuilder();

            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
            JSONArray jsonArray = new JSONArray(builder.toString());
            for (int i = 0; i < jsonArray.length(); i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Note note = new Note(
                        jsonObject.getString("courseId"),
                        jsonObject.getString("title"),
                        jsonObject.getString("noteText"),
                        jsonObject.getString("lastUpdateTime")
                );
                notesList.add(note);
                updateSubtitle();

            }
        } catch (IOException | JSONException e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveNotes();
        updateSubtitle();
    }



    public void handleEditResult(ActivityResult result){
        if (result == null || result.getData() == null) {
            return;
        }
        Intent data = result.getData();
        if (result.getResultCode() == RESULT_OK){
            if (data.hasExtra("NOTE")){
                Note note = (Note) data.getSerializableExtra("NOTE");
                notesList.remove(selectedPosition);
                notesList.add(selectedPosition, note);
                noteAdapter.notifyItemChanged(selectedPosition);
                saveNotes();
                updateSubtitle();
            }
        }


    }
    public void handleAddResult(ActivityResult result){
        if (result == null || result.getData() == null) {
            return;
        }

        Intent data = result.getData();
        if (result.getResultCode() == RESULT_OK) {
            if (data.hasExtra("NOTE")) {
                String noteJson = data.getStringExtra("NOTE");

                Note note = (Note) data.getSerializableExtra("NOTE");
                notesList.add(note);
                updateSubtitle();
                noteAdapter.notifyItemInserted(notesList.size());
                saveNotes();
                updateSubtitle();
            }

        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu2,menu);
        return true;
    }

    @Override
    public void onClick(View view) {
        int selectedPosition = recyclerView.getChildLayoutPosition(view);
        Note note = notesList.get(selectedPosition);
        Toast.makeText(this,"selected" + note.getCourseId(), Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, DetailedActivity.class);
        intent.putExtra("NOTE", note);

        activityResultLauncherEdit.launch(intent);

    }

    @Override
    public boolean onLongClick(View view) {

        int pos = recyclerView.getChildLayoutPosition(view);
        Note noteToDelete = notesList.get(pos);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Note");
        builder.setMessage("Delete Note '" + noteToDelete.getCourseId() + ": " + noteToDelete.getTitle() + "'?");
        //        set the icon
        builder.setIcon(R.drawable.icon);

        builder.setCancelable(false);

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                notesList.remove(pos);
                noteAdapter.notifyItemRemoved(pos);
                saveNotes();
                updateSubtitle();
                Toast.makeText(MainActivity.this, "Note Deleted", Toast.LENGTH_SHORT).show();


            }
        });

        builder.setNegativeButton("NO", null);

        AlertDialog dialog = builder.create();
        dialog.show();
        return true;
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "The back button was pressed - Bye!", Toast.LENGTH_SHORT).show();
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId()==R.id.infoOptions){

            Toast.makeText(this, "info options selected ", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(this, About.class);
            startActivity(intent);

        } else if (item.getItemId()==R.id.addOptions){
            Intent intent = new Intent(this, DetailedActivity.class);
            activityResultLauncherAdd.launch(intent);
        } else {
            return super.onOptionsItemSelected(item);
        }
        return true;
    }
}