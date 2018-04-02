package com.epicstudios.messengerpigeon;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private Presenter presenter;
    private List<String> groupList;
    private List<String> peopleList = new ArrayList<>();
    private ListView groups;
    private ArrayAdapter groupAdapter;
    private SharedPreferences prefs;
    public static boolean active = false;

    private static final String TAG = "MainActivity";
    private static final int READ_CONTACTS_PERMISSIONS_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();

        presenter = new Presenter(this);
        groupList = presenter.getGroups();
        groupAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, groupList);
        groups = (ListView) findViewById(R.id.peoples);
        groups.setAdapter(groupAdapter);
        groups.setOnItemClickListener(new AdapterView.OnItemClickListener() {
             @Override
             public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                 view.setId(i);
                 displayGroup(view);
             }
         });

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            getPermissionToReadContacts();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        active = true;

        FirebaseUser currentUser = auth.getCurrentUser();

        if (currentUser == null) {
            LogOutUser();
        }
    }

    private void LogOutUser() {
        Intent mainIntent = new Intent(
                MainActivity.this, LoginActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        if (item.getItemId() == R.id.logout_button)
        {
            auth.signOut();
            LogOutUser();
        }

        if (item.getItemId() == R.id.acc_set_button)
        {
            Intent settingsIntent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(settingsIntent);
        }
        return true;
    }

    @Override
    public void onStop() {
        super.onStop();
        active = false;
    }

    /**
     * Called by ListAdapter Buttons to display specific groups.
     * @param view the button
     */
    public void displayGroup(View view) {
        int id = view.getId();
        Log.i(TAG, groupList.get(id));
        /////////////////////////////////////////////////////
       /* peopleList.add("Person 1");
        peopleList.add("Person 2");
        ////////////////////////////////////////////////////
        ArrayAdapter peopleAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, peopleList);
        groups.setAdapter(peopleAdapter);*/

        Intent intent = new Intent(this, DisplayGroupActivity.class);
        intent.putExtra("Group", groupList.get(id));
        intent.putExtra("Presenter", new Gson().toJson(presenter));
        startActivity(intent);
    }

    /**
     *  called by Add Conversation button to bring up addConversationActivity
     *  @param view the button
     */
    public void addConversation(View view){
        Intent intent = new Intent(this, AddConversationActivity.class);
        startActivity(intent);
    }

    /**
     * called to get permission to read contacts from the phone.
     * ASync request with onRequestPermissionResult as callback function
     */
    @TargetApi(Build.VERSION_CODES.M)
    public void getPermissionToReadContacts() {
        Log.w(TAG, "getting permissions to read contacts in API 23+");
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            if (shouldShowRequestPermissionRationale(android.Manifest.permission.READ_CONTACTS)) {
                Toast.makeText(this, "Please allow permission!", Toast.LENGTH_SHORT).show();
            }
            requestPermissions(new String[]{android.Manifest.permission.READ_CONTACTS},
                    READ_CONTACTS_PERMISSIONS_REQUEST);
        }
    }

    /**
     * called by the permissions request when done processing
     * @param requestCode code associated with initial request
     * @param permissions ???
     * @param grantResults value indicating the results of the request
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        if (requestCode == READ_CONTACTS_PERMISSIONS_REQUEST) {
            if (grantResults.length == 1 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                /** parent.getContext() **/
                Toast.makeText(this, "Read Contacts permission granted", Toast.LENGTH_SHORT).show();
                //refreshSmsInbox();
            } else {
                Toast.makeText(this, "Read Contacts permission denied", Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
    //TODO check if necessary
    public void updateList(final String smsMessage) {
        groupAdapter.insert(smsMessage, 0);
        groupAdapter.notifyDataSetChanged();
    }
}