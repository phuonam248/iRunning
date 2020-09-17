package com.appsnipp.homedesign2.Activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.appsnipp.homedesign2.R;

import java.io.File;
import java.util.ArrayList;

public class PlayActivity extends AppCompatActivity {
    private TextView _songTitleTextView;
    private static MediaPlayer _myMediaPlayer = new MediaPlayer();
    private SeekBar _mediaPlayerSeekBar;
    private Thread _updateSeekBar;
    private int _currentSongPos;
    private ArrayList<File> _listSong;
    private Button _pauseBtn;
    private Button _nextBtn;
    private Button _previousBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        setUpActionBar();
        loadData();
        checkPreviousMediaPlayerExist();
        initComponents();
        setUpSongTitle();
        createNewMedia();
        createUpdateSeekBar();
        setUpMediaPlayer();
        setUpUpdateSeekBar();
    }

    private void setUpSongTitle() {
        _songTitleTextView.setText(_listSong.get(_currentSongPos).getName().replace(".mp3", ""));
    }

    private void initComponents() {
        _pauseBtn = findViewById(R.id.mediaPlayerPauseBtn);
        _nextBtn = findViewById(R.id.mediaPlayerNextBtn);
        _previousBtn = findViewById(R.id.mediaPlayerPreviousBtn);
        _songTitleTextView = findViewById(R.id.mediaPlayerSongTitleTextView);
        _mediaPlayerSeekBar = findViewById(R.id.mediaPlayerSeekBar);
    }

    private void createUpdateSeekBar() {
        _updateSeekBar = new Thread(){
            @Override
            public void run() {
                int totalDuration = _myMediaPlayer.getDuration();
                int currentPos = 0;
                while (currentPos < totalDuration) {
                    try {
                        sleep(500);
                        currentPos = _myMediaPlayer.getCurrentPosition();
                        _mediaPlayerSeekBar.setProgress(currentPos);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
    }

    private void createNewMedia() {
        Uri uri = Uri.parse(_listSong.get(_currentSongPos).toString());
        _myMediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
    }

    private void loadData() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        _currentSongPos = bundle.getInt("songPos");
        _listSong = (ArrayList)bundle.getParcelableArrayList("listSong");
    }

    private void setUpActionBar() {
        ActionBar actionBar = getSupportActionBar();
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#FF6700"));
        actionBar.setBackgroundDrawable(colorDrawable);
        actionBar.setTitle("Now playing");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
    }

    private void setUpMediaPlayer() {
        _myMediaPlayer.start();
    }

    private void setUpUpdateSeekBar() {
        _mediaPlayerSeekBar.setMax(_myMediaPlayer.getDuration());
        _mediaPlayerSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                _myMediaPlayer.seekTo(seekBar.getProgress());
            }
        });
        _updateSeekBar.start();
    }

    public void pauseMedia_onClick(View view) {
        if (_myMediaPlayer.isPlaying()) {
            _pauseBtn.setBackgroundResource(R.drawable.play_icon);
            _myMediaPlayer.pause();
        }
        else {
            _pauseBtn.setBackgroundResource(R.drawable.pause_icon);
            _myMediaPlayer.start();
        }
    }

    public void checkPreviousMediaPlayerExist() {
        if (_myMediaPlayer != null) {
            releaseCurrentMedia();
        }
    }

    public void releaseCurrentMedia() {
        _myMediaPlayer.stop();
        _myMediaPlayer.release();
    }

    public void nextMedia_onClick(View view) {
        if (_currentSongPos >= _listSong.size()-1) {
            _currentSongPos = 0;
        }
        else _currentSongPos++;
        releaseCurrentMedia();
        createNewMedia();
        setUpSongTitle();
        setUpMediaPlayer();
    }

    public void previousMedia_onClick(View view) {
        if (_currentSongPos == 0) {
            _currentSongPos = _listSong.size()-1;
        }
        else _currentSongPos--;
        releaseCurrentMedia();
        createNewMedia();
        setUpSongTitle();
        setUpMediaPlayer();
    }
}