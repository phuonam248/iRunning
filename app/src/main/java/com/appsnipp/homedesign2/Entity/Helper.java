package com.appsnipp.homedesign2.Entity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class Helper {
    private FirebaseUser currentUser;
    private User currentUserEntity;

    public User getCurrentUser(ArrayList<User> all) {
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String curUserId = currentUser.getUid();
        return null;
    };
}
