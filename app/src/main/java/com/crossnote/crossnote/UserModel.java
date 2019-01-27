package com.crossnote.crossnote;

import com.google.firebase.auth.FirebaseUser;

public class UserModel {
    String userId;
    String email;

    public UserModel(){

    }

    public UserModel(FirebaseUser currentUser) {
        this.email = currentUser.getEmail();
        this.userId = currentUser.getUid();
    }

    public String getEmail() {
        String result = email;
        if (email.isEmpty()) {
            result = "error";
        }
        return result;
    }

}