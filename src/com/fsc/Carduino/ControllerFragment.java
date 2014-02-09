package com.fsc.Carduino;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.fsc.Datagram.UDPMailMan;

/**
 * Fragment to display car controller
 */
public class ControllerFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        // Set the ip and port of the arduino
        UDPMailMan.setPort(2390);
        UDPMailMan.setIpAddress("192.168.0.107");


        View viewTreeRoot = inflater.inflate(R.layout.fragment_controller, container, false);

        // Set callbacks for all buttons
        Button upButton = (Button) viewTreeRoot.findViewById(R.id.button_up);
        upButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch ( event.getAction() ) {
                    case MotionEvent.ACTION_DOWN:
                        UDPMailMan.forward = true;
                        break;
                    case MotionEvent.ACTION_UP:
                        UDPMailMan .forward = false;
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
                        UDPMailMan .reverse = false;
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
                        UDPMailMan .left = false;
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
                        UDPMailMan .right = false;
                        break;
                }
                return true;
            }

        });

        // Begin sending out UDP packets
        UDPMailMan.beginUdpLoop();

        return viewTreeRoot;
    }
}
