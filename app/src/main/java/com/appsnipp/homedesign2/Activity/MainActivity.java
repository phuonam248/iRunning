package com.appsnipp.homedesign2.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.appsnipp.homedesign2.Adapter.UserRankingAdapter;
import com.appsnipp.homedesign2.Entity.History;
import com.appsnipp.homedesign2.Entity.PresentUser;
import com.appsnipp.homedesign2.Entity.User;
import com.appsnipp.homedesign2.Others.BottomNavigationBehavior;
import com.appsnipp.homedesign2.Others.DarkModePrefManager;
import com.appsnipp.homedesign2.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Objects;
import java.util.Scanner;

import static java.util.Collections.sort;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, PresentUser.NetworkCallback {
    private static final String TAG = "MainActivity";
    private FirebaseAuth fAuth;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private ArrayList<User> listUser;
    private UserRankingAdapter userRankingAdapter;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private ImageView avatarImage, avatarUpload;
    private TextView userName;
    private TextView userSCore;
    private TextView lastDistance;
    private TextView lastDuration;
    private TextView lastScore;
    private TextView lastCalories;
    private LinearLayout lastRecordWrapper;
    private DatabaseReference databaseReference;
    private User currentUser;
    public String currentUserId;
    private BottomNavigationView bottomNavigationView;
    private static final int MODE_DARK = 1;
    private static final int MODE_LIGHT = 1;
    private History history;
    private boolean check = false;
    private PresentUser presentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        checkAndRecordStatus();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        bottomNavigationView = findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        bottomNavigationView.setSelectedItemId(R.id.navigation3);
        //handling floating action menu
        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) bottomNavigationView.getLayoutParams();
        layoutParams.setBehavior(new BottomNavigationBehavior());
        initComponent();
        getDataFromMap();
        initRankingList();
        initListenerConnect();
        setUpCurUserInfo();
    }

    void initListenerConnect() {
        presentUser = new PresentUser();
        presentUser.registerContext(getApplicationContext(), this);
        presentUser.updateStatus(getApplicationContext());
    }

    void checkAndRecordStatus() {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myConnectionsRef = database.getReference("connections");
        final DatabaseReference lastOnlineRef = database.getReference("lastOnline");
        final DatabaseReference connectedRef = database.getReference(".info/connected");

        connectedRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NotNull DataSnapshot snapshot) {
                boolean connected = snapshot.getValue(Boolean.class);
                final DatabaseReference presenceRef = FirebaseDatabase.getInstance().getReference("Users").child(currentUserId).child("status");
                String id = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
                if (connected) {
                    DatabaseReference con = myConnectionsRef
                            .child(id);
                    con.setValue(Boolean.TRUE);
                    presenceRef.setValue("online");
                    con.onDisconnect().setValue(false);

                    lastOnlineRef.child(id).onDisconnect().setValue(ServerValue.TIMESTAMP);

                } else {

                    presenceRef.setValue("offline");
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.w(TAG, "Listener was cancelled at .info/connected");
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        setStatus("online");
    }

    public void setStatus(String status) {
        currentUserId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        final DatabaseReference presenceRef = FirebaseDatabase.getInstance().getReference("Users").child(currentUserId).child("status");
        presenceRef.setValue(status);

    }

    @Override
    protected void onPause() {

        setStatus("offline");
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        presentUser.unregisterContext(getApplicationContext());
        super.onDestroy();
    }

    public void initComponent() {
        databaseReference = FirebaseDatabase.getInstance().getReference().child("images");
        listUser = new ArrayList<>();
        linearLayoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        recyclerView = findViewById(R.id.listRanking);
//        DividerItemDecoration itemDecor = new DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL);
//        recyclerView.addItemDecoration(itemDecor);
        avatarImage = findViewById(R.id.avatar);
        userName = findViewById(R.id.userName);
        userSCore = findViewById(R.id.userScore);

        recyclerView.setLayoutManager(linearLayoutManager);

        lastCalories = findViewById(R.id.hisCaloriesTextView);
        lastDistance = findViewById(R.id.hisDistanceTextView);
        lastDuration = findViewById(R.id.hisDurationTextView);
        lastScore = findViewById(R.id.hisScoreTextView);
        lastRecordWrapper = findViewById(R.id.lastRecordWrapper);
    }

    public void setLastRecord(String calories, String distance, String duration, long score) {
        lastCalories.setText(calories);
        lastDuration.setText(duration);
        lastDistance.setText(distance);
        lastScore.setText(String.valueOf(score));
    }

    public void setUpCurUserInfo() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        assert currentUser != null;
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Users").child(currentUser.getUid());
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                userName.setText(user.getName());
                userSCore.setText(user.getScore().toString());
                storage = FirebaseStorage.getInstance();
                storageReference = storage.getReference();
                StorageReference profileRef = storageReference.child("images/" + user.getId());
                profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Glide.with(getApplicationContext()).asBitmap().load(uri).apply(new RequestOptions()
                                .skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.DATA)
                                .error(R.drawable.ic_user).circleCrop()).thumbnail(0.1f).into(avatarImage);


                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure: " + e.getMessage());
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MainActivity.this, "" + databaseError.getCode(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void initRankingList() {
        loadData();
    }

    public void loadData() {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("Users");
        ref.keepSynced(true);
        setUpNewsListView();

        userRankingAdapter.notifyDataSetChanged();
        if (listUser != null && !listUser.isEmpty()) {
////            _selectedNews = _newsList.get(0);
//            setUpStartingSelectedNews(_selectedNews);
        }

        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                User user = snapshot.getValue(User.class);
                listUser.add(user);
                System.out.println(user.toString());
                sort(listUser);
                userRankingAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                User user = snapshot.getValue(User.class);
                for (int i = 0; i < listUser.size(); ++i) {
                    if (listUser.get(i).getId().equals(user.getId())) {
                        listUser.set(i, user);
                        sort(listUser);
                        userRankingAdapter.notifyDataSetChanged();
                        break;
                    }
                    userRankingAdapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                for (User item : listUser) {
                    if (item.getId().equals(user.getId())) {
                        listUser.remove(item);
                        sort(listUser);
                        userRankingAdapter.notifyDataSetChanged();
                        break;
                    }
                    userRankingAdapter.notifyDataSetChanged();

                }

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });


        //     Log.d("UAA", "onDataChange: " + _newsList.size());
//        if (!_newsList.isEmpty()) {
//            _selectedNews = _newsList.get(0);
//        }
    }

    private void sort(ArrayList<User> listUser) {
        Collections.sort(listUser, new Comparator<User>() {
            @Override
            public int compare(User o1, User o2) {
                return o2.getScore().compareTo(o1.getScore());
            }
        });
    }

    private void setUpNewsListView() {
        userRankingAdapter = new UserRankingAdapter(listUser);
        recyclerView.setAdapter(userRankingAdapter);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.navigation1:
                    Intent news = new Intent(MainActivity.this, NewsActivity.class);
                    startActivity(news);
                    return true;

                case R.id.navigation2:
                    Intent home = new Intent(MainActivity.this, DietActivity.class);
                    startActivity(home);
                    return true;
                case R.id.navigation3:
                    return true;
                case R.id.navigation4:
                    Intent music = new Intent(MainActivity.this, ListSongActivity.class);
                    startActivity(music);
                    return true;
                case R.id.navigation5:
                    Intent setting = new Intent(MainActivity.this, SettingActivity.class);
                    startActivity(setting);
                    return true;
            }
            return false;
        }
    };

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

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

    //create a seperate class file, if required in multiple activities
    public void setDarkMode(Window window) {
        if (new DarkModePrefManager(this).isNightMode()) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            changeStatusBar(MODE_DARK, window);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            changeStatusBar(MODE_LIGHT, window);
        }
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

    public void onClick_startMap(View view) {
        Intent navigateToStartMap = new Intent(this, MapsActivity.class);
        startActivity(navigateToStartMap);
    }

    public void getDataFromMap() {
        Intent intent = getIntent();
        check = intent.getBooleanExtra("check", false);
        if (check) {
            String distance = intent.getStringExtra("distance");
            String duration = intent.getStringExtra("duration");
            String date = intent.getStringExtra("date");
            String calories = intent.getStringExtra("calories");
            long score = (long) intent.getIntExtra("score", 0);
            saveCurUserHistory(date, duration, distance, calories, score);
            setLastRecord(calories, distance, duration, score);
        } else {
            LoadHistoryOfCurUser();
        }
//        saveCurUserHistory("9/22/2020", "00:00:12", "30 km", "0.05 kcal", 100);
    }

    public void saveCurUserHistory(final String date, final String duration, final String distance, final String calories, final long score) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        assert currentUser != null;
        History history = new History(currentUser.getUid(), date, duration, distance, calories, score);
        addHistoryIntoUserHasId(history);
        final DatabaseReference Ref = FirebaseDatabase.getInstance().getReference("Users").child(currentUser.getUid());
        Ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                long updateScore = user.getScore() + score;
                DatabaseReference newScoreRef = FirebaseDatabase.getInstance().getReference("Users").child(user.getId()).child("score");
                newScoreRef.setValue(updateScore);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    //Ham them 1 history
    public void addHistoryIntoUserHasId(History history) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("History").child(history.getUserId());
        reference.push().setValue(history);
    }

    double getDoubleFromString(String string) {
        Scanner st = new Scanner(string);
        while (!st.hasNextDouble()) {
            st.next();
        }
        return st.nextDouble();
    }

    //M phai init may cai adapter voi list... DataReference bla...
    //Datetime la voi user id xem nhu la primary key cua history, m cho no ngay gio phut giay luon

    public void fullList_onClick(View view) {
        Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
        startActivity(intent);
    }

    public void loadHistoryByUserId(final String id) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("History").child(id);
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                History history = snapshot.getValue(History.class);
                if (history != null && history.getUserId().equals(id)) {
                    lastRecordWrapper.setVisibility(View.VISIBLE);
                    setLastRecord(history.getCalories(),
                            history.getDistance(),
                            history.getDuration(),
                            history.getScore());
                } else {
                    lastRecordWrapper.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String
                    previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                History history = snapshot.getValue(History.class);

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void LoadHistoryOfCurUser() {
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
                Toast.makeText(MainActivity.this, "" + databaseError.getCode(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onConnectionChanged(boolean connected) {

        currentUserId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        final DatabaseReference presenceRef = FirebaseDatabase.getInstance().getReference("Users").child(currentUserId).child("status");
        final DatabaseReference userLastOnlineRef = FirebaseDatabase.getInstance().getReference("UsersLastOnline").child(currentUserId);
        if (connected) {
            presenceRef.setValue("online");
            userLastOnlineRef.setValue(0);
            System.out.println("GgG");
            userRankingAdapter.notifyDataSetChanged();
        } else {
//            for (int i = 0; i < listUser.size(); i++) {
//                User user = listUser.get(i);
//                if (user.getId().equals(currentUserId)) {
//                   user.setStatus("offline");
//                   userRankingAdapter.notifyDataSetChanged();
//                   break;
//                }
//            }
        }


    }

}
