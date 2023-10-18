package com.example.coursenotesapp;

import static android.content.ContentValues.TAG;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Date;

public class NoteAdapter extends RecyclerView.Adapter<noteViewHolder>{
    private MainActivity mainActivity;
    private ArrayList<Note> notesList;
    public NoteAdapter(ArrayList<Note> notesList, MainActivity mainActivity){
        this.notesList = notesList;
        this.mainActivity = mainActivity;
    }
    @NonNull
    @Override
    public noteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: MAKING NEW noteViewHolder");
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tran_layout_entry, parent, false);
        itemView.setOnClickListener(mainActivity);
        itemView.setOnLongClickListener(mainActivity);

        return new noteViewHolder(itemView);
    }

    public static String trimTo80Chars(String str) {
        if (str.length() > 80) {
            return str.substring(0, 80) + "...";
        } else {
            return str;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull noteViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: filling view holder noteViewHolder"+position);
        Note note = notesList.get(position);
        String trimmedTitle = trimTo80Chars(note.getTitle());
        String trimmedNoteText = trimTo80Chars(note.getNoteText());
        holder.courseId.setText(note.getCourseId());
        holder.lastUpdateTime.setText(new Date().toString());

        holder.courseTitle.setText(trimmedTitle);
        holder.noteText.setText(trimmedNoteText);
    }

    @Override
    public int getItemCount() {
        return notesList.size();
    }
}



