package com.example.s_car;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothHeadset;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.os.ParcelUuid;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;

public class ControlCar extends AppCompatActivity {

    Button stop;
    ImageButton forwardButton,leftButton,rightButton,reverseButton,parkingAssistButton;
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
        try {
           // blue();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(ControlCar.this,"Couldn't connect",Toast.LENGTH_SHORT).show();
            finish();
        }
 /*           parkingAssistButton.setOnClickListener(new View.OnClickListener() {
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
            });*/










    }





}