package com.example.lopuk.registr;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class NewNoteActivity extends AppCompatActivity {

    TextView titleText;
    TextView noteText;
    Button sendNote_btn;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note);

        sendNote_btn = (Button) findViewById(R.id.sendNote_btn);
        titleText = (TextView) findViewById(R.id.titleText);
        noteText = (TextView) findViewById(R.id.noteText);

        sendNote_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User currentUser = new User(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()));
                ref = database.getReference("Users").child(currentUser.userId).child("notes");
                DatabaseReference noteRef = ref.push();
                Note note = new Note(titleText.getText().toString(), noteText.getText().toString(), noteRef.getKey());
                noteRef.setValue(note.toMap());
            }
        });
    }


}
