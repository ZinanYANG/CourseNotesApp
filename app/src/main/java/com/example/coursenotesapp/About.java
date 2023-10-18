package com.example.coursenotesapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class About extends AppCompatActivity {


    private TextView AppName;
    private String appName;
    private TextView DateName;
    private String dateName;
    private TextView VersionNum;
    private String versionNum;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        AppName = findViewById(R.id.AppName);
        DateName = findViewById(R.id.DateName);
        VersionNum = findViewById(R.id.VersionNum);

        appName = "Course Notes App";
        dateName = "@2023, Alan Young";
        versionNum = "Version 1.0";

        AppName.setText(appName);
        DateName.setText(dateName);
        VersionNum.setText(versionNum);



    }
}