package com.epicstudios.messengerpigeon;

import java.util.List;
import java.util.Stack;
import android.util.Pair;

/**
 * Created by Suzy on 3/12/2018.
 * The object that we hold a single correspondence (sms OR mms) in.
 */
public class Conversation {
    private final List<String> numbers;
    //conversation is a stack of a pair of a boolean(0-you, 1-them) and a string(message)
    private Stack<Pair<Boolean, String>> conversation = new Stack<>();

    /**
     * Takes a list of strings of the numbers associated with a set of messages
     */
    public Conversation(List<String> num){ numbers = num; }

    /**
     * addMessage adds a message to the conversation with a tag of sender. Called by listener and
     * the send button.
     * @param sender the boolean representing whether the client in the sender(0) or the receiver (1)
     * @param message the message
     */
    public void addMessage(Boolean sender, String message){
        Pair pair = Pair.create(sender, message);
        conversation.push(pair);
    }

    /**
     * getMessages gives the conversation in a stack
     * @return conversation - the stack of messages
     */
    public Stack getMessages(){ return conversation; }

    /**
     * addSender adds a number to the sender list, allowing for changes in conversation participants.
     * @param num the number to be added
     */
    public void addSender(String num){ numbers.add(num); }

    /**
     * deleteSender deletes a number from the sender list, allowing for changes in conversation participants.
     * @param num the number to be deleted
     */
    public void deleteSedner(String num){ numbers.remove(num); }

    /**
     * getSenders returns the list of participants associated with the conversation.
     * @return numbers - the list of numbers
     */
    public List<String> getSenders(){ return numbers; }
    //TODO deleteMessage(String message){}
}