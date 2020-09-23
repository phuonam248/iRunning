package com.appsnipp.homedesign2.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.appsnipp.homedesign2.Adapter.HistoryAdapter;
import com.appsnipp.homedesign2.Entity.History;
import com.appsnipp.homedesign2.Entity.User;
import com.appsnipp.homedesign2.R;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity {

    private FirebaseAuth fAuth;
    private FirebaseStorage storage;
    private ArrayList<History> _historyList;
    private HistoryAdapter _historyAdapter;
    private ListView _historyListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        initComponents();
        LoadHistoryOfCurUser();
        setUpActionBar();
    }

    private void setUpActionBar() {
        ActionBar actionBar = getSupportActionBar();
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#FF6700"));
        actionBar.setBackgroundDrawable(colorDrawable);
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
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("History").child(id);
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                History history = snapshot.getValue(History.class);
                assert history != null;
                if (history.getUserId().equals(id)) {
                    _historyList.add(history);
                    _historyAdapter.notifyDataSetChanged();
                    setUpListView();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String
                    previousChildName) {
                History history = snapshot.getValue(History.class);
                for (int i = 0; i < _historyList.size(); ++i) {
                    assert history != null;
                    if (_historyList.get(i).getUserId().equals(id)
                            && _historyList.get(i).getDate().equals(history.getDate())) {
                        _historyList.set(i, history);
                        _historyAdapter.notifyDataSetChanged();
                        setUpListView();
                        break;
                    }
                    _historyAdapter.notifyDataSetChanged();
                    setUpListView();
                }
            }
            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                History history = snapshot.getValue(History.class);
                for (History item : _historyList) {
                    assert history != null;
                    if (item.getUserId().equals(id)
                            && item.getDate().equals(history.getDate())) {
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

    void LoadHistoryOfCurUser() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        assert currentUser != null;

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Users").child(currentUser.getUid());
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                loadHistoryByUserId(user.getId());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(HistoryActivity.this, "" + databaseError.getCode(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}