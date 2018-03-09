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

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private ProgressDialog loadBar;

    private EditText userName;
    private EditText userEmail;
    private EditText userPswd;
    private Button createAcc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        auth = FirebaseAuth.getInstance();
        userName = (EditText) findViewById(R.id.register_name);
        userEmail = (EditText) findViewById(R.id.register_email);
        userPswd = (EditText) findViewById(R.id.register_pswd);
        createAcc = (Button) findViewById(R.id.create_acc_button);
        loadBar = new ProgressDialog(this);

        createAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = userName.getText().toString();
                String email = userEmail.getText().toString();
                String pswd = userPswd.getText().toString();

                registerAccount(name, email, pswd);
            }
        });
    }


    private void registerAccount(String name, String email, String pswd)
    {
        if (TextUtils.isEmpty(name))
        {
            Toast.makeText(RegisterActivity.this,
                              "Please include your name.",
                                    Toast.LENGTH_LONG).show();
        }

        else if (TextUtils.isEmpty(email))
        {
            Toast.makeText(RegisterActivity.this,
                              "Please include your email.",
                                    Toast.LENGTH_LONG).show();
        }

        else if (TextUtils.isEmpty(pswd))
        {
            Toast.makeText(RegisterActivity.this,
                              "Please include a password.",
                                    Toast.LENGTH_LONG).show();
        }

        else
        {
            loadBar.setTitle("Creating New Account");
            loadBar.setMessage("Please Wait.");
            loadBar.show();

            auth.createUserWithEmailAndPassword(email, pswd)
            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful())
                    {
                        Intent mainIntent = new Intent(RegisterActivity.this,
                                                                     MainActivity.class);
                        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(mainIntent);
                        finish();
                    }
                    else
                    {
                        Log.w("activity", "failure", task.getException());
                        Toast.makeText(RegisterActivity.this,
                                        "Error. Try again.",
                                         Toast.LENGTH_LONG).show();
                    }

                    loadBar.dismiss();
                }
            });
        }

    }

}