package com.example.multinotepad;

import android.util.Log;
import android.widget.TextView;

import java.io.Serializable;

public class Note implements Serializable {
    public String noteTitle;
    public String noteText;
    public String latestSavedDate;
    private static final String TAG = "Note";
    public Note(String noteTitle, String noteText, String latestSavedDate){
       // Log.d(TAG, "Note: title " + noteTitle);
       // Log.d(TAG, "Note: Text" + noteText);
        this.noteTitle = noteTitle;
        this.noteText = noteText;
        this.latestSavedDate = latestSavedDate;
        Log.d(TAG, "Note: title " + noteTitle);
        Log.d(TAG, "Note: text " + noteText);
    }

    public String getNoteText() {
        return this.noteText;
    }
    public String getNoteTitle(){
        return this.noteTitle;
    }
    public String getLatestSavedDate(){
        return this.latestSavedDate;
    }
    public void setNoteTitle(String newtitle){
        this.noteTitle = newtitle;
    }

    public void setNoteText(String newText) {
        this.noteText = newText;
    }

    public void setLatestSavedDate(String newlatestSavedDate) {
        this.latestSavedDate = newlatestSavedDate;
    }
    @Override
    public String toString() {
        return "Title : " + this.noteTitle + " \n" + "Date :" + this.latestSavedDate + "\n" + "Text :" +this.noteText ;
    }
}

