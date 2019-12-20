package com.example.multinotepad;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class EditActivity extends AppCompatActivity {
    private static final String TAG = "Edit Activity";
    private static final int RESULT_NO_MODIFICATION = 5 ;
    private EditText noteTitle;
    private EditText noteText;
    private String text = "";
    private String title = "";
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_activity);

        noteText = (EditText) findViewById(R.id.noteText);
        noteTitle = (EditText) findViewById(R.id.noteTitle);
        noteText.setMovementMethod(new ScrollingMovementMethod());
        Intent intent = getIntent();
        if (intent.hasExtra(Intent.EXTRA_TEXT)){
            title = intent.getStringExtra("Title");
            noteTitle.setText(title);
            text = intent.getStringExtra("Text");
            noteText.setText(text);
        }else {
            Log.d(TAG, "onCreate: empty text ");
            noteTitle.setText("");
            noteText.setText("");
        }
        Log.d(TAG,"edit note");
    }

    public void saveNote(){
        if (noteTitle.getText().toString().equals("")){
            setResult(RESULT_CANCELED);
            Log.d(TAG, "saveNote: not saved");
        } else {
            if(noteTitle.getText().toString().equals(title) && noteText.getText().toString().equals(text) && !noteText.getText().toString().equals("")){
                Log.d(TAG, "saveNote: not saved 2");
                setResult(RESULT_NO_MODIFICATION);
            } else {
                Intent noteData = new Intent();
                noteData.putExtra("NEW_TEXT", noteText.getText().toString());
                noteData.putExtra("NEW_TITLE", noteTitle.getText().toString());
                noteData.putExtra("NEW_DATE", "" + new SimpleDateFormat("dd MMM yyyy - HH:mm").format(Calendar.getInstance().getTime()));
                setResult(RESULT_OK, noteData);
                Log.d(TAG, "saveNote: saved");
                Log.d(TAG, "saveNote: title" + noteTitle.getText().toString());
                Log.d(TAG, "saveNote:"+ noteText.getText().toString());
            }
        }
        finish();
    }
    @Override
    public boolean onCreateOptionsMenu (Menu menu){
        getMenuInflater().inflate(R.menu.menu_edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item){
        switch (item.getItemId()) {
            case R.id.save_note:
                Log.d(TAG, "onOptionsItemSelected : " + item.getTitle());
                saveNote();
                break;
            default:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed(){
        Log.d(TAG, "onBackPressed: back pressed");
        if (!noteText.getText().toString().equals(text) || !noteTitle.getText().toString().equals(title)){
            Log.d(TAG, "onBackPressed: hdkhf");
            final AlertDialog alertDialog = new AlertDialog.Builder(this)
                    .setCancelable(false)
                    .setTitle("Confirmation")
                    .setMessage("Your note is not yet saved ! \n Would you like to save note \" "+noteTitle.getText()+"\" ?")
                    .setPositiveButton("Save",dialogClickListener)
                    .setNegativeButton("Don't Save",dialogClickListener)
                    .show();
        } else {
            super.onBackPressed();
            Log.d(TAG, "onBackPressed: nothing happeneed");
        }
    }
    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    Log.d(TAG, " Button : YES ");
                    saveNote();
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    Log.d(TAG, " Button : NO ");
                    finish();
                    break;
            }
        }
    };
}
