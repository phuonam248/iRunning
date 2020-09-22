package com.appsnipp.homedesign2.Entity;


import java.util.ArrayList;

public interface CallbackInterface {
    void onCallback(boolean connect);
    void onCallbackOnlineAccount(ArrayList<User> list);
}