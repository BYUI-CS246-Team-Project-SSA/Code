package com.epicstudios.messengerpigeon;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private ProgressDialog loadBar;
    private EditText logEmail;
    private EditText logPswd;
    private SharedPreferences prefs;
    private final String EMAIL = "email";
    private final String PASSWORD = "password";
    private final String PREF_FILE = "com.epicstudios.messengerpigeon.LOGIN_PREF";
    private final String TAG = "LogInActivity:";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        prefs = this.getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE);
        Log.d(TAG, "Preference File Created/accessed");
        String email = prefs.getString(EMAIL, null);
        Log.d(TAG, "Successfully retrieved email = " + email);
        String pass = prefs.getString(PASSWORD, null);
        //Log.d(TAG, "Successfully retrieved password = " + pass);

        logEmail = (EditText) findViewById(R.id.login_email);
        logPswd = (EditText) findViewById(R.id.login_pswd);

        loadBar = new ProgressDialog(this);
        auth = FirebaseAuth.getInstance();

        if(email != null) {
            Log.d(TAG, "Email is not null = " + email);
            logEmail.setText(email);
            if (pass != null) {
                //Log.d(TAG, "Password is not null = " + pass);
                signIntoAccount(email, pass);
            }
        }
    }
    public void skipClick(View view){
        Intent mainIntent = new Intent(LoginActivity.this,
                MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
    }

    public void registerClick(View view) {
        Intent go = new Intent(LoginActivity.this, RegisterActivity.class);
        go.putExtra("email", logEmail.getText().toString());
        go.putExtra("password", logPswd.getText().toString());
        startActivity(go);
    }

    public void signIn(View view){
        String email = logEmail.getText().toString();
        String pswd = logPswd.getText().toString();
        signIntoAccount(email, pswd);
    }

    private void signIntoAccount(final String email, final String pswd) {
        //Log.d(TAG, "Started signIntoAccount() with email="+email+" and pass="+pswd);
        if (TextUtils.isEmpty(email))
        {
            Toast.makeText(LoginActivity.this,
                    "Please include your email.",
                    Toast.LENGTH_LONG).show();
        }

        else if (TextUtils.isEmpty(pswd))
        {
            Toast.makeText(LoginActivity.this,
                    "Please include your password.",
                    Toast.LENGTH_LONG).show();
        }

        else
        {
            Log.d(TAG+"signIn", "got to else");
            loadBar.setTitle("Signing In");
            loadBar.setMessage("Please Wait.");
            loadBar.show();
            Log.d(TAG+"signIn", "loaded loading bar");

            auth.signInWithEmailAndPassword(email, pswd)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //TODO ask if we should save signin info
                            Log.d(TAG, "creating editor");
                            SharedPreferences.Editor editor = prefs.edit();
                            Log.d(TAG, "Editor created");
                            Log.d(TAG, "putting email with string "+ email);
                            editor.putString(EMAIL, email);
                            //Log.d(TAG, "putting password with string "+ pswd);
                            editor.putString(PASSWORD, pswd);
                            Log.d(TAG, "about to apply");
                            editor.apply();
                            Log.d(TAG, "Preferences applied");

                            Intent mainIntent = new Intent(LoginActivity.this,
                                    MainActivity.class);
                            mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(mainIntent);
                            finish();
                        } else {
                            Log.w(TAG, "Failure to sign in.", task.getException());
                            Toast.makeText(LoginActivity.this,
                                    "Error: Incorrect Email or Password.",
                                    Toast.LENGTH_LONG).show();
                        }

                        loadBar.dismiss();
                    }
            }   );
        }
    }
}
