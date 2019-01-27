package com.crossnote.crossnote;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class NewNoteActivity extends AppCompatActivity {

    EditText titleText, noteText;
    private String title, text;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle("New note");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note);
        titleText = findViewById(R.id.titleText);
        noteText = findViewById(R.id.noteText);
    }

    @Override
    public void onBackPressed(){
        if (checkNote()){
            UserModel currentUser = new UserModel(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()));
            ref = database.getReference("Users").child(currentUser.userId).child("notes");
            DatabaseReference noteRef = ref.push();
            NoteModel note = new NoteModel(title, text, noteRef.getKey());
            noteRef.setValue(note.toMap());
        }
        startActivity(new Intent(NewNoteActivity.this, NoteActivity.class));
    }

    private boolean checkNote(){
        boolean result = false;
        if (!(titleText.getText().toString().trim().equals("")&(noteText.getText().toString().trim().equals("")))){
            title = titleText.getText().toString();
            text = noteText.getText().toString();
            result = true;
        }
        return result;
    }
}
