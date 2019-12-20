package com.example.multinotepad;

import android.content.Intent;
import android.os.AsyncTask;
import android.util.JsonReader;
import android.util.Log;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;


public class MyAsyncTask extends AsyncTask<Integer, Integer, List<Note>> {
    public static boolean running = false;
    private MainActivity mActivity;

    public MyAsyncTask(MainActivity m1) {
        mActivity = m1;
    }

    private static final String TAG = "MyAsyncTask";

    @Override

    protected List<Note> doInBackground(Integer... para) {
        Log.d(TAG, "doInBackground: Starting background execution");
        List<Note> nList = new ArrayList<>();
        try {
            InputStream str;
            str = mActivity.getApplicationContext().openFileInput("notes.json");
            Log.d(TAG, "doInBackground: " + str);
            JsonReader reader;
            reader = new JsonReader(new InputStreamReader(str, "UTF-8"));
            reader.beginObject();
            boolean listInitialized = false;

            while (reader.hasNext()) {
                String name = reader.nextName();
                if (name.equals("number notes")) {
                    int val = Integer.parseInt(reader.nextString());
                    for (int x = 1; x <= val; x++) {
                        nList.add(new Note("Title " + x, "Text " + x, "Date " + x));
                        Log.d(TAG, "doInBackground: " + x);
                        Log.d(TAG, "doInBackground: " + nList);
                    }
                    listInitialized = true;
                } else if (name.substring(0, 4).equals("date") && listInitialized) {
                    nList.get(Integer.parseInt(name.substring(5)) - 1).setLatestSavedDate(reader.nextString());
                } else if (name.substring(0, 4).equals("text") && listInitialized) {
                    nList.get(Integer.parseInt(name.substring(5)) - 1).setNoteText(reader.nextString());
                } else if (name.substring(0, 5).equals("title") && listInitialized) {
                    nList.get(Integer.parseInt(name.substring(6)) - 1).setNoteTitle(reader.nextString());
                } else
                    reader.skipValue();
            }
            reader.endObject();
            Log.d(TAG, "loadNotes: Completed");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
//        Log.d(TAG, "doInBackground: " + nList);
        return nList;
    }


    @Override
    protected void onPostExecute(List<Note> myList) {

        super.onPostExecute(myList);
        mActivity.whenAsyncIsDone(myList);

        running = false;
    }
}
