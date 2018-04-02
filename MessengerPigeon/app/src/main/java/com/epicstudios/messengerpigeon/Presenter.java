package com.epicstudios.messengerpigeon;

import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.UserDictionary;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/*
import com.klinker.android.logger.Log;
import com.klinker.android.logger.OnLogListener;
import com.klinker.android.send_message.ApnUtils;
import com.klinker.android.send_message.BroadcastUtils;
import com.klinker.android.send_message.Message;
import com.klinker.android.send_message.Transaction;
import com.klinker.android.send_message.Utils;
import com.klinker.android.send_message.Settings;

import android.graphics.BitmapFactory;
*/
public class Presenter {
    private static final String TAG = "Presenter";
    private final Context context;
    private List<String> groups = new ArrayList<>();

    //private String phonenum = "5053990094";
    private SmsManager smsManager;
    //TO DO private Database =

    /**
     * Initiation of the presenter passing in the context of the activity
     */
    public Presenter(Context cont) {
        Log.i(TAG, "Presenter created.");
        context = cont;
        groups.add("Test One");
        groups.add("Test Two");
    }

    public void copyGroups(List<String> groupSet){
        groups = groupSet;
    }

    public List<String> getGroup(int index){
        List<String> people = new ArrayList<>();

        return people;
    }

    public void copyPresenter(Presenter toBe){
        this.groups = toBe.getGroups();
    }

    /**
     * addConversation creates a new sms or mms object to be sent
     *
     * @param persons the numbers of the participants
     */
    public void addConversation(List<String> persons) {

    }

    /**
     * returns list of all the groups saved on your phone(/google) by name
     * @return list of all the groups saved on your phone(/google) by String name
     */
    public List<String> getGroups(){ return groups; }

    private void retrieveGroups() {
       /* ContentResolver cr = context.getContentResolver();
        Uri uri = Uri.withAppendedPath(ContactsContract.Groups);
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
        return Name;*/
    }
    /**
     * Retrieves the contact associated with a phone number
     *
     * @param phoneNo The number to search for
     * @return Contact Name
     */
    private String getContactName(String phoneNo) {
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

    public void send(String person){
        String textToSend = " has started a conversation"; //TODO add users name
        smsManager.sendTextMessage(person, null, textToSend, null, null);
        Toast.makeText(context, "Message sent!", Toast.LENGTH_SHORT).show();
    }

    public void send(List<String> people){
        String textToSend = " has started a conversation"; //TODO add users name
        Uri contentUri;
        String locationUrl;
        Bundle configOverrides;
        PendingIntent sentIntent;
        new Thread(new Runnable() {
            @Override
            public void run() {
                //smsManager.sendMultimediaMessage(context, contentUri, locationUrl, configOverrides, sentIntent);
            }
        }).start();
    }
}
/*
import com.google.gson.Gson;
import java.io.FileInputStream;
import java.io.FileOutputStream;

private static final String SHARED_PREF_FILE = "epicstudios.messengerpigeon.PREF_FILE";
private Gson json = new Gson();

    /**
     * called to save conversation state.
     * @param cnt the context of the activity of caller   ????
     *
    public void writetoFile(Context cnt, List<Conversation> convers){
        String filename = "myConversations";
        cnt.deleteFile(filename);
        String fileContents = json.toJson(convers);
        FileOutputStream outputStream;
        try {
            outputStream = cnt.openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(fileContents.getBytes());
            outputStream.close();
        } catch (Exception e) {
            Log.e(TAG+"writetoFile()", e.getMessage());
        }
    }

    /**
     * called to pull the conversation list from the file
     * @param cnt the context of the app, passed by caller    ???
     * @return the List of all the conversations saved in file
     *
    public List<Conversation> readfromFile(Context cnt){
        String filename = "myConversations";
        String fileContents = "";
        FileInputStream inputStream;
        try {
            inputStream = cnt.openFileInput(filename);
            inputStream.read();
            inputStream.close();
        } catch (Exception e) {
            Log.e(TAG+"readfromFile()", e.getMessage());
        }
        //json.fromJson(fileContents, conversation);
        return null;
    }
*/