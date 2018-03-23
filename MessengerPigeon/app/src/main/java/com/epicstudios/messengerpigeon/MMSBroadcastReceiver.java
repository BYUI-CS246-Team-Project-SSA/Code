package com.epicstudios.messengerpigeon;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
//import com.android.mms.transaction.TransactionSettings;

/**
 * Created by chewy on 2/28/2018.
 */


public class MMSBroadcastReceiver extends BroadcastReceiver {

    private ConnectivityManager connectivityManager;
    //private TransactionSettings transactionSettings;

    public MMSBroadcastReceiver() {

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        //transactionSettings = new TransactionSettings(context, connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE_MMS).getExtraInfo());
        //byte[] rawPdu = HttpUtils.httpConnection(context, mContentLocation, null, HttpUtils.HTTP_GET_METHOD, transactionSettings.isProxySet(), transactionSettings.getProxyAddress(), transactionSettings.getProxyPort());
    }
}