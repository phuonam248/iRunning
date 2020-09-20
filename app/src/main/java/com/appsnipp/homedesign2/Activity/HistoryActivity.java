package com.appsnipp.homedesign2.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import com.appsnipp.homedesign2.Adapter.HistoryAdapter;
import com.appsnipp.homedesign2.Entity.History;
import com.appsnipp.homedesign2.R;

import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity {

    ArrayList<History> _historyList;
    HistoryAdapter _historyAdapter;
    ListView _historyListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        _historyList = new ArrayList<>();
        _historyList.add(new History("04/12/2020", "00:00:00", "1 km", "0.05 kcal", 300));
        _historyListView = findViewById(R.id.historyListView);
        _historyAdapter = new HistoryAdapter(this, R.layout.history_item, _historyList);
        _historyListView.setAdapter(_historyAdapter);
    }
}