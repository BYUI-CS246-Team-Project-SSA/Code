package com.epicstudios.messengerpigeon;

import android.util.Log;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by chewy on 3/12/2018.
 */

public class Presenter {
    private static final String TAG = "Presenter";
    private LinkedList<Conversation> conversations;
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
     * @param sender determine whether the client or other sent the message (for display purposes)
     */
    private void addConversation(List<String> persons, String message, Boolean sender) {
        Conversation temp = new Conversation(persons);
        temp.addMessage(sender, message);
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
    private void deleteConversation(List<String> persons) {
        Log.i(TAG, "Successfully deleted a conversation.");
    }
}