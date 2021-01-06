package com.example.s_car;

import android.bluetooth.BluetoothSocket;

import java.io.IOException;
import java.io.InputStream;

public class receiveData extends Thread {
    InputStream inputStream = null;
    public receiveData(BluetoothSocket bluetoothSocket) throws IOException {
        inputStream = bluetoothSocket.getInputStream();

    }
        public void run() {
            final int BUFFER_SIZE = 1024;
            byte[] buffer = new byte[BUFFER_SIZE];
            int bytes = 0;
            int b = BUFFER_SIZE;

            while (true) {
                try {
                    bytes = inputStream.read(buffer, bytes, BUFFER_SIZE - bytes);
                    String readMessage = new String(buffer, 0, bytes);
                    System.out.println(readMessage);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

    }

}
