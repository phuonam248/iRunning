package com.appsnipp.homedesign2.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.appsnipp.homedesign2.Entity.History;
import com.appsnipp.homedesign2.R;

import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity {

    ArrayList<History> _historyList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

    }
}