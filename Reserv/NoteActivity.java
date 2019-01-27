package com.example.lopuk.registr;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View;

import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class NoteActivity extends AppCompatActivity {

    FloatingActionButton btn_addNote;
    RecyclerView recyclerView;
    List<Note> result;
    NoteAdapter adapter;
    FirebaseDatabase database;
    DatabaseReference reference;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        result = new ArrayList<>();
        database = FirebaseDatabase.getInstance();
        User currentUser = new User(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()));
        reference = database.getReference("Users").child(currentUser.userId).child("notes");

        btn_addNote = (FloatingActionButton) findViewById(R.id.btn_addNote);
        recyclerView = (RecyclerView) findViewById(R.id.note_list);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(llm);


        btn_addNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NoteActivity.this, NewNoteActivity.class));


            }
        });



        addEmailRecord();

        adapter = new NoteAdapter(result);
        recyclerView.setAdapter(adapter);


        updateList();
        enableSwipeToDeleteAndUndo();
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case 0:
                break;

            case 1:
                break;
        }

        return super.onContextItemSelected(item);

    }

    private void addEmailRecord(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref;
        User currentUser = new User(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()));
        ref = database.getReference("Users").child(currentUser.userId);
        DatabaseReference emailRef = ref.child("email");
        emailRef.setValue(currentUser.email);
    }

    private void updateList(){
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                result.add(dataSnapshot.getValue(Note.class)); //Магия
                adapter.notifyDataSetChanged(); //Обновления с анимацие
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Note model = dataSnapshot.getValue(Note.class);

                int index = getItemIndex(model);

                result.set(index, model);
                adapter.notifyItemChanged(index);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                Note note = dataSnapshot.getValue(Note.class);

                int index = getItemIndex(note);

                result.remove(index);
                adapter.notifyItemRemoved(index);
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public int getItemIndex(Note note){
        int index = -1;
        for(int i = 0; i < result.size(); i++){
            if (result.get(i).key.equals(note.key)) {
                index = i;
                break;
            }
        }
        return index;
    }

    private void enableSwipeToDeleteAndUndo() {
        SwipeToDeleteCallback swipeToDeleteCallback = new SwipeToDeleteCallback(this) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

                final Note restoreNote = setRestoreNote((viewHolder.getAdapterPosition()));

                removeUser(viewHolder.getAdapterPosition());

                Snackbar snackbar = Snackbar
                        .make(findViewById(R.id.coordinatorLayout), "It was removed.", Snackbar.LENGTH_LONG);
                snackbar.setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DatabaseReference ref;
                        User currentUser = new User(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()));
                        ref = database.getReference("Users").child(currentUser.userId).child("notes");
                        DatabaseReference noteRef = ref.push();
                        Note note = new Note(restoreNote.title, restoreNote.textOfNote, noteRef.getKey());
                        noteRef.setValue(note.toMap());
                    }
                });
//
                snackbar.setActionTextColor(Color.YELLOW);
                snackbar.show();



            }
        };

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchhelper.attachToRecyclerView(recyclerView);
    }

    private void removeUser(int position){
        reference.child(result.get(position).key).removeValue();
    }

    private Note setRestoreNote(int position){
        String restoreTitle = String.valueOf((result.get(position).title));
        String restoreTextOfNote = String.valueOf((result.get(position).textOfNote));
        String restoreKey = String.valueOf((result.get(position).textOfNote));
        Note restoreNote = new Note(restoreTitle, restoreTextOfNote, restoreKey);
        return restoreNote;
    }


}

