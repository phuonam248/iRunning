package com.appsnipp.homedesign2.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.appsnipp.homedesign2.Entity.News;
import com.appsnipp.homedesign2.Entity.User;
import com.appsnipp.homedesign2.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class SignUpActivity extends AppCompatActivity {
    FirebaseAuth auth;
    DatabaseReference reference;
    private final String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private EditText email, name, password, confirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        initial();
        auth = FirebaseAuth.getInstance();
        setUpActionBar();
    }

    private void setUpActionBar() {
        ActionBar actionBar = getSupportActionBar();
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#FF6700"));
        actionBar.setBackgroundDrawable(colorDrawable);
    }

    private void initial() {
        email = findViewById(R.id.email);
        name = findViewById(R.id.name);
        password = findViewById(R.id.password);
        confirm = findViewById(R.id.confirm);
    }


    public void onClick_sign_upBtn(View view) {

        final String emailTxt = email.getText().toString().trim();
        String passwordTxt = password.getText().toString().trim();
        String nameTxt = name.getText().toString().trim();
        String confirmTxt = confirm.getText().toString().trim();
        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (emailTxt.matches(emailPattern) && s.length() > 0) {
                    Toast.makeText(getApplicationContext(), "Valid email address!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Invalid email address!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        if (!emailTxt.equals("") && !nameTxt.equals("") && !passwordTxt.equals("") && !confirmTxt.equals("")) {
            if (!passwordTxt.equals(confirmTxt)) {
                Toast.makeText(getApplicationContext(), "Password does not match!", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(emailTxt) || TextUtils.isEmpty(nameTxt) || TextUtils.isEmpty(passwordTxt)) {
                Toast.makeText(SignUpActivity.this, "All fileds are required!", Toast.LENGTH_SHORT).show();
            } else if (passwordTxt.length() < 6) {
                Toast.makeText(SignUpActivity.this, "Password must be at least 6 characters!", Toast.LENGTH_SHORT).show();
            } else {
                register(nameTxt, emailTxt, passwordTxt);
            }

        } else {
            Toast.makeText(getApplicationContext(), "Please fill all of information!", Toast.LENGTH_SHORT).show();
        }
    }

    private void register(final String username, final String email, String password) {

        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser firebaseUser = auth.getCurrentUser();
                            assert firebaseUser != null;
                            String userId = firebaseUser.getUid();

                            reference = FirebaseDatabase.getInstance().getReference("Users").child(userId);
                            String weight = "0";
                            long score = 0;
                            String distance = "0";
                            String totalCalo = "0";
                            User user = new User(userId, username, weight, email, score, distance, totalCalo);
                            user.setAvatar("");
                            reference.setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(SignUpActivity.this, "Successful Created!", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(SignUpActivity.this, "You can't register with this email or password", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("AAA", "onFailure: " + e);
            }
        });
    }
}