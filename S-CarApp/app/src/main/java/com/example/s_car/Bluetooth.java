package com.example.s_car;

import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Bluetooth extends Thread{
    private final InputStream mmInStream;
    private final OutputStream mmOutStream;

    //creation of the connect thread
    public Bluetooth(BluetoothSocket socket) {
        InputStream tmpIn = null;
        OutputStream tmpOut = null;

        try {
            //Create I/O streams for connection
            tmpIn = socket.getInputStream();
            tmpOut = socket.getOutputStream();
        } catch (IOException e) {
        }

        mmInStream = tmpIn;
        mmOutStream = tmpOut;
    }

    public void run() {
        byte[] buffer = new byte[256];
        int bytes;

        // Keep looping to listen for received messages
        while (true) {
            try {
                bytes = mmInStream.read(buffer);            //read bytes from input buffer
                System.out.println(bytes);
                if(buffer[0] == 1){

                }else{

                }

            } catch (IOException e) {
                break;
            }
        }
    }
}
