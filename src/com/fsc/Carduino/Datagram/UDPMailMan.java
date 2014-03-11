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

    // Ip address and port to send to
    public static int port = 0;
    public static String ipAddress;

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
    private static boolean isRunning = false;

    // The socket to use for UDP communication
    private static DatagramSocket socket;

    // The packet to send; we will reuse this object for each send to avoid unnecessary memory usage
    static DatagramPacket packet;

    public static String getIpAddress() {
        return ipAddress;
    }

    public static void setIpAddress(String ipAddress) {
        UDPMailMan.ipAddress = ipAddress;
    }

    public static int getPort() {
        return port;
    }

    public static void setPort(int port) {
        UDPMailMan.port = port;
    }

    public static boolean isRunning() {
        return isRunning;
    }

    public static void setRunningState(boolean isRunning) {
        UDPMailMan.isRunning = isRunning;
    }

    /**
     * Initiates the main loop that continuously sends commands
     */
    public static void beginUdpLoop() {

        // Set up the socket based on provided port and ip address
        try {
            socket = new DatagramSocket(port);
            local = InetAddress.getByName(ipAddress);
        } catch (SocketException e) {
            Log.e(UDPMailMan.class.getName(), String.format("Could not instantiate DatagramSocket object, " +
                    "exception: %s", e.toString()));
        } catch (UnknownHostException e) {
            Log.e(UDPMailMan.class.getName(), "Unknown host exception", e);
        }

        // Spawn a new thread to send UDP messages
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {

                // Start looping the first time we're called
                isRunning = true;

                // Run in a loop and wait for commands to be sent out
                while (isRunning) {
                    if (forward) {
                        String data = "d";
                        int msg_length = data.length();
                        byte[] message = data.getBytes();
                        packet = new DatagramPacket(message, msg_length, local, port);
                        logAndSendPacket(packet);
                        forward = false;

                    }
                    else if (reverse) {
                        String data = "b";
                        int msg_length = data.length();
                        byte[] message = data.getBytes();
                        packet = new DatagramPacket(message, msg_length, local, port);
                        logAndSendPacket(packet);
                        reverse = false;
                    }

                    if (left) {
                        String data = "l";
                        int msg_length = data.length();
                        byte[] message = data.getBytes();
                        packet = new DatagramPacket(message, msg_length, local, port);
                        logAndSendPacket(packet);
                        left = false;
                    }

                    else if (right) {
                        String data = "r";
                        int msg_length = data.length();
                        byte[] message = data.getBytes();
                        packet = new DatagramPacket(message, msg_length, local, port);
                        logAndSendPacket(packet);
                        right = false;
                    }

                    else if (stop) {
                        String data = "p";
                        int msg_length = data.length();
                        byte[] message = data.getBytes();
                        packet = new DatagramPacket(message, msg_length, local, port);
                        logAndSendPacket(packet);
                        stop = false;
                    }

                    else if (realign) {
                        String data = "s";
                        int msg_length = data.length();
                        byte[] message = data.getBytes();
                        packet = new DatagramPacket(message, msg_length, local, port);
                        logAndSendPacket(packet);
                        realign = false;
                    }

                    else if (authenticate) {
                        String data = "hello";
                        int msg_length = data.length();
                        byte[] message = data.getBytes();
                        packet = new DatagramPacket(message, msg_length, local, port);
                        logAndSendPacket(packet);
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
     * @param packet the packet to send
     */
    public static void logAndSendPacket(DatagramPacket packet) {
        try {
            socket.send(packet);
            Log.d(UDPMailMan.class.getName(), String.format("Sent packet %s on port %d to address %s",
                    packet.toString(), port, ipAddress));
        } catch (IOException e) {
            Log.e(UDPMailMan.class.getName(), String.format("Error! Exception occurred during sending of packet %s " +
                    "Exception: %s",
                    packet.toString(), e.toString()));
        }
    }
}
