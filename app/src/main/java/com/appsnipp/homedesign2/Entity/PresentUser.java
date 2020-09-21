package com.appsnipp.homedesign2.Entity;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


// class for
public class PresentUser implements com.appsnipp.homedesign2.Entity.CallbackInterface {
    private static PresentUser presentUser = null;
    private static com.appsnipp.homedesign2.Entity.CallbackInterface callbackInterface = null;

    private PresentUser() {
    }


    private static PresentUser getInstance() {
        if (presentUser == null) {
            presentUser = new PresentUser();
            callbackInterface = new com.appsnipp.homedesign2.Entity.CallbackInterface() {
                @Override
                public void onCallback(boolean connect) {
                    System.out.println(connect + "DDDDDDDDDDDDDDDDDDDDDDDDDDDDDD");
                }

                @Override
                public void onCallbackOnlineAccount(ArrayList<User> list) {
                    System.out.println(list.toString());
                }
            };
        }
        return presentUser;
    }

    public static void setOnline(String id) {
        DatabaseReference presenceRef = FirebaseDatabase.getInstance().getReference("Users").child(id).child("status");
        presenceRef.setValue("online");
    }

    public static void setOffline(String id) {
        DatabaseReference presenceRef = FirebaseDatabase.getInstance().getReference("Users").child(id).child("status");
        presenceRef.onDisconnect().setValue("offline");
    }

//    public static void getAllOnlineUser(ArrayList<User> list, String id) {
//        Query query = FirebaseDatabase.getInstance().getReference("Users").child(id).orderByChild("status");
//        query.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                User user = null;
//                ArrayList<User> list = new ArrayList<>();
//                for (DataSnapshot ds : snapshot.getChildren()) {
//                    user = ds.getValue(User.class);
//                    list.add(user);
//                    callbackInterface.onCallbackOnlineAccount(list);
//                }
//                System.out.println("FFFFFFFFFFFF"+ list.toString());
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }

//    public static void isOnline(String id) {
//        DatabaseReference presenceRef = FirebaseDatabase.getInstance().getReference("Users").child(id).child("status");
//        presenceRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                String connection = snapshot.getValue(String.class);
//                assert connection != null;
//                if (connection.equals("online")) {
//                    callbackInterface.onCallback(true);
//                } else {
//                    callbackInterface.onCallback(false);
//                }
//            }
//
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }


    @Override
    public void onCallback(boolean connect) {

    }

    @Override
    public void onCallbackOnlineAccount(ArrayList<User> list) {

    }
}
