package com.epicstudios.messengerpigeon;



/**
 * Created by chewy on 3/24/2018.
 */

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.telephony.PhoneNumberUtils;

//import com.klinker.android.send_message.Message;

import java.util.List;

import javax.sql.DataSource;

/**
 * Receiver for notifying us when a new MMS has been received by the device. By default it will
 * persist the message to the internal database. We also need to add functionality for
 * persisting it to our own database and giving a notification that it has been received.
 */
public class MmsReceiverReceiver extends com.klinker.android.send_message.MmsReceivedReceiver {

    private Long conversationId = null;

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
/*
        String nullableOrBlankBodyText = insertMms(context);

        if (nullableOrBlankBodyText != null && !nullableOrBlankBodyText.isEmpty() && conversationId != null) {
            Intent mediaParser = new Intent(context, MediaParserService.class);
            mediaParser.putExtra(MediaParserService.EXTRA_CONVERSATION_ID, conversationId);
            mediaParser.putExtra(MediaParserService.EXTRA_BODY_TEXT, nullableOrBlankBodyText.trim());
            new Handler().postDelayed(() -> context.startService(mediaParser), 2000);
        }

        context.startService(new Intent(context, NotificationService.class));*/
    }

    /*private String insertMms(Context context) {
        Cursor lastMessage = SmsMmsUtils.getLastMmsMessage(context);

        String snippet = "";
        if (lastMessage != null && lastMessage.moveToFirst()) {
            Uri uri = Uri.parse("content://mms/" + lastMessage.getLong(0));
            final String from = SmsMmsUtils.getMmsFrom(uri, context);

            if (BlacklistUtils.isBlacklisted(context, from)) {
                return null;
            }

            final String to = SmsMmsUtils.getMmsTo(uri, context);
            final String phoneNumbers = getPhoneNumbers(from, to,
                    PhoneNumberUtils.getMyPhoneNumber(context), context);
            List<ContentValues> values = SmsMmsUtils.processMessage(lastMessage, -1L, context);

            DataSource source = DataSource.getInstance(context);
            source.open();

            for (ContentValues value : values) {
                Message message = new Message();
                message.type = value.getAsInteger(Message.COLUMN_TYPE);
                message.data = value.getAsString(Message.COLUMN_DATA).trim();
                message.timestamp = value.getAsLong(Message.COLUMN_TIMESTAMP);
                message.mimeType = value.getAsString(Message.COLUMN_MIME_TYPE);
                message.read = false;
                message.seen = false;
                message.from = ContactUtils.findContactNames(from, context);
                message.simPhoneNumber = DualSimUtils.get(context).getAvailableSims().isEmpty() ? null : to;

                if (message.mimeType.equals(MimeType.TEXT_PLAIN)) {
                    snippet = message.data;
                }

                if (phoneNumbers.split(", ").length == 1) {
                    message.from = null;
                }

                if (SmsReceivedReceiver.shouldSaveMessages(source, message)) {
                    conversationId = source.insertMessage(message, phoneNumbers, context);

                    if (MmsSettings.get(context).autoSaveMedia &&
                            !MimeType.TEXT_PLAIN.equals(message.mimeType)) {
                        new MediaSaver(context).saveMedia(message);
                    }
                }
            }

            source.close();

            if (conversationId != null) {
                ConversationListUpdatedReceiver.sendBroadcast(context, conversationId,
                        snippet, false);
                MessageListUpdatedReceiver.sendBroadcast(context, conversationId);
            }
        }

        try {
            lastMessage.close();
        } catch (Exception e) { }

        return snippet;
    }

    @VisibleForTesting
    protected String getPhoneNumbers(String from, String to, String myNumber, Context context) {
        String[] toNumbers = to.split(", ");
        StringBuilder builder = new StringBuilder();
        String myName = getMyName(context);

        for (String number : toNumbers) {
            String cleanNumber = PhoneNumberUtils.clearFormatting(number);
            String myCleanNumber = PhoneNumberUtils.clearFormatting(myNumber);
            String contactName = ContactUtils.findContactNames(number, context);

            if (!cleanNumber.contains(myCleanNumber) && !myCleanNumber.contains(cleanNumber) && !contactName.equals(myName)) {
                builder.append(number);
                builder.append(", ");
            }
        }

        builder.append(from);
        return builder.toString();
    }

    @VisibleForTesting
    protected String getMyName(Context context) {
        return Account.get(context).myName;
    }

    @VisibleForTesting
    protected String getContactName(Context context, String number) {
        return ContactUtils.findContactNames(number, context);
    }
*/
}