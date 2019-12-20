package com.example.multinotepad;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.JsonWriter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class MainActivity  extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener{
    private static final int EDIT_REQ_CODE = 1; //used to check the result returned by AsyncTask
    private RecyclerView recyclerView;
    private MyAdapter mAdapter;
    private int latestPosition = 0;
    private List<Note> notesList = new ArrayList<>();
    private static final String TAG = "MainActivity";
    private static final int CREATE_REQUEST_CODE = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerNotes);
        mAdapter = new MyAdapter(notesList, this);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        MyAsyncTask.running = true;
        new MyAsyncTask(this).execute(); //start the Async Task

    }

    public void onClick(View v){
        int position = recyclerView.getChildLayoutPosition(v);
        Note m = notesList.get(position);
        latestPosition = position;
        modifyExistingNote(m);
    }

    public void modifyExistingNote(Note m){
        Intent intent = new Intent(MainActivity.this, EditActivity.class);
        intent.putExtra(Intent.EXTRA_TEXT, "Edit note");
        intent.putExtra("Title", m.getNoteTitle());
        intent.putExtra("Text", m.getNoteText());
        startActivityForResult(intent, EDIT_REQ_CODE);
    }


    @Override
    public boolean onLongClick(View view) {
        int position = recyclerView.getChildAdapterPosition(view);
        Note a = notesList.get(position);
        latestPosition = position;
        final AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setCancelable(false)
                .setTitle("Confirmation")
                .setMessage("Are you sure you want to delete the note " + a.getNoteTitle()+" ?")
                .setPositiveButton("Yes",dialogClickListener)
                .setNegativeButton("No",dialogClickListener)
                .show();
        
        return false;
    }

    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    notesList.remove(latestPosition);
                    mAdapter.notifyDataSetChanged();
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    Log.d(TAG, " Button : NO ");
                    break;
            }
        }
    };
    @Override
    public boolean onCreateOptionsMenu (Menu menu){
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected (MenuItem item){
        switch (item.getItemId()) {
            case R.id.item:
                Log.d(TAG, "onOptionsItemSelected : " + item.getTitle());
                initiateEditActivity();
                break;
            case R.id.info:
                Log.d(TAG, "onOptionsItemSelected : " + item.getTitle());
                initiateAboutActivity();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initiateEditActivity() {
        Intent intent = new Intent(MainActivity.this, EditActivity.class);
        startActivityForResult(intent, CREATE_REQUEST_CODE);

    }

    private void initiateAboutActivity() {
        Intent intent = new Intent(MainActivity.this, AboutActivity.class);
        startActivity(intent);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if (requestCode == CREATE_REQUEST_CODE){
            if (resultCode == RESULT_OK){
                String noteText = data.getStringExtra("NEW_TEXT");
                String noteTitle = data.getStringExtra("NEW_TITLE");
                String noteDate = data.getStringExtra("NEW_DATE");
                if(!noteTitle.equals("")){
                    notesList.add(0, new Note(noteTitle,noteText,noteDate));
                    Log.d(TAG, "onActivityResult: noteslist " + notesList);
                }
                mAdapter.notifyDataSetChanged();
                Log.d(TAG, "onActivityResult: New Note Added Create");
            }else {
                Toast.makeText(this, "Untitled notes are not saved!", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onActivityResult: result Code: " + resultCode);
            }

        }
        if (requestCode == EDIT_REQ_CODE){
            if(resultCode == RESULT_OK){
                String text = data.getStringExtra("NEW_TEXT");
                String title = data.getStringExtra("NEW_TITLE");
                String date = data.getStringExtra("NEW_DATE");
                notesList.remove(latestPosition);
                notesList.add(0,new Note(title,text,date));
                mAdapter.notifyDataSetChanged();
            }else {
                Log.d(TAG, "onActivityResult: Result code" + resultCode);
            }
        }
    }
    @Override
    protected void onPause(){
        saveNote();
        super.onPause();
    }
    @Override
    protected void onResume(){
        super.onResume();
        mAdapter.notifyDataSetChanged();
    }
    @Override
    protected void onStart() {
        Log.d(TAG, "onStart");
        super.onStart();
    }
    @Override
    protected void onStop(){
        super.onStop();
    }
    private void saveNote(){
        try {
            FileOutputStream fos = getApplicationContext().openFileOutput("notes.json", Context.MODE_PRIVATE);
            JsonWriter writer = new JsonWriter(new OutputStreamWriter(fos, "UTF-8"));
            writer.setIndent("  ");
            writer.beginObject();
            writer.name("number notes").value(notesList.size());
            int pos = 1;
            for (int j = 1; j <= notesList.size(); j++){
                if (notesList.get(j-1).getNoteTitle().equals("")){
                    Log.d(TAG, "saveNotes: Element " + j + " doesn't have a title");
                }
                else{
                    writer.name("title " + pos).value(notesList.get(j-1).getNoteTitle());
                    writer.name("date " + pos).value(notesList.get(j-1).getLatestSavedDate());
                    writer.name("text " + pos).value(notesList.get(j-1).getNoteText());
                    pos ++;
                }
            }
            writer.endObject();
            writer.close();

        } catch (Exception e) {
            e.getStackTrace();
        }
    }
    public void whenAsyncIsDone(List<Note> nList) {

        Log.d(TAG, "whenAsyncIsDone Start");
        this.notesList.clear();
        this.notesList.addAll(nList);
        mAdapter.notifyDataSetChanged();
        Log.d(TAG, "whenAsyncIsDone ");

    }

}
