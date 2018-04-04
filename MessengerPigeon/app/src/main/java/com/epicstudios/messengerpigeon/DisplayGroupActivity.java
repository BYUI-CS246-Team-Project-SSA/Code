package com.epicstudios.messengerpigeon;

import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.EditText;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class DisplayGroupActivity extends AppCompatActivity {

    private final String TAG = "DisplayGroupActivity";
    private List<String> listofPeople;
    private ListView people;
    private ArrayAdapter arrayAdapter;
    private Presenter presenter;
    //private EditText input;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);
        //input = (EditText) findViewById(R.id.input);*/

        Bundle extras = getIntent().getExtras();
        List<String> temp = extras.getStringArrayList("Groups");
        List<String> tempIDs = extras.getStringArrayList("GroupIDs");
        presenter = new Presenter(this, temp, tempIDs);
        listofPeople = presenter.getGroup(extras.getInt("index"));

        people = (ListView) findViewById(R.id.peoples);
        //arrayAdapter = new ArrayAdapter<>(this, R.layout.contact_display, *viewID* , listofPeople);
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.select_dialog_multichoice, listofPeople);
        people.setAdapter(arrayAdapter);

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            //getPermissionToReadContacts(); This should be in a fragment
        }
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
            //getPermissionToReadSMS(); This should be in a fragment
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        /*active = true;
        inst = this;*/
    }

    @Override
    public void onStop() {
        super.onStop();
        //active = false;
    }

    /**
     * checks for permissions then calls the send function in presenter
     * @param view the button
     */
    public void onSendClick(View view) {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            //getPermissionToReadSMS(); This should be in a fragment activity
        }
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.SEND_SMS)//TODO mms
                != PackageManager.PERMISSION_GRANTED) {
            //getPermissionToReadSMS(); This should be in a fragment activity
        }
    }


    //TODO check if necessary
    public void updateList(final String smsMessage) {
        arrayAdapter.insert(smsMessage, 0);
        arrayAdapter.notifyDataSetChanged();
    }

    //TODO Remove both refreshInbox() and getContactName()
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