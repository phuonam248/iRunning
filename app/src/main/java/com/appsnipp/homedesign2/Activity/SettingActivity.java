package com.appsnipp.homedesign2.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Toast;

import com.appsnipp.homedesign2.Entity.User;
import com.appsnipp.homedesign2.Others.DarkModePrefManager;
import com.appsnipp.homedesign2.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class SettingActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final int MODE_DARK = 1;
    private static final int MODE_LIGHT = 1;

    BottomNavigationView bottomNavigationView;
    private AlertDialog dialog;

    private EditText _edtTxtName;
    private EditText _edtTxtPassword;
    private EditText _edtTxtPhoneNumber;
    private EditText _edtTxtEmail;
    private EditText _edtTxtAddress;

    private SeekBar volumeSeekbar = null;
    private AudioManager audioManager = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
//      setContentView(R.layout.profile_main);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
//        getSupportActionBar().setTitle("MyTitle");

        bottomNavigationView = findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        bottomNavigationView.setSelectedItemId(R.id.navigation5);

        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        // setContentView(R.layout.activity_setting);
        initControls();
    }

    // Anh
    public void onClick_logoutBtn(View view) {
        if (view.getId() == R.id.logoutBtn) {
            Toast.makeText(SettingActivity.this, "See you later !", Toast.LENGTH_LONG).show();
            FirebaseAuth.getInstance().signOut();
            Intent signInIntent = new Intent(SettingActivity.this, LoginActivity.class);
            startActivityForResult(signInIntent, 1);
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.navigation1:
                    Intent news = new Intent(SettingActivity.this, NewsActivity.class);
                    startActivity(news);
                    return true;

                case R.id.navigation2:
                    Intent home = new Intent(SettingActivity.this, DietActivity.class);
                    startActivity(home);
                    return true;

                case  R.id.navigation3:
                    Intent setting = new Intent(SettingActivity.this, MainActivity.class);
                    startActivity(setting);
                    return true;

                case  R.id.navigation4:
                    Intent music = new Intent(SettingActivity.this, ListSongActivity.class);
                    startActivity(music);
                    return true;

                case R.id.navigation5:
                    return true;
            }
            return false;
        }
    };


    private void initControls() {
        try {
            volumeSeekbar = (SeekBar) findViewById(R.id.VolMusicBar);
            audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            volumeSeekbar.setMax(audioManager
                    .getStreamMaxVolume(AudioManager.STREAM_MUSIC));
            volumeSeekbar.setProgress(audioManager
                    .getStreamVolume(AudioManager.STREAM_MUSIC));

            volumeSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onStopTrackingTouch(SeekBar arg0) {
                }

                @Override
                public void onStartTrackingTouch(SeekBar arg0) {
                }

                @Override
                public void onProgressChanged(SeekBar arg0, int progress, boolean arg2) {
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
                            progress, 0);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addTextControl() {

    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_dark_mode) {
            //code for setting dark mode
            //true for dark mode, false for day mode, currently toggling on each click
            DarkModePrefManager darkModePrefManager = new DarkModePrefManager(this);
            darkModePrefManager.setDarkMode(!darkModePrefManager.isNightMode());
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            recreate();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void changeStatusBar(int mode, Window window) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(this.getResources().getColor(R.color.contentStatusBar));
            //Light mode
            if (mode == MODE_LIGHT) {
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
        }
    }

    public void buttonEditProfile(View view) {
        android.app.AlertDialog.Builder _builder = new AlertDialog.Builder(SettingActivity.this);
        View _view = getLayoutInflater().inflate(R.layout.edit_profile_layout, null);
        _builder.setView(_view);
        dialog = _builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        dialog.show();
        dialog.getWindow().setLayout(900,1600);
        initEditText();
        String a="hehe";
        loadCurrentUser(1,a);
    }

    private void initEditText() {
        _edtTxtName=(EditText)findViewById(R.id.editTextName);
        _edtTxtPassword=(EditText)findViewById(R.id.editTextPassword);
        _edtTxtPhoneNumber=(EditText)findViewById(R.id.editTextPhoneNumber);
        _edtTxtEmail=(EditText)findViewById(R.id.editTextEmail);
        _edtTxtAddress=(EditText)findViewById(R.id.editTextAddress);
    }

    public void btnChangeAvatar_onClick(View view) {
    }

    public void loadCurrentUser(final int type, final String stringChange) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        assert currentUser != null;
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Users").child(currentUser.getUid());

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                user.getName();
                switch (type){
                    case 1:
                        user.setName(stringChange);
                        break;
                    case 2:
                        user.setPassword(stringChange);
                        break;
                    default:
                        break;
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(SettingActivity.this, "" + databaseError.getCode(), Toast.LENGTH_SHORT).show();
            }


        });


    }
}