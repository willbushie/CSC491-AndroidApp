package com.example.photoshare;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.Inet4Address;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class IncomingConnectionsHandler {
    /*
    This class will handle all incoming user connections.
     */

    public static void lan_recieve() {
        /*
        This method works perfectly over LAN, but not over internet - need to do whole punching
         */
        try {
            // don't need to specify a hostname, it will be the current machine
            ServerSocket ss = new ServerSocket(6348);
            System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ServerSocket awaiting connections...");
            Socket socket = ss.accept(); // blocking call, this will wait until a connection is attempted on this port.
            System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~Connection from " + socket + "!");

            // get the input stream from the connected socket
            InputStream inputStream = socket.getInputStream();
            // create a DataInputStream so we can read data from it.
            DataInputStream dataInputStream = new DataInputStream(inputStream);

            // read the message from the socket
            String message = dataInputStream.readUTF();
            System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~The message sent from the socket was: " + message);

            //System.out.println("Closing sockets.");
            ss.close();
            socket.close();
        }
        catch (IOException e) {
            System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~No longer listening");
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {}
}
