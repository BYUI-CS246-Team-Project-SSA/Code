package com.epicstudios.messengerpigeon;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.SharedPreferences;
import android.os.Build;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Presenter presenter;
    private ArrayList<String> conversationList = new ArrayList<>();
    private ListView conversations;
    private ArrayAdapter arrayAdapter;
    private SharedPreferences prefs;
    public static boolean active = false;

    private static final String TAG = "MainActivity";
    private static final int READ_SMS_PERMISSIONS_REQUEST = 1;
    private static final int READ_CONTACTS_PERMISSIONS_REQUEST = 1;
    private static final String SHARED_PREF_FILE = "epicstudios.messengerpigeon.PREF_FILE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.startService(new Intent(this, QuickResponseService.class));

        conversationList.add("Only for now");
        conversations = (ListView) findViewById(R.id.conversations);
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, presenter.getConversations());
        conversations.setAdapter(arrayAdapter);
        this.startService(new Intent(this, QuickResponseService.class));
        // TODO Permissions
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            getPermissionToReadContacts();
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
            getPermissionToReadSMS();

        } else {
            //refreshSmsInbox();
        }

        /********   Buttons   **********/

        Button btnLogin = (Button) findViewById(R.id.login);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent go = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(go);
            }
        });

        /*******************************/
    }

    @Override
    public void onStart() {
        super.onStart();
        active = true;
    }

    @Override
    public void onStop() {
        super.onStop();
        active = false;
    }

    public static String getContactName(Context context, String phoneNo) {
        ContentResolver cr = context.getContentResolver();
        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNo));
        Cursor cursor = cr.query(uri, new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME}, null, null, null);
        if (cursor == null) {
            return phoneNo;
        }
        String Name = phoneNo;
        if (cursor.moveToFirst()) {
            Name = cursor.getString(cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME));
        }

        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        return Name;
    }
    /*
        called by ListAdapter Buttons to display that conversation.
     */
    public void displayConversation(View view) {
        Intent intent = new Intent(this, DisplayConversationActivity.class);
        startActivity(intent);
    }
    /*
        called by Add Conversation button to bring up addConversationActivity
        TODO make AddConversationActivity a fragment
    */
    public void addConversation(View view){
        Intent intent = new Intent(this, AddConversationActivity.class);
        startActivity(intent);
    }

    @TargetApi(Build.VERSION_CODES.M)
    public void getPermissionToReadSMS() {
        Log.w(TAG, "getting permissions to read sms in API 23+");
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            if (shouldShowRequestPermissionRationale(
                    android.Manifest.permission.READ_SMS)) {
                Toast.makeText(this, "Please allow permission!", Toast.LENGTH_SHORT).show();
            }
            requestPermissions(new String[]{android.Manifest.permission.READ_SMS},
                    READ_SMS_PERMISSIONS_REQUEST);
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    public void getPermissionToReadContacts() {
        Log.w(TAG, "getting permissions to read contacts in API 23+");
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            if (shouldShowRequestPermissionRationale(
                    android.Manifest.permission.READ_CONTACTS)) {
                Toast.makeText(this, "Please allow permission!", Toast.LENGTH_SHORT).show();
            }
            requestPermissions(new String[]{android.Manifest.permission.READ_CONTACTS},
                    READ_CONTACTS_PERMISSIONS_REQUEST);

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {

        if (requestCode == READ_SMS_PERMISSIONS_REQUEST) {
            if (grantResults.length == 1 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Read SMS permission granted", Toast.LENGTH_SHORT).show();
                //refreshSmsInbox();
            } else {
                Toast.makeText(this, "Read SMS permission denied", Toast.LENGTH_SHORT).show();
            }

        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }

        if (requestCode == READ_CONTACTS_PERMISSIONS_REQUEST) {
            if (grantResults.length == 1 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Read SMS permission granted", Toast.LENGTH_SHORT).show();
                //refreshSmsInbox();
            } else {
                Toast.makeText(this, "Read SMS permission denied", Toast.LENGTH_SHORT).show();
            }

        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}