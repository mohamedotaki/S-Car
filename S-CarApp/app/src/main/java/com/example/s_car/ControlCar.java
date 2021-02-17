package com.example.s_car;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothHeadset;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.ParcelUuid;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;

public class ControlCar extends AppCompatActivity {

    Button stop;
    ImageButton forwardButton,leftButton,rightButton,reverseButton,parkingAssistButton;
    BluetoothSocket bluetoothSocket = HomeActivity.bluetoothSocket;
    OutputStream outputStream;
    Thread bluetoothThread;
    TextView frontSensor,backSensor,leftSensor,rightSensor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control_car);
        parkingAssistButton = (ImageButton) findViewById(R.id.parkingAssistButton);
        forwardButton = (ImageButton) findViewById(R.id.topArrow);
        leftButton = (ImageButton) findViewById(R.id.leftArrow);
        rightButton = (ImageButton) findViewById(R.id.rightArrow);
        reverseButton = (ImageButton) findViewById(R.id.bottomArrow);
        stop = (Button) findViewById(R.id.StopButton);
        frontSensor = (TextView) findViewById(R.id.frontSensorTextViewControlCar);
        backSensor = (TextView) findViewById(R.id.backSensorTextViewControlCar);
        leftSensor = (TextView) findViewById(R.id.leftSensorTextViewControlCar);
        rightSensor = (TextView) findViewById(R.id.rightSensorTextViewControlCar);


            getSensorsValue(bluetoothSocket);
            bluetoothThread.start();




        try {
            outputStream = bluetoothSocket.getOutputStream();
            write("C");
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(ControlCar.this,"Couldn't connect",Toast.LENGTH_SHORT).show();
            finish();
        }
        parkingAssistButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        write("P");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            forwardButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        write("F");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            reverseButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        write("B");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            stop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        write("S");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            leftButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        write("L");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            rightButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        write("R");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

    }
    public void write(String s)  throws IOException {
        outputStream.write(s.getBytes());
    }

    @Override
    public void onBackPressed() {
        try {
            write("E");
            bluetoothThread.stop();
        } catch (IOException e) {}

        super.onBackPressed();
    }
    private void getSensorsValue(BluetoothSocket bluetoothSocket){
        final InputStream inputStream ;
        InputStream tmpIn = null;
        try {
            tmpIn = bluetoothSocket.getInputStream();
        } catch (IOException e) {}
        inputStream = tmpIn;
         bluetoothThread = new Thread() {
             @Override
             public void run() {
                 while (true) {
                     byte[] buffer = new byte[8];
                     int bytes;
                     try {
                         Thread.sleep(500);
                         bytes = inputStream.read(buffer);            //read bytes from input buffer
                         final byte[] data = buffer;
                         runOnUiThread(new Runnable() {
                             @Override
                             public void run() {
                                 rightSensor.setText((short)(((data[0]) & 0xFF) << 8 | (data[1]) & 0xFF));
                                 frontSensor.setText((short)(((data[2]) & 0xFF) << 8 | (data[3]) & 0xFF));
                                 backSensor.setText((short)(((data[4]) & 0xFF) << 8 | (data[5]) & 0xFF));
                                 leftSensor.setText((short)(((data[6]) & 0xFF) << 8 | (data[7]) & 0xFF));
                             }
                         });
                     } catch (Exception e) {
                         break;
                     }
                 }
             }

         };
    }
}

