package com.example.lopuk.registr;

import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

@IgnoreExtraProperties
public class User {

    public String userId;
    public String email;

    public User(){

    }

    public User(FirebaseUser currentUser) {
        this.email = currentUser.getEmail();
        this.userId = currentUser.getUid();
    }

}
