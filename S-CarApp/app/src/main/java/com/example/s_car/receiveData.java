package com.example.s_car;

import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static android.content.ContentValues.TAG;


public class receiveData extends Thread {
    private final BluetoothSocket mmSocket;
    private final InputStream mmInStream;
    private final OutputStream mmOutStream;

    public receiveData(BluetoothSocket socket) {
        Log.d(TAG, "ConnectedThread: Starting.");

        mmSocket = socket;
        InputStream tmpIn = null;
        OutputStream tmpOut = null;


        try {
            tmpIn = mmSocket.getInputStream();
            tmpOut = mmSocket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }

        mmInStream = tmpIn;
        mmOutStream = tmpOut;
    }

    public void run() {
        byte[] buffer = new byte[1024];  // buffer store for the stream
        String[] data = new String[20];
        int bytes; // bytes returned from read()
        String[] lines ;
        // Keep listening to the InputStream until an exception occurs
        while (true) {
            // Read from the InputStream
            try {
                bytes = mmInStream.read(buffer);
                String incomingMessage = new String(buffer, 0, bytes);
                lines = incomingMessage.split("\n");
                for(int i = 0 ; i< lines.length;i++) {
                    data[i] = incomingMessage.split("\n")[i];
                    System.out.println(data[i]);
                }
                //System.out.println(incomingMessage);


            } catch (IOException e) {
                Log.e(TAG, "write: Error reading Input Stream. " + e.getMessage());
                break;
            }
        }
    }
}