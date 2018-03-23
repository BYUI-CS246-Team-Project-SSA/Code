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
import android.provider.Telephony;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Presenter presenter;
    private List<Conversation> conversationList = new LinkedList<>();
    private ListView conversations;
    private ArrayAdapter arrayAdapter;
    private SharedPreferences prefs;
    public static boolean active = false;
    private List<String> persons = new ArrayList<>();

    private static final String TAG = "MainActivity";
    private static final int READ_SMS_PERMISSIONS_REQUEST = 1;
    private static final int READ_CONTACTS_PERMISSIONS_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.startService(new Intent(this, QuickResponseService.class));
        presenter = new Presenter(conversationList);
        ////////////////////////// testing //////////////////////////////
        persons.add("Only for now");
        presenter.addConversation(persons, "Testing Testing", true);
        persons.add("Testing a second");
        presenter.addConversation(persons, "Testing Testing", false);
        ////////////////////////////////////////////////////////////////
        conversationList = presenter.getConversations();
        conversations = (ListView) findViewById(R.id.conversations);
        conversations.setOnItemClickListener(new AdapterView.OnItemClickListener() {
             @Override
             public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                 displayConversation(view);
             }
         });
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

    /**
     * Called by Login button to go to Login Activity
     * @param view the button
     */
    public void displaySignIn(View view) {
        Intent go = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(go);
    }

    /**
     * Called by ListAdapter Buttons to display specific conversation.
     * @param view the button
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
    /*
    private void getAllSmsMessages() {
        Context context = this;
        Cursor cursor = context.getContentResolver().query(Telephony.Sms.CONTENT_URI, null, mSelectionClause, mSelectionArgs, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    int threadId = cursor.getInt(cursor.getColumnIndex(Telephony.Sms.THREAD_ID));
                    String messageId = cursor.getString(cursor.getColumnIndex(Telephony.Sms._ID));
                    String message = cursor.getString(cursor.getColumnIndex(Telephony.Sms.BODY));
                    int type = cursor.getInt(cursor.getColumnIndex(Telephony.Sms.TYPE));
                    long date = cursor.getLong(cursor.getColumnIndex(Telephony.Sms.DATE));
                    App.getDataBaseManager().saveMessage(new SmsMmsMessage(threadId, messageId,
                            message, type, date, null, null));
                } while (cursor.moveToNext());
            }
        } finally {
            cursor.close();
        }
    }*/
}