package com.epicstudios.messengerpigeon;

import android.content.Context;
import android.util.Log;
import com.google.gson.Gson;
import java.io.FileOutputStream;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by chewy on 3/12/2018.
 */

public class Presenter {
    private static final String TAG = "Presenter";
    private LinkedList<Conversation> conversations;
    private Gson json = new Gson();
    //TODO private Database =

    /**
     * Initiation of the presenter passing in the conversation list
     * @param conversations list of conversations
     */
    public Presenter(LinkedList<Conversation> conversations){
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
    public LinkedList<Conversation> getConversations() {
        return conversations;
    }

    /**
     * deleteConversation deletes a conversation from the conversation list
     * @param persons the
     */
    public void deleteConversation(List<String> persons) {
        Log.i(TAG, "Successfully deleted a conversation.");
    }

    /**
     * called to save conversation state.
     * @param cnt the context of the app, passed by caller   ????
     */
    public void writetoFile(Context cnt, LinkedList<Conversation> convers){
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
     * @return the L
     */
    /*public LinkedList<Conversation> readfromFile(Context cnt){
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
        json.fromJson(fileContents, LinkedList<Conversation>);
        json.fromJson(fileContents, Conversations);
        return null;
    }*/
}