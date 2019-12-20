package com.example.multinotepad;

import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {
    private static final String TAG = "MyAdapter";
    private List<Note> nList;
    private MainActivity mact;

    public MyAdapter(List<Note> emptylist , MainActivity m1){
        Log.d(TAG, "MyAdapter");
        this.nList = emptylist;
        mact = m1;
    }
    @Override
    public MyViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.note_list, parent, false);

        itemView.setOnClickListener(mact);
        itemView.setOnLongClickListener((View.OnLongClickListener) mact);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Note myNote = nList.get(position);
        holder.noteTitle.setText(myNote.getNoteTitle());
        if (myNote.getNoteText().length() < 80 ){ //checks if the title length is less than 80 characters
            holder.noteText.setText(myNote.getNoteText());
        }
        else {
            holder.noteText.setText(myNote.getNoteText().substring(0,80) + " ...");
        }
        holder.latestSavedDate.setText(myNote.getLatestSavedDate());
    }

    @Override
    public int getItemCount() {
        mact.getSupportActionBar().setTitle("Multi Notes "+ "("+nList.size()+")");
        return nList.size();
    }
}
