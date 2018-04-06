package com.epicstudios.messengerpigeon;

import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.provider.ContactsContract;
import android.telephony.SmsManager;
import android.util.Log;
import android.util.Pair;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class Presenter {
    private static final String TAG = "Presenter:";
    private final Context context;
    private List<String> groups = new ArrayList<>();
    private List<String> groupIDs = new ArrayList<>();
    private HashMap<Long, String> ContactPhotos;
    private ContentResolver cr;
    private SmsManager smsManager;

    /**
     * Initiation of the presenter passing in the context of the activity
     * @param cont context of the Activity creating presenter
     */
    Presenter(Context cont) {
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
     * @param groupTitles list of group titles
     * @param groupIds list of group ids
     */
    Presenter(Context cont, List<String> groupTitles, List<String> groupIds){
        Log.i(TAG, "Presenter created.");
        context = cont;
        cr = context.getContentResolver();
        groups = groupTitles;
        groupIDs = groupIds;
    }
    /**
    public void copyPresenter(Presenter toBe){
        this.groups = toBe.getGroups();
        this.groupIDs = toBe.getGroupIDs();
    }*/

    /**
     * returns list of all the groups saved on your phone(/google) by name
     * @return list of all the groups saved on your phone(/google) by String name
     */
    List<String> getGroups(){ return groups; }

    /**
     * returns list of all the groups ID's saved on your phone(/google) by name
     * @return list of all the groups ID's saved on your phone(/google) by String name
     */
    List<String> getGroupIDs(){ return groupIDs; }

    /**
     * makes list of all the groups saved on your phone(/google) by String name
     */
    private void retrieveGroups() { //TODO DONE
        Uri uri = ContactsContract.Groups.CONTENT_SUMMARY_URI;
        String[] clms = new String[]{ContactsContract.Groups.TITLE, ContactsContract.Groups._ID,
                //ContactsContract.Groups.SUMMARY_COUNT,
                ContactsContract.Groups.SUMMARY_WITH_PHONES
        };
        String selection = ContactsContract.Groups.DELETED + "=0";// and " + ContactsContract.Groups.GROUP_VISIBLE + "=1";
        Cursor gcursor = cr.query(uri, clms, selection, null, null);

        if (gcursor != null) {

            final int titlePos = gcursor.getColumnIndex(ContactsContract.Groups.TITLE);
            final int idPos = gcursor.getColumnIndex(ContactsContract.Groups._ID);
            //final int countPos = gcursor.getColumnIndex(ContactsContract.Groups.SUMMARY_COUNT);
            final int phoneCountPos = gcursor.getColumnIndex(ContactsContract.Groups.SUMMARY_WITH_PHONES);

            if (gcursor.getCount() < 1) {
                groups.add("Found no Groups");
                groupIDs.add("NA");
            } else {
                while (gcursor.moveToNext()) {
                    String title = gcursor.getString(titlePos);
                    if (!(gcursor.getString(phoneCountPos).equals("0"))) {
                        groups.add(title);
                        groupIDs.add(gcursor.getString(idPos));
                    }
                }
                Log.d(TAG + "groups", "groups retrieved");
            }
        } else {
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
    ArrayList<Pair<Long, String>> getGroup(int index){ //TODO DONE
        String title = groups.get(index);
        String id = groupIDs.get(index);
        Log.d(TAG, "groupID = "+id);

        ArrayList<Pair<Long, String>> people = new ArrayList<Pair<Long, String>>();
        ContactPhotos = new HashMap();

        //final String PHOTO = ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE;
        final String GROUPMEMBERSHIP = ContactsContract.CommonDataKinds.GroupMembership.CONTENT_ITEM_TYPE;

        Uri data = ContactsContract.Data.CONTENT_URI;
        String[] clms = new String[]{
                ContactsContract.Data.DISPLAY_NAME,
                ContactsContract.Data.CONTACT_ID,
                ContactsContract.Data.PHOTO_URI,
        };
        String[] selectionArgs = {id};
        /*String selection = ContactsContract.Data.HAS_PHONE_NUMBER+"=1 AND (("+
                ContactsContract.Data.MIMETYPE+"='"+GROUPMEMBERSHIP+"' AND "+
                ContactsContract.Data.DATA1+"=?) OR "+
                ContactsContract.Data.MIMETYPE+"='"+PHOTO+"')";*/
        String selection = ContactsContract.Data.HAS_PHONE_NUMBER+"=1 AND "+
                ContactsContract.Data.MIMETYPE+"='"+GROUPMEMBERSHIP+"' AND "+
                ContactsContract.Data.DATA1+"=?";
        Cursor cursor = cr.query(data, clms, selection, selectionArgs, null);

        if (cursor != null) {

            final int idPos = cursor.getColumnIndex(ContactsContract.Data.CONTACT_ID);
            final int namePos = cursor.getColumnIndex(ContactsContract.Data.DISPLAY_NAME);
            final int photoURIPos = cursor.getColumnIndex(ContactsContract.Data.PHOTO_URI);

            if(cursor.getCount() < 1) {
                Pair<Long, String> temp = new Pair<Long, String>(0L, "Something went Wrong: Found no Contacts in Group "+ title);
                people.add(temp);
            }
            else{
                while(cursor.moveToNext()) {
                    long ID = cursor.getLong(idPos);
                    String name = cursor.getString(namePos);

                    Log.d(TAG+"Contacts", "NAME = "+ name);
                    Log.d(TAG+"Contacts", "CONTACT_ID = "+ID);
                    Log.d(TAG+"ContactsPhoto", "PHOTO_URI = "+cursor.getString(photoURIPos));

                    Pair<Long, String> temp = new Pair<>(ID, name);
                    people.add(temp);
                    if(cursor.getString(photoURIPos) != null) {  ContactPhotos.put(ID, cursor.getString(photoURIPos));}
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

    HashMap getPhotos(){
        final String tag = "getPhoto";
        HashMap<Long, Bitmap> map = new HashMap();
        Set<Long> keys = ContactPhotos.keySet();
        Log.d(TAG+tag, "getPhoto() with set "+ keys.toString());
        String selection = " AND "+ContactsContract.Data.MIMETYPE+" = "+ContactsContract.CommonDataKinds.Photo.PHOTO;
        String[] projection = {ContactsContract.CommonDataKinds.Photo.PHOTO};
        for(Long item:keys){
            Cursor cursor = getCursor(item, null, "");
            final int index = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Photo.PHOTO);
            final int mimeType = cursor.getColumnIndex(ContactsContract.Data.MIMETYPE);

            if(cursor.moveToFirst()) {
                Log.d(TAG + tag, "Cursor_position_move = " + cursor.getPosition());
                Log.d(TAG + tag, "mimeType = " + cursor.getString(mimeType));
                Log.d(TAG + tag, "index_name = " + cursor.getColumnName(index));
                byte[] bytes = cursor.getBlob(index);
                Bitmap bytesMap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                map.put(item, bytesMap);
            }
        }
        return map;
    }

    /**
     * addConversation creates a new sms or mms object to be sent
     * @param people the numbers of the participants
     */
    public void addConversation(List<Pair<Long, String>> people) {
        List<String> phonNums = new ArrayList<>();
        final int main = ContactsContract.CommonDataKinds.Phone.TYPE_MAIN;
        final int mobile = ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE;
        final int other = ContactsContract.CommonDataKinds.Phone.TYPE_OTHER;
        final int custom = ContactsContract.CommonDataKinds.Phone.TYPE_CUSTOM;
        final int wmobile = ContactsContract.CommonDataKinds.Phone.TYPE_WORK_MOBILE;
        final int pager = ContactsContract.CommonDataKinds.Phone.TYPE_PAGER; //?

        for(Pair person: people) {
            Cursor c = cr.query(ContactsContract.Data.CONTENT_URI,
                    new String[]{ContactsContract.Data._ID, ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.LABEL},
                    ContactsContract.Data.CONTACT_ID + "=?" + " AND "
                            + ContactsContract.Data.MIMETYPE + "='" + ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE + "'",
                    new String[]{String.valueOf(person.first)}, null);
            if(c != null) {
                c.moveToFirst();
                int type = c.getInt(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));
                Log.d(TAG+"numbers", "Type = "+type);
                phonNums.add(c.getString(c.getColumnIndex(ContactsContract.Data.DATA1)));
            }
        }
        Log.d(TAG+"numbers", "phonenumbers = "+phonNums.toString());
        for(String num: phonNums) { send(num); }
    }

    /**
     * Retrieves the contact associated with a phone number
     * @param ID The ID of the contact to search for
     * @return the cursor containing all the info in the data table for this contact
     */
    private Cursor getCursor(long ID, String[] projection, String selection) {
        String SingleSelection = ContactsContract.Data.CONTACT_ID+" = "+ID+selection;
        Uri data = ContactsContract.Data.CONTENT_URI;
        Cursor SingleCursor = cr.query(data, projection, SingleSelection, null, null);

        Log.d(TAG + "SingleCursor", "CursorCount = " + SingleCursor.getColumnCount());
        for(String name:SingleCursor.getColumnNames()){
            Log.d(TAG + "SingleCursor", "ColumNames = " +name);
        }
        Log.d(TAG + "SingleCursor", "ColumCount = " + SingleCursor.getCount());

        int display_index = SingleCursor.getColumnIndex("display_name");
        Log.d(TAG + "SingleCursor", "nameIndex = " + display_index);
        //SingleCursor.moveToFirst();
        //Log.d(TAG + "SingleCursor", "name = " + SingleCursor.getString(display_index));
        /*
        int mimeTypePos = SingleCursor.getColumnIndex(ContactsContract.Data.MIMETYPE);
        if (SingleCursor != null) {
            if (SingleCursor.getCount() > 0) {
                while(SingleCursor.moveToNext()) {
                    String display_name = SingleCursor.getString(display_index);
                    Log.d(TAG + "SingleCursor", "Name = " + display_name);
                    String mimeType = SingleCursor.getString(mimeTypePos);
                }
            }
        }*/
        return SingleCursor;
    }

    public void testCursor(){
        final String tag = "TestCursor:";
        Uri Results;
        Cursor cursor = context.getContentResolver().query(ContactsContract.Contacts.CONTENT_URI,
                new String[] {ContactsContract.Contacts.LOOKUP_KEY}, null, null, null);
        if (cursor != null) {
            try {
                StringBuilder uriListBuilder = new StringBuilder();
                int index = 0;
                while (cursor.moveToNext()) {
                    if (index != 0) uriListBuilder.append(':');
                    uriListBuilder.append(cursor.getString(0));
                    index++;
                }
                Results = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_MULTI_VCARD_URI,
                        Uri.encode(uriListBuilder.toString()));
                Log.d(TAG+tag, "Results String = "+ Results.toString());
                Log.d(TAG+tag, "Results EncodedPath= "+ Results.getEncodedPath());
                Log.d(TAG+tag, "Results Path = "+ Results.getPath());
                Log.d(TAG+tag, "Results EncodedFrag= "+ Results.getEncodedFragment());
                Log.d(TAG+tag, "Results PathSegments = "+Results.getPathSegments());
            } catch (Exception e){ }
            cursor.close();
        }
    }
        /*getContactName(phonenumber){
        ContentResolver cr = context.getContentResolver();
        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phonenumber));//only for phone#s
        Cursor cursor = cr.query(uri, new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME}, null, null, null);
        if (cursor == null) {
            return phonenumber;
        }
        if (cursor.moveToFirst()) {
            phonenumber = cursor.getString(cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME));
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        return phonenumber;*/

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