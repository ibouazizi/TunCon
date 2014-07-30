package com.vidopt.tuncon;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.sip.SipAudioCall;
import android.net.sip.SipException;
import android.net.sip.SipManager;
import android.net.sip.SipProfile;
import android.net.sip.SipRegistrationListener;
import android.util.Log;
import android.widget.Toast;

import java.text.ParseException;

/**
 * Created by imedbouazizi on 7/25/14.
 */
public class SipClient {

    public SipManager manager = null;
    public SipProfile me = null;
    public SipAudioCall call = null;

    public final String domain = "tunconnection.com";

    Context parent;

    public SipClient()
    {

    }

    public void initializeManager(Context context) {
        parent = context;

        if (manager == null) {
            manager = SipManager.newInstance(context);
        }

        initializeLocalProfile();
    }

    public void initializeLocalProfile() {
        if (manager == null) {
            return;
        }

        if (me != null) {
            closeLocalProfile();
        }

        try {
            SipProfile.Builder builder = new SipProfile.Builder("test", "192.168.1.13");
            builder.setPassword("tounes11");
            builder.setOutboundProxy("192.168.1.13");
            builder.setAutoRegistration(true);
            builder.setProtocol("TCP");
            me = builder.build();

            Intent intent = new Intent();
            intent.setAction("android.tuncon.INCOMING_CALL");
            PendingIntent pendingIntent = PendingIntent.getBroadcast(parent, 0, intent, Intent.FILL_IN_DATA);

            manager.open(me);//, pendingIntent, null);

            //manager.setRegistrationListener(me.getUriString(), new SipRegistrationListener() {
            manager.register(me, 2000, new SipRegistrationListener() {
                @Override
                public void onRegistering(String localProfileUri) {
                    Log.d("Registering", "Registering user");
                }

                @Override
                public void onRegistrationDone(String localProfileUri, long expiryTime) {
                    Log.d("Error", "registration successful");
                }

                @Override
                public void onRegistrationFailed(String localProfileUri, int errorCode, String errorMessage) {
                    String text = "Error registering " + localProfileUri + ", error code: " + errorCode + ", error message: " + errorMessage;
                    Toast.makeText(parent, text, Toast.LENGTH_LONG);
                }
            });

        } catch (ParseException pe) {
            Log.d("Parse Exception: ", "Error: " + pe.toString());
        } catch (SipException se) {
            Log.d("SIP Exception: ", "Error: " + se.toString());

        }
    }

    public void closeLocalProfile() {
        if (manager == null) {
            return;
        }
        try {
            if (me != null) {
                manager.close(me.getUriString());
            }
        } catch (Exception ee) {
            Log.d("TunCon", "Failed to close local profile", ee);
        }
    }


    public void initiateCall(String phone) {
        String sipAddress = phone;
        if (!phone.contains("+216")) {
            sipAddress = "sip:+216 " + phone + "@" + domain;
        } else {
            sipAddress = "sip:" + phone + "@" + domain;
        }
        try {
            SipAudioCall.Listener listener = new SipAudioCall.Listener() {
                public void onCallEstablished(SipAudioCall call) {

                }

                public void onCallEnded(SipAudioCall call) {

                }
            };

            call = manager.makeAudioCall(me.getUriString(), sipAddress, listener, 30);

        } catch (Exception ee) {
            if (me != null) {
                try {
                    manager.close(me.getUriString());
                } catch (Exception e) {

                }
            }

            if (call != null) {
                call.close();
            }
        }
    }

    public void endCall() {
        if (call != null) {
            try {
                call.endCall();
            } catch(SipException se) {

            }
            call.close();
        }
    }

}
