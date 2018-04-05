package com.epicstudios.messengerpigeon;

import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DisplayGroupActivity extends AppCompatActivity {

    private final String TAG = "DisplayGroupActivity";
    private ListView people;
    private contactAdapter contactAdapter;
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
        int index = extras.getInt("index");
        final ArrayList<Pair<Long, String>> listPeople = presenter.getGroup(index);
        Log.d(TAG, "listPeople: "+ listPeople.toString());

        people = (ListView) findViewById(R.id.peoples);
        contactAdapter = new contactAdapter(this, R.layout.contact_display, listPeople);
        //contactAdapter = new contactAdapter(this, android.R.layout.select_dialog_multichoice, listPeople);
        people.setAdapter(contactAdapter);

        ArrayAdapter arrayAdapter;
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.select_dialog_multichoice, listPeople);
        //arrayAdapter = new ArrayAdapter<>(this, R.layout.contact_display, listPeople);
        //people.setAdapter(arrayAdapter);

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
     *
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
        //TODO presenter.addConversation();
    }


    //TODO check if necessary
    public void updateList(final String smsMessage) {
        //contactAdapter.insert(smsMessage, 0);
        //contactAdapter.notifyDataSetChanged();
    }
}