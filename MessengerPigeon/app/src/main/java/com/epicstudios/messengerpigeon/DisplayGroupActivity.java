package com.epicstudios.messengerpigeon;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class DisplayGroupActivity extends AppCompatActivity {

    private final String TAG = "DisplayGroupActivity";
    private ListView people;
    private ContactAdapter contactAdapter;
    private Presenter presenter;
    private EditText input;
    ArrayList<Pair<Long, String>> listPeople;

    public DisplayGroupActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);
        input = (EditText) findViewById(R.id.input);

        Bundle extras = getIntent().getExtras();
        List<String> temp = extras.getStringArrayList("Groups");
        List<String> tempIDs = extras.getStringArrayList("GroupIDs");
        presenter = new Presenter(this, temp, tempIDs);
        int index = extras.getInt("index");
        listPeople = presenter.getGroup(index);
        Log.d(TAG, "listPeople: "+ listPeople.toString());

        people = (ListView) findViewById(R.id.peoples);
        contactAdapter = new ContactAdapter(this, R.layout.contact_display, listPeople, presenter.getPhotos());
        people.setAdapter(contactAdapter);

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            //getPermissionToReadContacts(); This should be in a fragment
        }
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
        //TODO presenter.addConversation();
        presenter.addConversation(listPeople);

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.SEND_SMS)//TODO mms
                != PackageManager.PERMISSION_GRANTED) {
            //getPermissionToReadSMS(); This should be in a fragment activity
        }

    }
}