package com.epicstudios.messengerpigeon;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private Presenter presenter;
    private ListView groups;
    private ArrayAdapter groupAdapter;
    public static boolean active = false;

    private static final String TAG = "MainActivity";
    private SharedPreferences prefs;
    private final String PREF_FILE = "com.epicstudios.messengerpigeon.LOGIN_PREF";
    private static final int READ_CONTACTS_PERMISSIONS_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser == null) {
            //LogOutUser();
        }

        groups = (ListView) findViewById(R.id.peoples);
        prefs = this.getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            getPermissionToReadContacts();
        } else { setup(); }
    }

    private void setup(){
        presenter = new Presenter(this);
        groupAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, presenter.getGroups());
        groups.setAdapter(groupAdapter);
        groups.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                view.setId(i);
                displayGroup(view);
            }
        });
    }

    private void LogOutUser() {
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.apply();
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

        if (item.getItemId() == R.id.login_button)
        {
            LogOutUser();
        }

        if (item.getItemId() == R.id.acc_set_button)
        {
            Intent settingsIntent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(settingsIntent);
        }
        return true;
    }

    /**
     * Called by ListAdapter Buttons to display specific groups.
     * @param view the button
     */
    public void displayGroup(View view) {
        int id = view.getId();
        Intent intent = new Intent(this, DisplayGroupActivity.class);
        intent.putExtra("index", id);
        intent.putExtra("Groups", (ArrayList<String>) presenter.getGroups());
        intent.putExtra("GroupIDs", (ArrayList<String>) presenter.getGroupIDs());
        /*Gson gson = new Gson();
        Log.d(TAG, "gson created");
        String json = gson.toJson(presenter);
        Log.d(TAG, "json created");
        Log.d(TAG, json);
        intent.putExtra("Presenter", json);*/
        startActivity(intent);
    }

    /**
     *  called by Add Conversation button to bring up addConversationActivity
     *  @param view the button
     */
    public void FlappyPigeon(View view){
        Log.d("FlappyPigeon", "making intent");
        Intent launchIntent = new Intent(this, com.epicstudios.messengerpigeon.flappybirdgame.FlappyBirdGame.class);
        if (launchIntent != null) {
            Log.d("FlappyPigeon", "Flappy Success!");
            startActivity(launchIntent);//null pointer check in case package name was not found
        } else { Log.d("FlappyPigeon", "Flappy Failed!"); }
    }

    /**
     * called to get permission to read contacts from the phone.
     * ASync request with onRequestPermissionResult as callback function
     */
    @TargetApi(Build.VERSION_CODES.M)
    private void getPermissionToReadContacts() {
        Log.w(TAG, "getting permissions to read contacts in API 23+");
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            if (shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS)) {
                Toast.makeText(this, "Need permissions to read contacts in order to" +
                        " display both groups and contacts", Toast.LENGTH_LONG).show();
            }
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS},
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
    //TODO check if necessary
    /*public void updateList(final String smsMessage) {
        groupAdapter.insert(smsMessage, 0);
        groupAdapter.notifyDataSetChanged();
    }*/
}