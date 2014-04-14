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
        String portPrefKey = getString(R.string.pref_hostport_key);
        String namePrefKey = getString(R.string.pref_hostname_key);
        UDPMailMan.setPort(Integer.parseInt(prefs.getString(portPrefKey, getString(R.string.pref_hostport_default))));
        UDPMailMan.setHostName(prefs.getString(namePrefKey, getString(R.string.pref_hostname_default)));

        View viewTreeRoot = inflater.inflate(R.layout.fragment_controller, container, false);

        // Set callbacks for all buttons
        Button powerButton = (Button) viewTreeRoot.findViewById(R.id.button_power);
        powerButton.setActivated(false);
        powerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View powerButton) {
                if (UDPMailMan.isRunning) {
                    UDPMailMan.isRunning = false;
                    powerButton.setBackgroundResource(R.drawable.button_power_off);
                }
                else {
                    UDPMailMan.isRunning = true;
                    powerButton.setBackgroundResource(R.drawable.button_power);
                    UDPMailMan.authenticate = true;
                    // Start another async task loop
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
                        UDPMailMan.forward = true;
                        break;
                    case MotionEvent.ACTION_UP:
                        UDPMailMan.forward = false;
                        UDPMailMan.stop = true;
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
                        UDPMailMan.reverse = true;
                        break;
                    case MotionEvent.ACTION_UP:
                        UDPMailMan.reverse = false;
                        UDPMailMan.stop = true;
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
                        UDPMailMan.left = true;
                        break;
                    case MotionEvent.ACTION_UP:
                        UDPMailMan.left = false;
                        UDPMailMan.realign = true;
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
                        UDPMailMan.right = true;
                        break;
                    case MotionEvent.ACTION_UP:
                        UDPMailMan.right = false;
                        UDPMailMan.realign = true;
                        break;
                }
                return true;
            }

        });

        return viewTreeRoot;
    }
}
