package com.crossnote.crossnote;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder>{

    private List<NoteModel> list;

    public NoteAdapter(List<NoteModel> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NoteViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final NoteViewHolder holder, final int position) {
        NoteModel note = list.get(position);

        holder.textTitle.setText(note.title);
        holder.textNote.setText(note.textOfNote);

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ViewNote.class);
                intent.putExtra("title", list.get(position).title);
                intent.putExtra("text", list.get(position).textOfNote);
                intent.putExtra("kay", list.get(position).key);
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class NoteViewHolder extends RecyclerView.ViewHolder{

        TextView textTitle, textNote;
        LinearLayout parentLayout;

        public NoteViewHolder(View itemView){
            super(itemView);
            textTitle = itemView.findViewById(R.id.text_title);
            textNote = itemView.findViewById(R.id.text_note);
            parentLayout = itemView.findViewById(R.id.parent_layout);
            }
    }
}
