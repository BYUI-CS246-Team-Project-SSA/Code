package com.epicstudios.messengerpigeon;

import android.annotation.TargetApi;
import android.support.annotation.NonNull;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class DisplayConversationActivity extends AppCompatActivity {

    private String phonenum = "5053990094";
    private ArrayList<String> smsMessagesList = new ArrayList<>();
    private ListView messages;
    private ArrayAdapter arrayAdapter;
    private EditText input;
    private SmsManager smsManager = SmsManager.getDefault();
    private static DisplayConversationActivity inst;
    public static boolean active = false;
    public static DisplayConversationActivity instance() {
        return inst;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_conversation);
        messages = (ListView) findViewById(R.id.conversations);
        input = (EditText) findViewById(R.id.input);
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, smsMessagesList);
        messages.setAdapter(arrayAdapter);
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            //getPermissionToReadContacts(); This should be in a fragment
        }
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
            //getPermissionToReadSMS(); This should be in a fragment

        } else {
            refreshSmsInbox();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        active = true;
        inst = this;
    }

    public void updateInbox(final String smsMessage) {
        arrayAdapter.insert(smsMessage, 0);
        arrayAdapter.notifyDataSetChanged();
    }

    public void onSendClick(View view) {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            //getPermissionToReadSMS(); This should be in a fragment activity
        } else {
            smsManager.sendTextMessage(phonenum, null, input.getText().toString(), null, null);
            Toast.makeText(this, "Message sent!", Toast.LENGTH_SHORT).show();
            input.setText("");
        }
    }

    public void refreshSmsInbox() {
        ContentResolver contentResolver = getContentResolver();
        Cursor smsInboxCursor = contentResolver.query(Uri.parse("content://sms/inbox"), null, null, null, null);
        int indexBody = smsInboxCursor.getColumnIndex("body");
        int indexAddress = smsInboxCursor.getColumnIndex("address");
        if (indexBody < 0 || !smsInboxCursor.moveToFirst()) return;
        arrayAdapter.clear();
        do {
            String str = "SMS From: " + getContactName(this, smsInboxCursor.getString(indexAddress)) +
                    "\n" + smsInboxCursor.getString(indexBody) + "\n";
            //String str = "SMS From: " + smsInboxCursor.getString(indexAddress) +
            //"\n" + smsInboxCursor.getString(indexBody) + "\n"; to sort by contact
            // if (smsInboxCursor.getString(indexAddress).equals("PHONE NUMBER HERE")) {
            arrayAdapter.add(str);
            //  }
        } while (smsInboxCursor.moveToNext());
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
}