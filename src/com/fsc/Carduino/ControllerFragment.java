package com.fsc.Carduino;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
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

        View viewTreeRoot = inflater.inflate(R.layout.fragment_controller, container, false);

        Button upButton = (Button) viewTreeRoot.findViewById(R.id.button_up);
        upButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                UDPMailMan.sendDatagram("hello");
            }
        });
        return viewTreeRoot;
    }
}
