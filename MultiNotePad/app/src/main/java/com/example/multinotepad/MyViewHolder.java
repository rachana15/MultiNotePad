package com.example.multinotepad;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;


class MyViewHolder extends RecyclerView.ViewHolder {
    public TextView noteTitle;
    public TextView noteText;
    public TextView latestSavedDate;

    public MyViewHolder(View view){
        super(view);
        noteTitle = (TextView) view.findViewById(R.id.noteTitle);
        noteText = (TextView) view.findViewById(R.id.noteText);
        latestSavedDate = (TextView) view.findViewById(R.id.latestSavedDate);

    }
}
