package com.epicstudios.messengerpigeon;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private DatabaseReference storeUserDataRef;
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

        Bundle data = getIntent().getExtras();
        String email = data.getString("email");
        String password = data.getString("password");
        if(email != null) {
            userEmail.setText(data.getString("email"));
        }
        if(password != null){
            userPswd.setText(data.getString("password"));
        }

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


    private void registerAccount(final String name, String email, String pswd)
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
                        String currentUid = auth.getCurrentUser().getUid();
                        storeUserDataRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUid);

                        storeUserDataRef.child("user_name").setValue(name);
                        storeUserDataRef.child("user_status").setValue("Hey, I'm using MessengerPigeon!!");
                        storeUserDataRef.child("user_image").setValue("default_profile");
                        storeUserDataRef.child("user_thumb_image").setValue("default_image")
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        if (task.isSuccessful()) {
                                            Intent mainIntent = new Intent(RegisterActivity.this,
                                                    LoginActivity.class);
                                            mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(mainIntent);
                                            finish();
                                        }
                                    }
                                });
                    }
                    else
                    {
                        Log.w("RegisterActivity", "Failure to create account.", task.getException());
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