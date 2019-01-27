package com.crossnote.crossnote;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.NavigationView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;


public class BottomNavigationDrawerFragment extends BottomSheetDialogFragment {

    public NavigationView nv;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        UserModel currentUser = new UserModel(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()));
        final View view =  inflater.inflate(R.layout.fragment_bottomsheet, container, false);

        nv = view.findViewById(R.id.navigation_view);
        nv.setItemIconTintList(null);

        Menu menu = nv.getMenu();
        menu.findItem(R.id.nav_email).setTitle(currentUser.getEmail());

        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case (R.id.nav3):

                        Intent intent = new Intent();
                        intent.setClass(view.getContext(), MainActivity.class);
                        intent.putExtra("finish", true);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        FirebaseAuth.getInstance().signOut();
                        break;
                    case (R.id.nav_about):
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://t.me/DirtyAlex"));
                        startActivity(browserIntent);
                        break;
                }
                return false;
            }
        });
        return view;

    }


}
