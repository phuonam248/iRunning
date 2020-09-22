package com.appsnipp.homedesign2.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import com.appsnipp.homedesign2.Adapter.HistoryAdapter;
import com.appsnipp.homedesign2.Entity.History;
import com.appsnipp.homedesign2.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity {

    private ArrayList<History> _historyList;
    private HistoryAdapter _historyAdapter;
    private ListView _historyListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        initComponents();
    }

    public void initComponents() {
        _historyList = new ArrayList<>();
        _historyListView = findViewById(R.id.historyListView);
        _historyAdapter = new HistoryAdapter(this, R.layout.history_item, _historyList);
    }

    public void setUpListView() {
        _historyListView.setAdapter(_historyAdapter);
    }

    //Ham load history tu 1 user id
    public void loadHistoryByUserId(final String id) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("History");
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                History history = snapshot.getValue(History.class);
                assert history != null;
                if (history.get_userId().equals(id)) {
                    _historyList.add(history);
                }
                _historyAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String
                    previousChildName) {
                History history = snapshot.getValue(History.class);
                for (int i = 0; i < _historyList.size(); ++i) {
                    assert history != null;
                    if (_historyList.get(i).get_userId().equals(id)
                            && _historyList.get(i).get_date().equals(history.get_date())) {
                        _historyList.set(i, history);
                        _historyAdapter.notifyDataSetChanged();
                        break;
                    }
                    _historyAdapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                History history = snapshot.getValue(History.class);
                for (History item : _historyList) {
                    assert history != null;
                    if (item.get_userId().equals(id)
                            && item.get_date().equals(history.get_date())) {
                        _historyList.remove(item);
                        _historyAdapter.notifyDataSetChanged();
                        break;
                    }
                    _historyAdapter.notifyDataSetChanged();

                }

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}