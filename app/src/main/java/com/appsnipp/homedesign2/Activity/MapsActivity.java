package com.appsnipp.homedesign2.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.appsnipp.homedesign2.Entity.User;
import com.appsnipp.homedesign2.R;
import com.bumptech.glide.Glide;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Scanner;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private String TAG = "MapsActivity";
    private LocationManager locationManager;
    private LocationListener locationListener;
    private StorageReference storageReference;
    private FirebaseStorage storage;
    private String startTime;
    private Calendar calendar;
    private ArrayList<LatLng> runningRecord;
    private Dialog dialog;
    private int totalScore = 0;
    private GoogleMap mMap;
    FusedLocationProviderClient mFusedLocationProviderClient;
    private int DEFAULT_ZOOM = 15;
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";
    private boolean locationPermissionGranted = false;
    private LatLng defaultLocation = new LatLng(10.762622, 106.660172);
    private Polyline curPolyline = null;
    private Location mPreviousLocation = null;
    private Location mLastKnownLocation = null;
    private Chronometer chronometer;
    private boolean isResume = false;
    private boolean isStarting = false;
    private long tMilisec, tStart, tBuff, tUpdate = 0L;
    private int sec, min, milisec;
    private double minDouble;
    private Handler handler;
    private Button startBtn, pauseBtn;
    private TextView scoreTextView;
    private TextView caloriesTextView;
    private TextView distanceTextView;
    // MET value for running with average speed of human
    private double MET = 9.8;
    // Average speed of human
    private double aveSpeed = 12.67;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Construct a FusedLocationProviderClient.
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        initComponents();
        handler = new Handler();
        setUpActionBar();
    }

    private void initComponents() {
        chronometer = findViewById(R.id.stopwatch);
        startBtn = findViewById(R.id.startBtn);
        pauseBtn = findViewById(R.id.pauseBtn);
        scoreTextView = findViewById(R.id.scoreTextView);
        distanceTextView = findViewById(R.id.distanceTextView);
        caloriesTextView = findViewById(R.id.caloriesTextView);
        dialog = new Dialog(this);
        runningRecord = new ArrayList<>();
        calendar = Calendar.getInstance();
    }

    String getCurrentDate() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
        return simpleDateFormat.format(calendar.getTime());
    }

    String getCurrentTime() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("h:mm a");
        return simpleDateFormat.format(calendar.getTime());
    }

    void showSummaryPopUp() {
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.summary_pop_up);
        Button restartBtn = dialog.findViewById(R.id.restartBtn);
        restartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                Intent backToMain = new Intent(MapsActivity.this, MainActivity.class);
                backToMain.putExtra("check", true);
                backToMain.putExtra("distance", distanceTextView.getText());
                backToMain.putExtra("duration", chronometer.getText());
                backToMain.putExtra("calories", caloriesTextView.getText());
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
                backToMain.putExtra("date", simpleDateFormat.format(calendar.getTime()));
                backToMain.putExtra("score", Integer.valueOf((String)scoreTextView.getText()));
                startActivity(backToMain);
            }
        });
        TextView totalScoreTextView = dialog.findViewById(R.id.totalScoreTextView);
        totalScoreTextView.setText(scoreTextView.getText());
        TextView totalDisTextView = dialog.findViewById(R.id.totalDisTextView);
        totalDisTextView.setText(distanceTextView.getText());
        TextView totalDurationTextView = dialog.findViewById(R.id.durationTextView);
        totalDurationTextView.setText(chronometer.getText());
        TextView totalCaloriesTextView = dialog.findViewById(R.id.totalCaloriesTextView);
        totalCaloriesTextView.setText(caloriesTextView.getText());
        TextView startTimeTextView = dialog.findViewById(R.id.startTimeTextView);
        startTimeTextView.setText(startTime);
        TextView endTimeTextView = dialog.findViewById(R.id.endTimeTextView);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("h:mm a");
        endTimeTextView.setText(simpleDateFormat.format(calendar.getTime()));
        loadCurrentUserAva();
        dialog.show();
    }

    public Runnable runnable = new Runnable() {
        @Override
        public void run() {
            tMilisec = SystemClock.uptimeMillis() - tStart;
            tUpdate = tBuff + tMilisec;
            sec = (int) (tUpdate / 1000);
            min = sec / 60;
            minDouble = (double) sec / 60;
            sec = sec % 60;
            milisec = (int) (tUpdate % 100);
            String newTime = String.format("%02d", min) + ":"
                    + String.format("%02d", sec) + ":"
                    + String.format("%02d", milisec);
            chronometer.setText(newTime);
            handler.postDelayed(this, 60);
        }
    };

    double totalCaloriesBurned(double dis, double weigh) {
        return (dis * MET * 3.5 * weigh) / 200;
    }

    public void loadCurrentUserAva() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        assert currentUser != null;

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Users").child(currentUser.getUid());
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                storage = FirebaseStorage.getInstance();
                storageReference = storage.getReference();
                StorageReference profileRef = storageReference.child("images/" + user.getId().toString());
                final ImageView imageView = dialog.findViewById(R.id.avaSummary);
                profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Glide.with(getApplicationContext()).load(uri).error(R.drawable.ic_user).circleCrop().thumbnail(0.1f)
                                .into(imageView);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure: "+ e.getMessage());
                    }
                });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MapsActivity.this, "" + databaseError.getCode(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        getLocationPermission();
    }

    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        Location curLocation;
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            getDeviceLocation(true);
            UpdateLocationChange();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    1); // 1 = PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getDeviceLocation(true);
                    UpdateLocationChange();
                }
            }
        }
    }

    private void UpdateLocationChange() {
        locationManager = (LocationManager) getSystemService(this.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                getDeviceLocation(false);
                if (mLastKnownLocation != null && mPreviousLocation != null  && isStarting && isResume) {
                    double distance = mPreviousLocation.distanceTo(mLastKnownLocation);
                    System.out.println(distance);
                    String curDistanceString = (String) distanceTextView.getText();
                    double curDistanceDouble = getDoubleFromString(curDistanceString);
                    System.out.println(curDistanceDouble);
                    distance/=1000;
                    distance += curDistanceDouble;
                    System.out.println(distance);
                    distanceTextView.setText(String.valueOf(Math.round(distance * 100.0) / 100.0) + " km");

                    String curCaloriesString = (String) caloriesTextView.getText();
                    double curCaloriesDouble = getDoubleFromString(curCaloriesString);
                    double newCalories = totalCaloriesBurned(curDistanceDouble, 70);
                    curCaloriesDouble += newCalories;
                    curCaloriesDouble = Math.round(curCaloriesDouble * 100.0) / 100.0;
                    caloriesTextView.setText(String.valueOf(curCaloriesDouble) + " kcal");

                    if (minDouble != 0) {
                        int newScore = (int) ((distance*1000) / (minDouble * 100));
                        int curScore = Integer.valueOf((String) scoreTextView.getText());
                        newScore += curScore;
                        scoreTextView.setText(String.valueOf(newScore));
                    }
                    if (mPreviousLocation != null) {
                        LatLng src = new LatLng(mPreviousLocation.getLatitude(), mPreviousLocation.getLongitude());
                        LatLng des = new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude());
                        runningRecord.add(src);
                        Polyline polyline = mMap.addPolyline(new PolylineOptions().width(15)
                                .color(MapsActivity.this.getResources().getColor(R.color.colorOrange))
                                .add(
                                src, des
                        ));
                    }
                    mPreviousLocation = mLastKnownLocation;
                }
                mPreviousLocation = mLastKnownLocation;
            }
        };
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
    }

    private void getDeviceLocation(final boolean isFirstTime) {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {

            final Task<Location>[] locationResult = new Task[]{mFusedLocationProviderClient.getLastLocation()};
            locationResult[0].addOnCompleteListener(this, new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    if (task.isSuccessful()) {
                        mMap.getUiSettings().setMyLocationButtonEnabled(true);
                        // Set the map's camera position to the current location of the device.
                        //LocationManager locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
                        //Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        mLastKnownLocation = task.getResult();
                        if (mLastKnownLocation != null) {
                            mMap.setMyLocationEnabled(true);
                            LatLng curLocation = new LatLng(mLastKnownLocation.getLatitude(),
                                    mLastKnownLocation.getLongitude());
                            System.out.println("lastKnownLocation != null");
                            if (isFirstTime) {
                                mMap.addMarker(new MarkerOptions().position(curLocation).title("You are here"));
                                CameraPosition newCameraPosition = new CameraPosition.Builder()
                                        .target(curLocation) // Sets the center of the map to Mountain View
                                        .zoom(15)                      // Sets the zoom
                                        .bearing(90)                   // Sets the orientation of the camera to east
                                        .tilt(30)                      // Sets the tilt of the camera to 30 degrees
                                        .build();
                                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(newCameraPosition));
                            }
                        } else {
                            System.out.println("lastKnownLocation = null");
                        }
                    } else {
                        mMap.moveCamera(CameraUpdateFactory
                                .newLatLngZoom(defaultLocation, DEFAULT_ZOOM));
                        mMap.getUiSettings().setMyLocationButtonEnabled(false);
                    }
                }
            });
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage(), e);
        }

    }

    double getDoubleFromString(String string) {
        Scanner st = new Scanner(string);
        while (!st.hasNextDouble()) {
            st.next();
        }
        return st.nextDouble();
    }

    private void drawRouteToChosenMap(LatLng curLocation) {
        mMap.moveCamera(CameraUpdateFactory.newLatLng(curLocation));
//        LatLng chosenMap = getChosenMap();
//        String routeMode = "driving";
//        String requestDirectionUrl = getRequestDirectionUrl(curLocation, chosenMap, routeMode);
//        new FetchURL(this).execute(requestDirectionUrl, routeMode);
    }

    public void start_onClick(View view) {
        if (!isStarting) {
            tStart = SystemClock.uptimeMillis();
            handler.postDelayed(runnable, 0);
            chronometer.start();
            isStarting = true;
            isResume = true;
            startBtn.setText("STOP");
            startTime = getCurrentTime();
        }
        else {
                handler.removeCallbacks(runnable);
                chronometer.stop();
                isStarting = false;
                isResume = false;
                startBtn.setText("START");
                pauseBtn.setText("PAUSE");
                showSummaryPopUp();
        }
    }

    public void pause_onClick(View view) {
        if (isStarting) {
            if (!isResume) {
                tStart = SystemClock.uptimeMillis();
                handler.postDelayed(runnable, 0);
                chronometer.start();
                isResume = true;
                pauseBtn.setText("PAUSE");
            }
            else {
                tBuff += tMilisec;
                handler.removeCallbacks(runnable);
                chronometer.stop();
                isResume = false;
                pauseBtn.setText("RESUME");
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        locationManager.removeUpdates(locationListener);
    }

    private void setUpActionBar() {
        ActionBar actionBar = getSupportActionBar();
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#FF6700"));
        actionBar.setBackgroundDrawable(colorDrawable);
    }
}