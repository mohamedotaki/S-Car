package com.example.s_car;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.ParcelUuid;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;

public class HomeActivity extends AppCompatActivity {
    Button controlButton, settingsButton;
    OutputStream outputStream;
    BluetoothSocket bluetoothSocket;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        controlButton = (Button)findViewById(R.id.controlButton);
        settingsButton = (Button)findViewById(R.id.settingsButton);

        try {
            availableBluetooth();
        } catch (Exception e) {
            e.printStackTrace();
        }
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goTo(SettingsActivity.class);
            }
        });
        controlButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    goTo(ControlCar.class);
            }
        });
    }

    void availableBluetooth() throws Exception {
        BluetoothAdapter blueAdapter = BluetoothAdapter.getDefaultAdapter();
        SharedPreferences sharedPreferences = getSharedPreferences("settings", MODE_PRIVATE);
        String bluetoothDeviceName = sharedPreferences.getString("bluetoothDeviceName", "noName");
        if(!bluetoothDeviceName.equalsIgnoreCase("noName")) {
            if (blueAdapter != null) {
                if (blueAdapter.isEnabled()) {
                    Set<BluetoothDevice> bondedDevices = blueAdapter.getBondedDevices();
                    if (bondedDevices.size() > 0) {
                        Object[] devices = (Object[]) bondedDevices.toArray();
                        for (int i = 0; i < bondedDevices.size(); i++) {
                            BluetoothDevice device = (BluetoothDevice) devices[i];
                            if (bluetoothDeviceName.equalsIgnoreCase(device.getName())) ;
                            bluetoothSocket = connectToDevice(device);
                            if (bluetoothSocket.isConnected()) {
                                Toast.makeText(HomeActivity.this, "Connected to Car", Toast.LENGTH_SHORT).show();
                                outputStream = bluetoothSocket.getOutputStream();
                            } else {
                                Toast.makeText(HomeActivity.this, "Not Connected to Car", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                }
            }
        }else Toast.makeText(HomeActivity.this, "Please connect to the car in settings", Toast.LENGTH_SHORT).show();

    }

     static BluetoothSocket connectToDevice(BluetoothDevice device) throws Exception{
        ParcelUuid[] uuids = device.getUuids();
         BluetoothSocket socket = device.createRfcommSocketToServiceRecord(uuids[0].getUuid());
        if(!socket.isConnected()) {
            socket.connect();
        }
        if(socket.isConnected()) {
            return socket;
        }
        return null;
    }


    public void write(String s) throws IOException {
        outputStream.write(s.getBytes());
    }


    private void goTo(Class activity){
        try {
            Intent intent = new Intent(getApplicationContext(),activity);
            startActivity(intent);
        }catch (Exception e){}

    }

}