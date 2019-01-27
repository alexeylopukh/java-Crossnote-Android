package com.crossnote.crossnote;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class ViewNote extends AppCompatActivity {

    EditText titleText, noteText;
    String title, text, key;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref;

    NoteModel note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note);
        setTitle("View Note");
        titleText = findViewById(R.id.titleText);
        noteText = findViewById(R.id.noteText);
        NoteModel note = new NoteModel(
                getIntent().getStringExtra("title"),
                getIntent().getStringExtra("text"),
                getIntent().getStringExtra("kay")
                );
        titleText.setText(note.title);
        noteText.setText(note.textOfNote);
        key = note.key;
    }

    @Override
    public void onBackPressed(){
//        UserModel currentUser = new UserModel(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()));
//        ref = database.getReference("Users").child(currentUser.userId).child("notes");
//        DatabaseReference noteRef = ref.child(key);
//        toSeparate(newNoteText.getText().toString());
//        if (title == null & text == null){startActivity(new Intent(ViewNote.this, NoteActivity.class));}
//        NoteModel newNote = new NoteModel(title, text, key);
//        noteRef.setValue(newNote.toMap());

        if (checkNote()){
            UserModel currentUser = new UserModel(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()));
            ref = database.getReference("Users").child(currentUser.userId).child("notes");
            DatabaseReference noteRef = ref.child(key);
            NoteModel newNote = new NoteModel(title, text, key);
            noteRef.setValue(newNote.toMap());
        }

        startActivity(new Intent(this, NoteActivity.class));
    }

    private boolean checkNote(){
        boolean result = false;
        if (!(titleText.getText().toString().equals("")&(noteText.getText().toString().equals("")))){
            title = titleText.getText().toString();
            text = noteText.getText().toString();
            result = true;
        }
        return result;
    }

}
