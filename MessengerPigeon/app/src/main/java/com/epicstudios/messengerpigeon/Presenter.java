package com.epicstudios.messengerpigeon;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;
import com.google.gson.Gson;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

public class Presenter {
    private static final String TAG = "Presenter";
    private List<Conversation> conversations = new LinkedList<>();
    private static final String SHARED_PREF_FILE = "epicstudios.messengerpigeon.PREF_FILE";
    private Gson json = new Gson();
    //TODO private Database =

    /**
     * Initiation of the presenter passing in the conversation list
     * @param conversations list of conversations
     */
    public Presenter(List<Conversation> conversations){
        this.conversations = conversations;
        Log.i(TAG, "Presenter created.");
    }

    /**
     * addConversation creates a conversation object for the new conversation and adds it to
     * our conversation list.
     * @param persons the numbers of the participants
     * @param message the message that initiated the need for a new conversation
     * @param incoming determine whether the client or other sent the message (for display purposes)
     */
    public void addConversation(List<String> persons, String message, Boolean incoming) {
        Conversation temp = new Conversation(persons);
        temp.addMessage(incoming, message);
        conversations.add(temp);
        Log.i(TAG, "Succesfully added a conversation");
    }

    /**
     * getConversations gives the list of all ongoing conversations.
     * @return the list of conversations in order from most recent to least.
     */
    public List<Conversation> getConversations() {
        return conversations;
    }

    /**
     * retrieves messages associated with a conversation
     * @param conv the conversation to retrieve messages from
     * @return return the messages in a stack
     */
    public Stack getMessages(Conversation conv){
        int index = conversations.indexOf(conv);
        if(index == -1){ return null; }
        Conversation conversation = conversations.get(index);
        return conversation.getMessages();
    }

    /**
     * deleteConversation deletes a conversation from the conversation list
     * @param conv the conversation to be deleted
     */
    public Boolean deleteConversation(Conversation conv) {
        int index = conversations.indexOf(conv);
        if(index == -1){ return false; }
        conversations.remove(index);
        Log.i(TAG, "Successfully deleted a conversation.");
        return true;
    }

    /**
     * called to save conversation state.
     * @param cnt the context of the activity of caller   ????
     */
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
     */
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

    /**
     * Retrieves the contact associated with a phone number
     * @param context the context of the activity of caller   ????
     * @param phoneNo The number to search for
     * @return Contact Name
     */
    private static String getContactName(Context context, String phoneNo) {
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