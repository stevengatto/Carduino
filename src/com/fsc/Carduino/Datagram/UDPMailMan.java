package com.fsc.Carduino.Datagram;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.net.*;

/**
 * Sends UDP packets for you. Call {@link #beginUdpLoop()} to begin a loop that listens for command requests and
 * sends them out.
 */
public class UDPMailMan {

    private static final String TAG = UDPMailMan.class.getName();

    // Commands
    public static final String COMMAND_FORWARD = "d";
    public static final String COMMAND_REVERSE = "b";
    public static final String COMMAND_LEFT = "l";
    public static final String COMMAND_RIGHT = "r";
    public static final String COMMAND_STOP = "p";
    public static final String COMMAND_REALIGN = "s";
    public static final String COMMAND_AUTH = "hello";

    // Ip address and port to send to
    public static int port = 0;
    public static String hostName;

    // The address to send to as an InetAddress Object
    private static InetAddress local;

    // True when the corresponding command needs to be sent out
    public static boolean forward = false;
    public static boolean reverse = false;
    public static boolean left = false;
    public static boolean right = false;
    public static boolean stop = false;
    public static boolean realign = false;
    public static boolean authenticate = false;

    // Specifies whether the mail man is running
    public static boolean isRunning = false;

    // The socket to use for UDP communication
    private static DatagramSocket socket;

    // The packet to send; we will reuse this object for each send to avoid unnecessary memory usage
    static DatagramPacket packet;

    // Avoid creating additional async tasks
    public static boolean asyncTaskRunning = false;

    public static void setHostName(String hostName) {
        UDPMailMan.hostName = hostName;
    }

    public static void setPort(int port) {
        UDPMailMan.port = port;
    }


    /**
     * Initiates the main loop that continuously sends commands
     */
    public static void beginUdpLoop() {

        // Spawn a new thread to send UDP messages
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {

                // Set up the socket based on provided port and ip address
                try {
                    socket = new DatagramSocket(port);
                    local = InetAddress.getByName(hostName);
                } catch (SocketException e) {
                    Log.e(UDPMailMan.class.getName(), String.format("Could not instantiate DatagramSocket object, " +
                            "exception: %s", e.toString()));
                } catch (UnknownHostException e) {
                    Log.e(UDPMailMan.class.getName(), "Unknown host exception", e);
                }

                // Avoid creating additional async tasks
                asyncTaskRunning = true;

                // Start looping the first time we're called
                isRunning = true;

                // Run in a loop and wait for commands to be sent out
                while (isRunning) {
                    if (forward) {
                        logAndSendPacket(COMMAND_FORWARD);

                    }
                    else if (reverse) {
                        logAndSendPacket(COMMAND_REVERSE);
                    }

                    if (left) {
                        logAndSendPacket(COMMAND_LEFT);
                    }

                    else if (right) {
                        logAndSendPacket(COMMAND_RIGHT);
                    }

                    else if (stop) {
                        logAndSendPacket(COMMAND_STOP);
                        stop = false;
                    }

                    else if (realign) {
                        logAndSendPacket(COMMAND_REALIGN);
                    }
                    else if (authenticate) {
                        logAndSendPacket(COMMAND_AUTH);
                        authenticate = false;
                    }

                }
                return null;
            }
        }.execute();
    }

    /**
     * Log an outgoing packet and send it
     *
     * @param message the message to send
     */
    public static void logAndSendPacket(String message) {

        DatagramPacket packet = new DatagramPacket(message.getBytes(), message.length(), local, port);

        try {
            socket.send(packet);
            Log.d(UDPMailMan.class.getName(), String.format("Sent packet %s on port %d to address %s " +
                    "(resolved from name %s)", new String(packet.getData()), port, local.toString(), hostName));
        } catch (IOException e) {
            Log.e(UDPMailMan.class.getName(), String.format("Error! Exception occurred during sending of packet %s " +
                    "Exception: %s",
                    packet.toString(), e.toString()));
        }
    }
}
