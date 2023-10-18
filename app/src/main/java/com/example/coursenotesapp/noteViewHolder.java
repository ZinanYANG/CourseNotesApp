package com.example.coursenotesapp;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
public class noteViewHolder extends RecyclerView.ViewHolder{
    TextView courseId;
    TextView courseTitle;
    TextView noteText;
    TextView lastUpdateTime;

    public noteViewHolder(@NonNull View itemView) {
        super(itemView);
        courseId = itemView.findViewById(R.id.courseId);
        courseTitle = itemView.findViewById(R.id.courseTitle);
        noteText = itemView.findViewById(R.id.noteText);
        lastUpdateTime = itemView.findViewById(R.id.lastUpdateTime);
    }
}
