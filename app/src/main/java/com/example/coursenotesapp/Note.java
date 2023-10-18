package com.example.coursenotesapp;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class Note implements Serializable {
    String courseId;
    String title;
    String noteText;
    String lastUpdateTime;

    public Note(String courseId, String title, String noteText, String lastUpdateTime) {
        this.courseId = courseId;
        this.title = title;
        this.noteText = noteText;
        this.lastUpdateTime = lastUpdateTime;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNoteText() {
        return noteText;
    }

    public void setNoteText(String noteText) {
        this.noteText = noteText;
    }

    public String getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(String lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    @Override
    public String toString() {
        return "Note{" +
                "courseId='" + courseId + '\'' +
                ", title='" + title + '\'' +
                ", noteText='" + noteText + '\'' +
                ", lastUpdateTime='" + lastUpdateTime + '\'' +
                '}';
    }

    public String toJSON(){
        try{
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("courseId", getCourseId());
            jsonObject.put("title", getTitle());
            jsonObject.put("noteText", getNoteText());
            jsonObject.put("lastUpdateTime", getLastUpdateTime());
            return jsonObject.toString();
        } catch (JSONException e){
            e.printStackTrace();
        }
        return null;
    }
}
