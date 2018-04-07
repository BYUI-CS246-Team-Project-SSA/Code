package com.epicstudios.messengerpigeon;

import android.*;
import android.Manifest;
import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class DisplayGroupActivity extends AppCompatActivity {

    private final String TAG = "DisplayGroupActivity";
    private ContactAdapter contactAdapter;
    private Bundle extras;
    private Presenter presenter;
    private EditText input;
    ArrayList<Pair<Long, String>> listPeople;

    private static final int READ_CONTACTS_PERMISSIONS_REQUEST = 1;
    private static final int SEND_SMS_PERMISSIONS_REQUEST = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);
        input = (EditText) findViewById(R.id.input);

        extras = getIntent().getExtras();
        List<String> temp = extras.getStringArrayList("Groups");
        List<String> tempIDs = extras.getStringArrayList("GroupIDs");
        presenter = new Presenter(this, temp, tempIDs);

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            getPermissionToReadContacts();
        } else {setup(); }
    }

    private void setup(){
        int index = extras.getInt("index");
        listPeople = presenter.getGroup(index);
        Log.d(TAG, "listPeople: "+ listPeople.toString());

        ListView people = (ListView) findViewById(R.id.peoples);
        contactAdapter = new ContactAdapter(this, R.layout.contact_display, listPeople, presenter.getPhotos());
        people.setAdapter(contactAdapter);
    }

    /**
     * checks for permissions then calls the send function in presenter
     * @param view the button
     */
    @TargetApi(Build.VERSION_CODES.M)
    public void onSendClick(View view) {
        String message = input.getText().toString();
        List<Pair<Long, String>> peoples = new ArrayList<>();
        for(int x = 0; x < listPeople.size(); x++) {
            if(!contactAdapter.isChecked(x)){ peoples.add(listPeople.get(x)); }
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            Log.w(TAG, "getting permissions to send sms");
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                    != PackageManager.PERMISSION_GRANTED) {
                if (shouldShowRequestPermissionRationale(Manifest.permission.SEND_SMS)) {
                    Toast.makeText(this, "Need permissions to send message", Toast.LENGTH_LONG).show();
                }
                requestPermissions(new String[]{Manifest.permission.SEND_SMS},
                        SEND_SMS_PERMISSIONS_REQUEST);
            }
        } else { presenter.addConversation(peoples, message); input.setText(""); }

        /*if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.SEND_SMS)//TODO mms
                != PackageManager.PERMISSION_GRANTED) {
            //getPermissionToReadSMS(); This should be in a fragment activity
        }*/
    }

    /**
     * called to get permission to read contacts from the phone.
     * ASync request with onRequestPermissionResult as callback function
     */
    @TargetApi(Build.VERSION_CODES.M)
    private void getPermissionToReadContacts() {
        Log.w(TAG, "getting permissions to read contacts in API 23+");
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            if (shouldShowRequestPermissionRationale(android.Manifest.permission.READ_CONTACTS)) {
                Toast.makeText(this, "Need permissions to read contacts in order to" +
                        " display both groups and contacts", Toast.LENGTH_LONG).show();
            }
            requestPermissions(new String[]{android.Manifest.permission.READ_CONTACTS},
                    READ_CONTACTS_PERMISSIONS_REQUEST);
        }
    }
    public void getPermissions(View view){
        getPermissionToReadContacts();
    }

    /**
     * called by the permissions request when done processing
     * @param requestCode code associated with initial request
     * @param permissions ???
     * @param grantResults value indicating the results of the request
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        TextView error = findViewById(R.id.error);
        Button errorbt = findViewById(R.id.errorbt);
        if (requestCode == READ_CONTACTS_PERMISSIONS_REQUEST) {
            if (grantResults.length == 1 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                /** parent.getContext() **/
                Toast.makeText(this, "Read Contacts permission granted", Toast.LENGTH_SHORT).show();
                error.setVisibility(View.GONE);
                errorbt.setVisibility(View.GONE);
                setup();
            } else {
                Toast.makeText(this, "Read Contacts permission denied, Warning! " +
                        "Most of functionality relies on this permission", Toast.LENGTH_SHORT).show();
                error.setVisibility(View.VISIBLE);
                errorbt.setVisibility(View.VISIBLE);
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}