package com.example.lopuk.registr;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder>{

    private List<Note> list;

    public NoteAdapter(List<Note> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NoteViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final NoteViewHolder holder, int position) {
        Note note = list.get(position);

        holder.textTitle.setText(note.title);
        holder.textNote.setText(note.textOfNote);


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class NoteViewHolder extends RecyclerView.ViewHolder{

        TextView textTitle, textNote;

        public NoteViewHolder(View itemView){
            super(itemView);

            textTitle = itemView.findViewById(R.id.text_title);
            textNote = itemView.findViewById(R.id.text_note);
        }
    }
}
