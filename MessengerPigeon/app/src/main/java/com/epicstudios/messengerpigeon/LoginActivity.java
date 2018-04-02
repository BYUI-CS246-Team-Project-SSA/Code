package com.epicstudios.messengerpigeon;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
    private Button signInButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        logEmail = (EditText) findViewById(R.id.login_email);
        logPswd = (EditText) findViewById(R.id.login_pswd);
        signInButton = (Button) findViewById(R.id.login_button);

        loadBar = new ProgressDialog(this);
        auth = FirebaseAuth.getInstance();

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = logEmail.getText().toString();
                String pswd = logPswd.getText().toString();

                signIntoAccount(email, pswd);
            }
        });

        Button btnRegister = (Button) findViewById(R.id.register);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent go = new Intent(LoginActivity.this, RegisterActivity.class);
                go.putExtra("email", logEmail.getText().toString());
                go.putExtra("password", logPswd.getText().toString());
                startActivity(go);
            }
        });
    }


    private void signIntoAccount(String email, String pswd)
    {
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
            loadBar.setTitle("Signing In");
            loadBar.setMessage("Please Wait.");
            loadBar.show();

            auth.signInWithEmailAndPassword(email, pswd)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful())
                            {
                                Intent mainIntent = new Intent(LoginActivity.this,
                                        MainActivity.class);
                                mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(mainIntent);
                                finish();
                            }
                            else
                            {
                                Log.w("LoginActivity", "Failure to sign in.", task.getException());
                                Toast.makeText(LoginActivity.this,
                                        "Error: Incorrect Email or Password.",
                                        Toast.LENGTH_LONG).show();
                            }

                            loadBar.dismiss();
                        }
                    });
        }

    }

}
