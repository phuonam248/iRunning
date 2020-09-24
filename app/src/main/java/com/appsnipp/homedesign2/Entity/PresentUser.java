package com.appsnipp.homedesign2.Entity;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;


import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


import org.jetbrains.annotations.NotNull;


import java.util.ArrayList;
import java.util.Objects;


public class PresentUser  {
    public static CallbackInterface callbackInterface;
    public ArrayList<User> userArrayList;
    public Context context;
    //
    private boolean connected;
    public NetworkCallback callback;

    public NetworkCallback getCallback() {
        return callback;
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateStatus(context);
        }
    };


    public void updateStatus(Context context) {

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        boolean isConnected = false;
        Network[] allNetworks = connectivityManager.getAllNetworks();

        for (Network network : allNetworks) {
            NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(network);
            if (networkCapabilities != null) {
                if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                        || networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
                        || networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET))
                    isConnected = true;
                break;
            }
        }

        if (callback != null) {
            callback.onConnectionChanged(isConnected);
        }
    }

    public void registerContext(Context context, NetworkCallback callback) {
        this.callback = callback;
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        context.registerReceiver(receiver, filter);
        updateStatus(context);
    }

    public void unregisterContext(Context context) {
        callback = null;
        context.unregisterReceiver(receiver);
    }

    public PresentUser() {

    }



    public interface NetworkCallback {
        void onConnectionChanged(boolean connected);
    }



    public void getAllOnlineUser() {
        Query query = FirebaseDatabase.getInstance().getReference("Users");
        query.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NotNull DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if (Objects.equals(snapshot.child("status").getValue(), "online")) {
                        User user = snapshot.getValue(User.class);
                        userArrayList.add(user);
                    }
                }
                callbackInterface.onCallbackOnlineAccount(userArrayList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }


        });

    }

}
