package com.appsnipp.homedesign2.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Toast;

import com.appsnipp.homedesign2.Entity.User;
import com.appsnipp.homedesign2.Others.DarkModePrefManager;
import com.appsnipp.homedesign2.R;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;


public class SettingActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "SettingActivity";
    private static final int MODE_DARK = 1;
    private static final int MODE_LIGHT = 1;

    BottomNavigationView bottomNavigationView;
    private AlertDialog dialog;

    private EditText _edtTxtName;
    private EditText _edtTxtPassword;
    private EditText _edtTxtEmail;

    private EditText _edtTxtNewPassword;
    private EditText _edtTxtReEnterPassword;
    private EditText _edtTxtOldPassword;

    private SeekBar volumeSeekbar = null;
    private AudioManager audioManager = null;
    private LinearLayout _linearLayoutChangeProfile;
    private String _userName=null;

    private View _view;

    private FirebaseUser currentUser;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private Uri filePath;
    private final int PICK_IMAGE_REQUEST = 10;
    private ImageView userAva;

    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

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
        initUserAva();
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

    public void initUserAva() {
        ImageView imageView = findViewById(R.id.settingAvatar);
        loadCurUserImage(imageView);
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

                case R.id.navigation3:
                    Intent setting = new Intent(SettingActivity.this, MainActivity.class);
                    startActivity(setting);
                    return true;

                case R.id.navigation4:
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
        _view = getLayoutInflater().inflate(R.layout.edit_profile_layout, null);
        _builder.setView(_view);
        dialog = _builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        dialog.show();
        dialog.getWindow().setLayout(900, 1500);
        initEditText(_view);
        ImageView imageView = _view.findViewById(R.id.changeAva);
        loadCurUserImage(imageView);
        String a = "hehe";
        //loadCurrentUser(1,a);
    }

    private void initEditText(View view) {
        _edtTxtName = (EditText) view.findViewById(R.id.editTextName);
        _edtTxtPassword = (EditText) view.findViewById(R.id.editTextPassword);
        _edtTxtEmail = (EditText) view.findViewById(R.id.editTextEmail);
        loadCurrentUser(0, "");
    }

    public void btnChangeAvatar_onClick(View view) {
        android.app.AlertDialog.Builder _builder = new AlertDialog.Builder(SettingActivity.this);
        View _view = getLayoutInflater().inflate(R.layout.activity_upload_image, null);
        _builder.setView(_view);
        dialog = _builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        Button btnChoose = (Button) _view.findViewById(R.id.btnChoose);
        Button btnUpload = (Button) _view.findViewById(R.id.btnUpload);
        userAva = (ImageView) _view.findViewById(R.id.imgView);
        btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();
            }
        });
        loadCurUserImage(userAva);
        dialog.show();
    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                userAva.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void loadCurUserImage(final ImageView ava) {
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
                StorageReference profileRef = storageReference.child("images/" + user.getId());
                profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Glide.with(getApplicationContext()).load(uri).error(R.drawable.ic_user).circleCrop().thumbnail(0.1f)
                                .into(ava);
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
                Toast.makeText(SettingActivity.this, "" + databaseError.getCode(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void uploadImage() {
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            if (filePath != null) {
                final ProgressDialog progressDialog = new ProgressDialog(this);
                progressDialog.setTitle("Uploading...");
                progressDialog.show();
                StorageReference ref = storageReference.child("images/" + currentUser.getUid().toString());
                ref.putFile(filePath)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                progressDialog.dismiss();
                                Toast.makeText(SettingActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();
                                loadCurUserImage(userAva);
                                ImageView imageView = _view.findViewById(R.id.changeAva);
                                loadCurUserImage(imageView);
                                initUserAva();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.dismiss();
                                Toast.makeText(SettingActivity.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                                        .getTotalByteCount());
                                progressDialog.setMessage("Uploaded " + (int) progress + "%");
                            }
                        });
            } else {
                Toast.makeText(this,"Something wrong !", Toast.LENGTH_LONG);
            }
        }
    }

    public void loadCurrentUser(final int type, final String stringChange) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        assert currentUser != null;
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference("Users").child(currentUser.getUid());

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                switch (type) {
                    case 1:
                        myRef.child("name").setValue(stringChange);
                        break;
                    default:
                        setEditProfile(user.getName(), user.getPassword(), user.getEmail());
                        break;
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(SettingActivity.this, "" + databaseError.getCode(), Toast.LENGTH_SHORT).show();
            }

            private void setEditProfile(String name, String password, String email) {
                _edtTxtName.setText(name);
                _edtTxtEmail.setText(email);
                _edtTxtPassword.setText(password);
            }
        });
    }

    public void onClick_changeData(View view) {
        int _typeChange = view.getId();
        switch (_typeChange) {
            case R.id.buttonEditName:
                editName();
                break;
            case R.id.buttonEditPassword:
                editPassword();
                break;
            default:
                break;
        }
    }

    private void editPassword() {
        android.app.AlertDialog.Builder _builder = new AlertDialog.Builder(SettingActivity.this);
        View _view = getLayoutInflater().inflate(R.layout.edit_password_layout, null);
        _builder.setView(_view);
        dialog = _builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        dialog.show();
        initChangePasswordEditText(_view);
    }

    private void initChangePasswordEditText(View view) {
        _edtTxtNewPassword = (EditText)view.findViewById(R.id.editTextNewPassword);
        _edtTxtReEnterPassword=(EditText)view.findViewById(R.id.editTextReEnterPassword);
        _edtTxtOldPassword= (EditText)view.findViewById(R.id.editTextOldPassword);
    }

    private void editName() {
        _linearLayoutChangeProfile=(LinearLayout)dialog.findViewById(R.id.linearLayoutChooseChange);
        _linearLayoutChangeProfile.setVisibility(View.VISIBLE);
        _userName=_edtTxtName.getText().toString();
        _edtTxtName.setEnabled(true);
    }

    private void changePassword(final String oldPass,final String newPass){
        // Get auth credentials from the user for re-authentication. The example below shows
        // email and password credentials but there are multiple possible providers,
        // such as GoogleAuthProvider or FacebookAuthProvider.
        if(user==null) Log.d(TAG, "changePassword: user null"); 
        AuthCredential credential = EmailAuthProvider
                .getCredential(user.getEmail(), oldPass.toString());

        Log.d(TAG, "changePassword: cho nay");
        // Prompt the user to re-provide their sign-in credentials
        user.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            user.updatePassword(newPass).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Log.d(TAG, "Password updated");
                                        Toast.makeText(SettingActivity.this, "Password has been updated!", Toast.LENGTH_SHORT).show();
                                        dialog.cancel();
                                    } else {
                                        Log.d(TAG, "Error password not updated");
                                    }
                                }
                            });
                        } else {
                            Log.d(TAG, "Error auth failed");
                        }
                    }
                });
    }

    public void onClick_cancelChangingPassword(View view) {
        dialog.cancel();
    }

    public void onClick_acceptChangingPassword(View view) {
        String _oldPass= _edtTxtOldPassword.getText().toString();
        String _pass=_edtTxtNewPassword.getText().toString();
        String _rePass=_edtTxtNewPassword.getText().toString();
        if(_pass==null||_rePass==null||_oldPass==null){
            Toast.makeText(this, "Please fill all the information!",
                    Toast.LENGTH_SHORT).show();
            onClick_acceptChangingPassword(view);
        }
        if(_pass.equals(_rePass)){
            Log.d(TAG, "onClick_acceptChangingPassword: " +
                    _edtTxtNewPassword.getText().toString());
            Log.d(TAG, "onClick_acceptChangingPassword: " +
                    _edtTxtReEnterPassword.getText().toString());
            changePassword(_oldPass,_pass);
        } else {
            Toast.makeText(this, "Passwords are not match! Enter Password again!",
                    Toast.LENGTH_SHORT).show();
        }
    }

    public void onClick_acceptChangingProfile(View view) {
        String newName=_edtTxtName.getText().toString();
        if(newName!=null&&newName!=_userName){
            changeName(newName);
        }
        _edtTxtName.setText(newName);
        _edtTxtName.setEnabled(false);
        _linearLayoutChangeProfile.setVisibility(View.GONE);
    }

    private void changeName(String newName) {
        loadCurrentUser(1,newName);
    }

    public void onClick_cancelChangingProfile(View view) {
        _edtTxtName.setText(_userName);
        _edtTxtName.setEnabled(false);
        _linearLayoutChangeProfile.setVisibility(View.GONE);
    }
}