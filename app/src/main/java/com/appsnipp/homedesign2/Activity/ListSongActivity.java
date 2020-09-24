package com.appsnipp.homedesign2.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.appsnipp.homedesign2.Adapter.SongsAdapter;
import com.appsnipp.homedesign2.R;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

public class ListSongActivity extends AppCompatActivity {
    private static final int MODE_DARK = 1;
    private static final int MODE_LIGHT = 1;
    @Override
    protected void onResume() {
        super.onResume();
        setStatus("online");
    }

    public void setStatus(String status) {
        String currentUserId;
        currentUserId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        final DatabaseReference presenceRef = FirebaseDatabase.getInstance().getReference("Users").child(currentUserId).child("status");
        presenceRef.setValue(status);

    }

    BottomNavigationView bottomNavigationView;
    SongsAdapter _songsAdapter;
    ArrayList<File> _listSong;
    ListView _songListView;
    String TAG = "SongPlayer";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_song);
        setUpBotNav();
        requestPermission();
        loadSongFromExternalStorageToArrayList();
        setSongListView();
    }
    private void setUpBotNav() {
        bottomNavigationView = findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        bottomNavigationView.setSelectedItemId(R.id.navigation4);
    }
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.navigation1:
                    Intent news = new Intent(ListSongActivity.this, NewsActivity.class);
                    startActivity(news);
                    return true;

                case  R.id.navigation2:
                    Intent diet = new Intent(ListSongActivity.this, DietActivity.class);
                    startActivity(diet);
                    return true;

                case R.id.navigation3:
                    Intent home = new Intent(ListSongActivity.this, MainActivity.class);
                    startActivity(home);
                    return true;

                case  R.id.navigation4:
                    return true;

                case R.id.navigation5:
                    Intent setting = new Intent(ListSongActivity.this, SettingActivity.class);
                    startActivity(setting);
                    return true;
            }
            return false;
        }
    };
    private void setSongListView() {
        _songsAdapter = new SongsAdapter(this, R.layout.song_list_item, _listSong);
        _songListView = findViewById(R.id.songListView);
        _songListView.setAdapter(_songsAdapter);
        _songListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intentToMediaPlayer = new Intent(ListSongActivity.this, PlayActivity.class);
                intentToMediaPlayer.putExtra("songPos", i);
                intentToMediaPlayer.putExtra("listSong", _listSong);
                startActivity(intentToMediaPlayer);
            }
        });
    }
    public void requestPermission() {
        Dexter.withContext(this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {

                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();
    }
    public ArrayList<File> findSong(File dir) {
        ArrayList<File> listSong = new ArrayList<>();
        File[] listFile = dir.listFiles();
        if (listFile != null) {
            for (int i = 0; i < listFile.length; ++i) {
                if (listFile[i].isDirectory() && !listFile[i].isHidden()) {
                    listSong.addAll(findSong(listFile[i]));
                }
                else  {
                    if (listFile[i].getName().endsWith(".mp3")) {
                        listSong.add(listFile[i]);
                    }
                }
            }
        }
        else
            Log.d(TAG, "listFile is null. => line 89");
        return listSong;
    }
    public void loadSongFromExternalStorageToArrayList() {
        _listSong = findSong(Environment.getExternalStorageDirectory());
    }
}