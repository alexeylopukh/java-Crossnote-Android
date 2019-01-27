package com.crossnote.crossnote;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.bottomappbar.BottomAppBar;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.ActionMenuItem;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class NoteActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    List<NoteModel> reusult;
    NoteAdapter adapter;
    FloatingActionButton newNoteBtn;
    FirebaseDatabase database;
    DatabaseReference reference;
    NavigationView nv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Notes");
        setContentView(R.layout.activity_note);
        recyclerView = findViewById(R.id.RecyclerView);
        newNoteBtn = findViewById(R.id.newNoteBtn);
        nv = findViewById(R.id.navigation_view);
        reusult = new ArrayList<>();
        adapter = new NoteAdapter(reusult);
        database = FirebaseDatabase.getInstance();
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter(adapter);
        UserModel currentUser = new UserModel(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()));
        reference = database.getReference("Users").child(currentUser.userId).child("notes");
        enableSwipeToDeleteAndUndo();
        updateList();
        addEmailRecord();

        newNoteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NoteActivity.this, NewNoteActivity.class));
            }
        });
        BottomAppBar bar = (BottomAppBar) findViewById(R.id.bar);
        setSupportActionBar(bar);
    }

    private void updateList(){
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                reusult.add(0,dataSnapshot.getValue(NoteModel.class)); //Магия
                adapter.notifyDataSetChanged(); //Обновления с анимацие

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                NoteModel model = dataSnapshot.getValue(NoteModel.class);

                int index = getItemIndex(model);

                reusult.set(index, model);
                adapter.notifyItemChanged(index);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                NoteModel note = dataSnapshot.getValue(NoteModel.class);

                int index = getItemIndex(note);

                reusult.remove(index);
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
    public int getItemIndex(NoteModel note){
        int index = -1;
        for(int i = 0; i < reusult.size(); i++){
            if (reusult.get(i).key.equals(note.key)) {
                index = i;
                break;
            }
        }
        return index;
    }

    private void enableSwipeToDeleteAndUndo() {

        Thread thread = new Thread(){
            @Override
            public void run() {
                try {
                    recyclerView.setClickable(false);
                    sleep(10000);
                    recyclerView.setClickable(true);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        thread.start();

        SwipeToDeleteCallback swipeToDeleteCallback = new SwipeToDeleteCallback(this) {
            @Override
            public synchronized void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

                final NoteModel restoreNote = setRestoreNote((viewHolder.getAdapterPosition()));

                removeUser(viewHolder.getAdapterPosition());

                CoordinatorLayout coordinatorLayout = findViewById(R.id.CL);

                Snackbar snackbar = Snackbar
                        .make(coordinatorLayout, "It was removed.", Snackbar.LENGTH_LONG);
                snackbar.setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DatabaseReference ref;
                        UserModel currentUser = new UserModel(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()));
                        ref = database.getReference("Users").child(currentUser.userId).child("notes");
                        DatabaseReference noteRef = ref.child(restoreNote.key);
                        NoteModel note = new NoteModel(restoreNote.title, restoreNote.textOfNote, noteRef.getKey());
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
        reference.child(reusult.get(position).key).removeValue();
    }

    private NoteModel setRestoreNote(int position){
        String restoreTitle = String.valueOf((reusult.get(position).title));
        String restoreTextOfNote = String.valueOf((reusult.get(position).textOfNote));
        String restoreKey = String.valueOf((reusult.get(position).key));
        NoteModel restoreNote = new NoteModel(restoreTitle, restoreTextOfNote, restoreKey);
        return restoreNote;
    }

    @Override
    public void onBackPressed(){
        return;
    }

    private void addEmailRecord(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref;
        UserModel currentUser = new UserModel(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()));
        ref = database.getReference("Users").child(currentUser.userId);
        DatabaseReference emailRef = ref.child("email");
        emailRef.setValue(currentUser.email);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.bottomappbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d("MyApp", String.valueOf(item.getItemId()));
        switch (item.getItemId()){
                    case (R.id.app_bar_search):
                        Toast.makeText(getApplicationContext(), "In the development", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        BottomNavigationDrawerFragment a = new BottomNavigationDrawerFragment();
                        a.show(getSupportFragmentManager(), a.getTag());
                        break;
                }
                return true;
    }

}

