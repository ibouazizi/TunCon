package com.vidopt.tuncon;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by imedbouazizi on 7/29/14.
 */
public class TunisiaCallHandler extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // Extract phone number reformatted by previous receivers
        String phoneNumber = getResultData();
        if (phoneNumber == null) {
            // No reformatted number, use the original
            phoneNumber = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
        }
        // My app will bring up the call, so cancel the broadcast
        if (phoneNumber.contains("+216")) {
            setResultData(null);
            // Start my app to bring up the call
            Toast.makeText(context, "Will call " + phoneNumber + " using TunCall", Toast.LENGTH_LONG);
        }
    }
}