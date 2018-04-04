package com.epicstudios.messengerpigeon;

import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.SmsManager;
import android.util.Log;
import android.util.Pair;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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
    private static final String TAG = "Presenter:";
    private final Context context;
    private List<String> groups = new ArrayList<>();
    private List<String> groupIDs = new ArrayList<>();
    private ContentResolver cr;
    private SmsManager smsManager;
    //private String phonenum = "5053990094";
    //TO DO private Database =

    /**
     * Initiation of the presenter passing in the context of the activity
     * @param cont context of the Activity creating presenter
     */
    public Presenter(Context cont) {
        Log.i(TAG, "Presenter created.");
        context = cont;
        cr = context.getContentResolver();
        /****************Tests*********************
        groups.add("Group One");
        groups.add("Group Two");
        /*************************************/
        retrieveGroups();
    }

    /**
     * Initiation of the presenter passing in the context of the activity, and group information
     * @param cont context of the Activity creating presenter
     * @param grouptitles list of group titles
     * @param groupids list of group ids
     */
    public Presenter(Context cont, List<String> grouptitles, List<String> groupids){
        Log.i(TAG, "Presenter created.");
        context = cont;
        cr = context.getContentResolver();
        groups = grouptitles;
        groupIDs = groupids;
    }

    public void copyPresenter(Presenter toBe){
        this.groups = toBe.getGroups();
        this.groupIDs = toBe.getGroupIDs();
    }

    /**
     * returns list of all the groups saved on your phone(/google) by name
     * @return list of all the groups saved on your phone(/google) by String name
     */
    public List<String> getGroups(){ return groups; }

    /**
     * returns list of all the groups ID's saved on your phone(/google) by name
     * @return list of all the groups ID's saved on your phone(/google) by String name
     */
    public List<String> getGroupIDs(){ return groupIDs; }

    /**
     * makes list of all the groups saved on your phone(/google) by String name
     */
    private void retrieveGroups() { //TODO DONE
        Uri uri = ContactsContract.Groups.CONTENT_SUMMARY_URI;
        String[] clms = new String[]{ContactsContract.Groups.TITLE, ContactsContract.Groups._ID,
                ContactsContract.Groups.SUMMARY_COUNT,
                ContactsContract.Groups.SUMMARY_WITH_PHONES
        };
        String selection = ContactsContract.Groups.DELETED + "=0";// and " + ContactsContract.Groups.GROUP_VISIBLE + "=1";
        Cursor gcursor = cr.query(uri, clms, selection, null, null);
        if (gcursor != null) {
            if(gcursor.getCount() < 1) {
                groups.add("Found no Groups");
            }
            else{
                while(gcursor.moveToNext()){
                    String title = gcursor.getString(0);
                    if(!(gcursor.getString(3).equals("0"))) {
                        groups.add(title);
                        groupIDs.add(gcursor.getString(1));
                    }
                    //Log.d(TAG+":groups", title+" has "+gcursor.getString(2)+" contacts");
                    //Log.d(TAG+":groups", title+" has "+gcursor.getString(3)+" contacts with phone numbers");
                }
                Log.d(TAG+"groups", "groups retrieved");
            }
        }
        else {
            //TO DO Something went wrong, except it crashes if anything goes wrong here
        }
        if (gcursor != null && !gcursor.isClosed()) {
            gcursor.close();
        }
    }

    /**
     * Retrieves all contacts from a group
     * @param index the index of the group that is being shown
     * @return the list of contacts, with phone numbers, in group
     */
    public ArrayList<Pair<Long, String>> getGroup(int index){
        String title = groups.get(index);
        String id = groupIDs.get(index);
        Log.d(TAG, "groupID = "+id);
        ArrayList<Pair<Long, String>> people = new ArrayList<Pair<Long, String>>();

        final String GROUPMEMBERSHIP = "'"+ContactsContract.CommonDataKinds.GroupMembership.CONTENT_ITEM_TYPE+"'";

        Uri data = ContactsContract.Data.CONTENT_URI;
        String[] clmsD = new String[]{
                ContactsContract.Data.DISPLAY_NAME,
                ContactsContract.Data.IN_VISIBLE_GROUP,
                ContactsContract.Data.CONTACT_ID,
                ContactsContract.Contacts._ID,
                ContactsContract.Data.LOOKUP_KEY,
                ContactsContract.Data.MIMETYPE,
                ContactsContract.Data.DATA1,
        };

        String selection = ContactsContract.Data.HAS_PHONE_NUMBER+"=1 AND "+
                ContactsContract.Data.MIMETYPE+"="+GROUPMEMBERSHIP+" AND "+
                ContactsContract.Data.DATA1+"=?";
        String[] selectionArgs = {id};

        Cursor cursor = cr.query(data, clmsD, selection, selectionArgs, null);

        final int idPos = cursor.getColumnIndex(ContactsContract.Data.CONTACT_ID);
        final int namePos = cursor.getColumnIndex(ContactsContract.Data.DISPLAY_NAME);
        final int dataPos = cursor.getColumnIndex(ContactsContract.Data.DATA1);
        final int mimePos = cursor.getColumnIndex(ContactsContract.Data.MIMETYPE);
        final int lookupPos = cursor.getColumnIndex(ContactsContract.Data.LOOKUP_KEY);
        //final int photoPos = cursor.getColumnIndex(ContactsContract.Data.PHOTO_URI);

        final int CidPos = cursor.getColumnIndex(ContactsContract.Contacts._ID);
        //final int CnamePos = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);

        if (cursor != null) {
            if(cursor.getCount() < 1) {
                Pair<Long, String> temp = new Pair<Long, String>(0L, "Something went Wrong: Found no Contacts in Group "+ title);
                people.add(temp);
            }
            else{
                while(cursor.moveToNext()) {
                    String name = cursor.getString(namePos);
                    long ID = cursor.getLong(idPos);
                    String lookup = cursor.getString(lookupPos);
                    Log.d(TAG+"Contacts", "NAME = "+ name);
                    Log.d(TAG+"Contacts", "CONTACT_ID = "+ID);
                    Log.d(TAG+"Contacts", "_ID = "+cursor.getString(CidPos));
                    Log.d(TAG+"Contacts", "LOOKUP_KEY = "+lookup);
                    Pair<Long, String> temp = new Pair<Long, String>(ID, name);
                    people.add(temp);
                }
            }
        }
        else {
            //TO DO Something went wrong, except if something goes wrong app just crashes
        }

        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }

        /*****************Tests********************
        people.add("Person 1");
        people.add("Person 2");
        /*************************************/
        return people;
    }

    /**
     * addConversation creates a new sms or mms object to be sent
     * @param persons the numbers of the participants
     */
    public void addConversation(List<String> persons) {


    }

    /**
     * Retrieves the contact associated with a phone number
     *
     * @param ID The ID of the contact to search for
     * @return the cursor containing all the info in the data table for this contact
     */
    private Cursor getContactByID(long ID) {
        String SingleSelection = ContactsContract.Data.CONTACT_ID+" = "+ID;
        Cursor SingleCursor = cr.query(ContactsContract.Data.CONTENT_URI, null, SingleSelection, null, null);

        int colm_count = SingleCursor.getColumnCount();
        Log.d(TAG + "SingleCursor", "ColumCount = " + colm_count);
        int display_index = SingleCursor.getColumnIndex("display_name");
        Log.d(TAG + "SingleCursor", "nameIndex = " + display_index);
        String[] colms = SingleCursor.getColumnNames();
        for(String colm:colms){
            Log.d(TAG+"SingleCursor", colm);
        }

        if (SingleCursor != null) {
            if (SingleCursor.getCount() > 0) {
                while(SingleCursor.moveToNext()) {
                    String display_name = SingleCursor.getString(display_index);
                    Log.d(TAG + "SingleCursor", "Name = " + display_name);
                }
            }
        }
        return SingleCursor;
    }
        /*ContentResolver cr = context.getContentResolver();
        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(name));//only for phone#s
        Cursor cursor = cr.query(uri, new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME}, null, null, null);
        if (cursor == null) {
            return Name;
        }
        if (cursor.moveToFirst()) {
            Name = cursor.getString(cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME));
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        return Name;*/

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