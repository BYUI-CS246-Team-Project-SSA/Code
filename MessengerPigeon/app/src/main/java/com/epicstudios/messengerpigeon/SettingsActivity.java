package com.epicstudios.messengerpigeon;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsActivity extends AppCompatActivity {

    private CircleImageView settingsDisplayImage;
    private TextView settingsDisplayName;
    private TextView settingsDisplayStatus;
    private Button settingsChangeImage;
    private Button settingsChangeStatus;

    private DatabaseReference getUserDataRef;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        auth = FirebaseAuth.getInstance();
        String onlineUserId = auth.getCurrentUser().getUid();
        getUserDataRef = FirebaseDatabase.getInstance().getReference().child("Users").child(onlineUserId);

        settingsDisplayImage = (CircleImageView) findViewById(R.id.SettingsProfileImage);
        settingsDisplayName = (TextView) findViewById(R.id.SettingsName);
        settingsDisplayStatus = (TextView) findViewById(R.id.SettingsStatus);
        settingsChangeImage = (Button) findViewById(R.id.changeImage);
        settingsChangeStatus = (Button) findViewById(R.id.changeStatus);

        getUserDataRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String image = dataSnapshot.child("user_image").getValue().toString();
                String name = dataSnapshot.child("user_name").getValue().toString();
                String status = dataSnapshot.child("user_status").getValue().toString();
                String thumb_image = dataSnapshot.child("user_thumb_image").getValue().toString();

                settingsDisplayName.setText(name);
                settingsDisplayStatus.setText(status);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
