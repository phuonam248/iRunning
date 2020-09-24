package com.appsnipp.homedesign2.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Arrays;

// Facebook login libraries
import com.appsnipp.homedesign2.R;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

// Google login libraries
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.common.api.ApiException;

public class LoginActivity extends AppCompatActivity {

    // FACEBOOK login var
    private CallbackManager callbackManager;
    private AccessToken accessToken = AccessToken.getCurrentAccessToken();
    private boolean isLoggedIn = (accessToken != null && !accessToken.isExpired());

    // GOOGLE login var
    private GoogleSignInClient googleSignInClient;
    private GoogleSignInAccount account;
    private GoogleSignInOptions googleSignInOptions;
    // Anh
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // login authentication of firebase - Anh
        auth = FirebaseAuth.getInstance();
        // checkbox show password
        show_password_checkbox();

        // GOOGLE Login
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail().build();
        // Build a GoogleSignInClient with the options specified by gso.
        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);
        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        account = GoogleSignIn.getLastSignedInAccount(this);
        /*updateUI(account);*/


        // FACEBOOK Login
        callbackManager = CallbackManager.Factory.create();

        LoginButton loginButton = (LoginButton) findViewById(R.id.facebookLoginBtn);

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });

        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });
    }

    // FACEBOOK and GOOGLE login onActivityResult method
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            // Signed in successfully, show authenticated UI.
            /*updateUI(account);*/
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            //Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            /*updateUI(null);*/
        }
    }

    // Show password checkbox
    public void show_password_checkbox() {
        final EditText loginPassEditText = (EditText) findViewById(R.id.loginPassEditText);
        CheckBox showPassCheckbox = (CheckBox) findViewById(R.id.showPassCheckbox);
        showPassCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    loginPassEditText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    loginPassEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });
    }

    // onClick event listener
    public void onClick_facebookLoginBtn(View view) {
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile"));
    }


    // Navigate to Sign In layout
    public void onClick_navigateToSignInBtn(View view) {
        Intent navigateToSignIn = new Intent(this, SignUpActivity.class);
        startActivity(navigateToSignIn);
    }

    public void onClick_googleLoginBtn(View view) {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, 1);

    }

    // Anh
    public void onClick_loginBtn(View view) {
        EditText loginUsernameEditText = findViewById(R.id.loginUsernameEditText);
        EditText loginPassEditText = findViewById(R.id.loginPassEditText);
        if (TextUtils.isEmpty(loginUsernameEditText.getText().toString().trim()) || TextUtils.isEmpty(loginPassEditText.getText().toString().trim())) {
            Toast.makeText(LoginActivity.this, "All fields are required!", Toast.LENGTH_SHORT).show();
        } else {

            auth.signInWithEmailAndPassword(loginUsernameEditText.getText().toString().trim(), loginPassEditText.getText().toString().trim())
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(LoginActivity.this, "Authentication failed!", Toast.LENGTH_SHORT).show();
                                Toast.makeText(LoginActivity.this, "Wrong password or username", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }
    }
}
