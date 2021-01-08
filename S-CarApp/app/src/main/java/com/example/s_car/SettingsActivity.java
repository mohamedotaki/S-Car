package com.example.s_car;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.ParcelUuid;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Set;

public class SettingsActivity extends AppCompatActivity {
    Switch wifiSwitch;
    TextView wifiListHeading,isConnectedBluetooth ;
    Button wifiConnectButton,enableBluetoothButton;
    ListView availableWifiList,bluetoothListView;
    EditText wifiPasswordEditText;
    OutputStream outputStream;
    InputStream inStream;
    BluetoothSocket socket ;
    String[] bluetoothDevicesArray , availableWifi;
    ArrayAdapter bluetoothAdapter,wifiAdapter;
    ArrayList<BluetoothDevice> bluetoothDevicesArrayList = new ArrayList<BluetoothDevice>();
    BluetoothAdapter blueAdapter = BluetoothAdapter.getDefaultAdapter();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        wifiSwitch = (Switch) findViewById(R.id.wifiSwitch);
        wifiConnectButton = (Button) findViewById(R.id.wifiConnectButton);
        wifiListHeading = (TextView) findViewById(R.id.wifiListHeading);
        isConnectedBluetooth = (TextView) findViewById(R.id.isConnectedBluetoothTextView);
        availableWifiList = (ListView) findViewById(R.id.availableWifiList);
        wifiPasswordEditText = (EditText) findViewById(R.id.wifiPasswordEditText);
        bluetoothListView = (ListView) findViewById(R.id.bluetoothListView);
        enableBluetoothButton = (Button) findViewById(R.id.enableBluetoothButton);


        bluetoothDevicesArray = availableBluetooth();
        if(bluetoothDevicesArray!= null) {
            bluetoothAdapter = new ArrayAdapter<String>(this, R.layout.settings_adapter, R.id.bluetoothName, bluetoothDevicesArray);
            bluetoothListView.setAdapter(bluetoothAdapter);
            bluetoothListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    try {
                        if (connectToDevice(bluetoothDevicesArrayList.get(position))) {
                            isConnectedBluetooth.setText("Connected");
                            bluetoothAdapter.notifyDataSetChanged();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }


        enableBluetoothButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if (blueAdapter.enable()){
                   try {
                       Thread.sleep(150);
                   } catch (InterruptedException e) {
                       e.printStackTrace();
                   }
                   Intent intent = getIntent();
                   finish();
                   startActivity(intent);
               }
            }
        });

        wifiSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(wifiSwitch.getText().equals("Off") ) {
                    wifiSwitch.setText("On");
                    wifiListHeading.setVisibility(View.VISIBLE);
                    availableWifiList.setVisibility(View.VISIBLE);
                    wifiPasswordEditText.setVisibility(View.VISIBLE);
                    wifiConnectButton.setVisibility(View.VISIBLE);
                    try {
                        write("W");
                    } catch (IOException e) {
                        wifiSwitch.setText("Off");
                        Toast.makeText(SettingsActivity.this, "Couldn't Connect to The Network", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                    receiveData receiveData = new receiveData(socket);
                    receiveData.start();

                }else{
                    wifiSwitch.setText("Off");
                    Toast.makeText(SettingsActivity.this, "Please connect the car via Bluetooth first", Toast.LENGTH_SHORT).show();
                    wifiListHeading.setVisibility(View.GONE);
                    availableWifiList.setVisibility(View.GONE);
                    wifiPasswordEditText.setVisibility(View.GONE);
                    wifiConnectButton.setVisibility(View.GONE);

                }

            }
        });
    }


    String[] availableBluetooth() {
        String[] devicesName = new String[0];
        if (blueAdapter != null) {
            if (blueAdapter.isEnabled()) {
                enableBluetoothButton.setVisibility(View.GONE);
                bluetoothListView.setVisibility(View.VISIBLE);
                Set<BluetoothDevice> bondedDevices = blueAdapter.getBondedDevices();
                if(bondedDevices.size() > 0) {
                    Object[] devices = (Object []) bondedDevices.toArray();
                    devicesName = new String[bondedDevices.size()];
                    for(int i=0; i <bondedDevices.size();i++){
                        BluetoothDevice  device = (BluetoothDevice) devices[i];
                            devicesName[i] = device.getName();
                            bluetoothDevicesArrayList.add(device);
                    }
                }
                return devicesName;
            } else {
                bluetoothListView.setVisibility(View.GONE);
                enableBluetoothButton.setVisibility(View.VISIBLE);
            }
        }
        return null;
    }
    private boolean connectToDevice(BluetoothDevice device) throws Exception{
        Toast.makeText(SettingsActivity.this, "Connecting to " + device.getName() +", Please wait...", Toast.LENGTH_SHORT).show();
        boolean connected = false;
        ParcelUuid[] uuids = device.getUuids();
         socket = device.createRfcommSocketToServiceRecord(uuids[0].getUuid());
        if(!socket.isConnected()) {
            socket.connect();
        }
        if(socket.isConnected()) {
            Toast.makeText(SettingsActivity.this, "Connected to " + device.getName(), Toast.LENGTH_SHORT).show();
            connected = true;
        }else{
            Toast.makeText(SettingsActivity.this, "Failed to connect to " + device.getName(), Toast.LENGTH_SHORT).show();
        }
        outputStream = socket.getOutputStream();
        inStream = socket.getInputStream();
        return connected;
    }

    public void write(String s) throws IOException {
        outputStream.write(s.getBytes());
    }


}