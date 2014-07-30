package com.vidopt.tuncon;

import android.app.Fragment;
import android.content.DialogInterface;
import android.net.sip.SipAudioCall;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * A placeholder fragment containing a simple view.
 */
public class DialerFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    public View rootView = null;
    SipClient sip = null;

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static DialerFragment newInstance(int sectionNumber) {
        DialerFragment fragment = new DialerFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public DialerFragment() {
        sip = new SipClient();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_dialer, container, false);

        Button btn1 = (Button)rootView.findViewById(R.id.btn1);
        btn1.setTag("1");
        btn1.setOnClickListener(new DialerClickListener());

        Button btn2 = (Button)rootView.findViewById(R.id.btn2);
        btn2.setTag("2");
        btn2.setOnClickListener(new DialerClickListener());

        Button btn3 = (Button)rootView.findViewById(R.id.btn3);
        btn3.setTag("3");
        btn3.setOnClickListener(new DialerClickListener());

        Button btn4 = (Button)rootView.findViewById(R.id.btn4);
        btn4.setTag("4");
        btn4.setOnClickListener(new DialerClickListener());

        Button btn5 = (Button)rootView.findViewById(R.id.btn5);
        btn5.setTag("5");
        btn5.setOnClickListener(new DialerClickListener());

        Button btn6 = (Button)rootView.findViewById(R.id.btn6);
        btn6.setTag("6");
        btn6.setOnClickListener(new DialerClickListener());

        Button btn7 = (Button)rootView.findViewById(R.id.btn7);
        btn7.setTag("7");
        btn7.setOnClickListener(new DialerClickListener());

        Button btn8 = (Button)rootView.findViewById(R.id.btn8);
        btn8.setTag("8");
        btn8.setOnClickListener(new DialerClickListener());

        Button btn9 = (Button)rootView.findViewById(R.id.btn9);
        btn9.setTag("9");
        btn9.setOnClickListener(new DialerClickListener());

        Button btn0 = (Button)rootView.findViewById(R.id.btn0);
        btn0.setTag("0");
        btn0.setOnClickListener(new DialerClickListener());

        Button btnPound = (Button)rootView.findViewById(R.id.btnPound);
        btnPound.setTag("Pound");
        btnPound.setOnClickListener(new DialerClickListener());

        Button btnStar = (Button)rootView.findViewById(R.id.btnStar);
        btnStar.setTag("Star");
        btnStar.setOnClickListener(new DialerClickListener());


        Button btnDel = (Button)rootView.findViewById(R.id.btnDelete);
        btnDel.setTag("Delete");
        btnDel.setOnClickListener(new DialerClickListener());

        Button btnCall = (Button)rootView.findViewById(R.id.btnCall);
        btnCall.setTag("Call");
        btnCall.setOnClickListener(new DialerClickListener());

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

        Log.d("Fragment", "Fragment Start");
        sip.initializeManager(getActivity());
    }


    @Override
    public void onStop() {
        super.onStop();

        Log.d("Fragment", "Fragment Start");

        sip.endCall();
        sip.closeLocalProfile();
    }

    class DialerClickListener implements View.OnClickListener
    {
        public void onClick(View v)
        {
            TextView tv = (TextView)rootView.findViewById(R.id.tvNumber);
            String num = tv.getText().toString();

            Button btn = (Button)v;
            String str = btn.getTag().toString();

                if (str == "dial") {
                    if (sip != null && num.length() == 13) {
                        sip.initiateCall(num);
                    }
                }
                else if (str == "Star" || str == "Pound") {

                }
                else if (str == "Delete") {
                    if (num.length() > 5) {
                        String s = num.substring(0, num.length() - 1);
                        tv.setText(s);
                    }
                } else {
                    if (num.length() < 13) {

                        tv.setText(num + str);

                }
            }
        }
    };
}
