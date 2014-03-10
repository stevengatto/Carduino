package com.fsc.Carduino;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.fsc.Carduino.Datagram.UDPMailMan;

/**
 * Fragment to display car controller
 */
public class ControllerFragment extends Fragment {

    public static final String PREF_KEY_IP_ADDRESS = "pref_key_host_ip";
    public static final String PREF_KEY_HOST_PORT = "pref_key_host_port";

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        //Ensure that the settings contain a port and ip address
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
        String ipAddress = prefs.getString(PREF_KEY_IP_ADDRESS, "");
        String port = prefs.getString(PREF_KEY_HOST_PORT, "");
        if (ipAddress.equals("") || port.equals("")) {
            Intent intent = new Intent(activity, PreferencesActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        // Grab the IP and port from preferences
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        UDPMailMan.setPort(Integer.parseInt(prefs.getString(PREF_KEY_HOST_PORT, "2390")));
        UDPMailMan.setIpAddress(prefs.getString(PREF_KEY_IP_ADDRESS, "127.0.0.0"));

        View viewTreeRoot = inflater.inflate(R.layout.fragment_controller, container, false);

        // Set callbacks for all buttons

        Button powerButton = (Button) viewTreeRoot.findViewById(R.id.button_power);
        powerButton.setActivated(false);
        powerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View powerButton) {
                if (UDPMailMan.isRunning()) {
                    UDPMailMan.setRunningState(false);
                    powerButton.setBackgroundResource(R.drawable.button_power_off);
                    UDPMailMan.authenticate = true; //authenticate to the server
                    UDPMailMan.authenticate = false; // stop sending authentication messages
                }
                else {
                    UDPMailMan.setRunningState(true);
                    powerButton.setBackgroundResource(R.drawable.button_power);
                    UDPMailMan.beginUdpLoop();
                }
            }
        });

        Button upButton = (Button) viewTreeRoot.findViewById(R.id.button_up);
        upButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch ( event.getAction() ) {
                    case MotionEvent.ACTION_DOWN:
                        UDPMailMan.stop = false;  //quit stopping
                        UDPMailMan.forward = true; //go
                        break;
                    case MotionEvent.ACTION_UP:
                        UDPMailMan.forward = false; //stop going
                        UDPMailMan.stop = true; // stop
                        break;
                }
                return true;
            }

        });

        Button downButton = (Button) viewTreeRoot.findViewById(R.id.button_down);
        downButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch ( event.getAction() ) {
                    case MotionEvent.ACTION_DOWN:
                        UDPMailMan.stop = false; // quit sending stop messages
                        UDPMailMan.reverse = true;//go
                        break;
                    case MotionEvent.ACTION_UP:
                        UDPMailMan.reverse = false;//stop sending signal to go
                        UDPMailMan.stop = true;//kill power
                        break;
                }
                return true;
            }

        });

        Button leftButton = (Button) viewTreeRoot.findViewById(R.id.button_left);
        leftButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch ( event.getAction() ) {
                    case MotionEvent.ACTION_DOWN:
                        UDPMailMan.realign = false; //stop telling it to realign
                        UDPMailMan.left = true; //start turning left
                        break;
                    case MotionEvent.ACTION_UP:
                        UDPMailMan.left = false; //stop sending left turn signals
                        UDPMailMan.realign = true; //realign the wheels
                        break;
                }
                return true;
            }

        });

        Button rightButton = (Button) viewTreeRoot.findViewById(R.id.button_right);
        rightButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch ( event.getAction() ) {
                    case MotionEvent.ACTION_DOWN:
                        UDPMailMan.realign = false;
                        UDPMailMan.right = true;
                        break;
                    case MotionEvent.ACTION_UP:
                        UDPMailMan.right = false; //stop sending right turn signals
                        UDPMailMan.realign = true; //reset wheels to straight
                        break;
                }
                return true;
            }

        });

        return viewTreeRoot;
    }
}
