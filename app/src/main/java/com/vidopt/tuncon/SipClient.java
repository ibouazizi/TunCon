package com.vidopt.tuncon;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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

    public final String domain = "tunconnection.com";
    public SipManager manager = null;
    public SipProfile me = null;
    public SipAudioCall call = null;
    public IncomingCallReceiver callReceiver;
    Context mContext;

    public SipClient()
    {

    }

    public void initializeManager(Context context) {
        mContext = context;

        IntentFilter filter = new IntentFilter();
        filter.addAction("android.tuncon.INCOMING_CALL");
        callReceiver = new IncomingCallReceiver();
        mContext.registerReceiver(callReceiver, filter);

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
            return;
        }

        try {
            SipProfile.Builder builder = new SipProfile.Builder("test", "192.168.1.13");
            builder.setPassword("tounes11");
            //builder.setOutboundProxy("192.168.1.13");
            //builder.setAutoRegistration(true);
            //builder.setProtocol("UDP");
            me = builder.build();

            Intent intent = new Intent();
            intent.setAction("android.tuncon.INCOMING_CALL");
            PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, 0, intent, Intent.FILL_IN_DATA);

            manager.open(me, pendingIntent, null);

            manager.setRegistrationListener(me.getUriString(), new SipRegistrationListener() {
                //manager.register(me, 6000, new SipRegistrationListener() {
                @Override
                public void onRegistering(String localProfileUri) {
                    Log.d("Registering", "Registering user");
                    //Toast toast = Toast.makeText(mContext, "Registering " + localProfileUri, Toast.LENGTH_LONG);
                    //toast.show();
                }

                @Override
                public void onRegistrationDone(String localProfileUri, long expiryTime) {
                    Log.d("Error", "registration successful");
                    //Toast toast = Toast.makeText(mContext, "Registration successful: " + localProfileUri, Toast.LENGTH_LONG);
                    //toast.show();
                }

                @Override
                public void onRegistrationFailed(String localProfileUri, int errorCode, String errorMessage) {
                    String text = "Error registering " + localProfileUri + ", error code: " + errorCode + ", error message: " + errorMessage;
                    //Toast toast = Toast.makeText(mContext, text, Toast.LENGTH_LONG);
                    //toast.show();
                }
            });

        } catch (ParseException pe) {
            Log.d("Parse Exception: ", "Error: " + pe.toString());
            Toast toast = Toast.makeText(mContext, pe.toString(), Toast.LENGTH_LONG);
            toast.show();
        } catch (SipException se) {
            Log.d("SIP Exception: ", "Error: " + se.toString());
            Toast toast = Toast.makeText(mContext, se.toString(), Toast.LENGTH_LONG);
            toast.show();
        } catch (Exception e) {
            Log.d("Exception: ", "Error: " + e.toString());
            Toast toast = Toast.makeText(mContext, e.toString(), Toast.LENGTH_LONG);
            toast.show();
        }
    }

    public void closeLocalProfile() {
        if (manager == null) {
            return;
        }
        if (callReceiver != null) {
            mContext.unregisterReceiver(callReceiver);
        }
        try {
            if (me != null) {
                manager.close(me.getUriString());
                me = null;
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
                    Toast toast = Toast.makeText(mContext, "Call established!", Toast.LENGTH_LONG);
                    toast.show();
                }

                public void onCallEnded(SipAudioCall call) {
                    Toast toast = Toast.makeText(mContext, "Call ended!", Toast.LENGTH_LONG);
                    toast.show();
                }
            };

            call = manager.makeAudioCall(me.getUriString(), "sip:test3@192.168.1.13"/*sipAddress*/, listener, 30);

        } catch (Exception ee) {
            if (me != null) {
                try {
                    manager.close(me.getUriString());
                } catch (Exception e) {
                    Log.d("Error", "Error establishing call: " + e.toString());
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
