package com.example.s_car;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import java.util.ArrayList;
import java.util.Set;

public class SettingsActivity extends AppCompatActivity {
    Switch wifiSwitch;
    TextView wifiListHeading,isConnectedBluetooth,wifiTextView ;
    Button wifiConnectButton, bluetoothButton;
    ListView availableWifiList,bluetoothListView;
    EditText wifiPasswordEditText;
    String[] bluetoothDevicesArray , availableWifi;
    ArrayAdapter bluetoothAdapter,wifiAdapter;
    ArrayList<BluetoothDevice> bluetoothDevicesArrayList = new ArrayList<BluetoothDevice>();
    BluetoothSocket bluetoothSocket = HomeActivity.bluetoothSocket;
    OutputStream outputStream;
    User user;
    Thread bluetoothThread;
    BluetoothAdapter blueAdapter = BluetoothAdapter.getDefaultAdapter();
    ArrayList<String> wifiName = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        wifiSwitch = findViewById(R.id.wifiSwitch);
        wifiConnectButton = findViewById(R.id.wifiConnectButton);
        wifiListHeading = findViewById(R.id.wifiListHeading);
        wifiTextView = findViewById(R.id.chosenWifi);
        isConnectedBluetooth = findViewById(R.id.isConnectedBluetoothTextView);
        availableWifiList = findViewById(R.id.availableWifiList);
        wifiPasswordEditText = findViewById(R.id.wifiPasswordEditText);
        bluetoothListView = findViewById(R.id.bluetoothListView);
        bluetoothButton = findViewById(R.id.bluetoothButtonSettings);

        Intent intent = getIntent();
        Bundle receivedData = intent.getExtras();
        if(receivedData != null) {
            user = (User) receivedData.getSerializable("currentUser");
        }

        /////////BlueTooth
        bluetoothDevicesArray = availableBluetooth(); // get all available bluetooth devices
        // list all the available devices and set on item click
        if(bluetoothDevicesArray!= null) {
            bluetoothAdapter = new ArrayAdapter<String>(this, R.layout.settings_adapter, R.id.bluetoothName, bluetoothDevicesArray);
            bluetoothListView.setAdapter(bluetoothAdapter);
            bluetoothListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    try {
                         bluetoothSocket = HomeActivity.connectToDevice(bluetoothDevicesArrayList.get(position),user);
                        if (bluetoothSocket.isConnected()) {
                           SharedPreferences sharedPref = getSharedPreferences("settings", MODE_PRIVATE);
                           sharedPref.edit().putString("bluetoothDeviceName", bluetoothDevicesArrayList.get(position).getName()).apply();
                            Toast.makeText(SettingsActivity.this,"Connected to "+ bluetoothDevicesArrayList.get(position).getName(),Toast.LENGTH_SHORT).show();

                           outputStream = bluetoothSocket.getOutputStream();
                           bluetoothButton.setText("Disconnect");
                           bluetoothButton.setVisibility(View.VISIBLE);
                            bluetoothListView.setVisibility(View.GONE);
                            // isConnectedBluetooth.setText("Connected");
                            bluetoothAdapter.notifyDataSetChanged();
                            HomeActivity.bluetoothSocket =bluetoothSocket;
                        }else {
                            Toast.makeText(SettingsActivity.this,"Failed to Connect to "+ bluetoothDevicesArrayList.get(position).getName(),Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        // check if the user is connected to display the disconnect button
        try {
            if (bluetoothSocket.isConnected()) {
                outputStream = bluetoothSocket.getOutputStream();
                bluetoothButton.setText("Disconnect");
                bluetoothButton.setVisibility(View.VISIBLE);
                bluetoothListView.setVisibility(View.GONE);
            }
        }catch (Exception e){}

        bluetoothButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int s = 0;
                switch(bluetoothButton.getText().toString()){
                    case"Disconnect":
                        try {
                            bluetoothSocket.close();
                            HomeActivity.bluetoothSocket = bluetoothSocket;
                            bluetoothButton.setVisibility(View.GONE);
                            bluetoothListView.setVisibility(View.VISIBLE);
                            Toast.makeText(SettingsActivity.this,"Disconnected",Toast.LENGTH_SHORT).show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                    case"Enable Bluetooth":
                        if (blueAdapter.enable()){
                            try {
                                Thread.sleep(200);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            Intent intent = getIntent();
                            finish();
                            startActivity(intent);
                        }
                        break;
                }

            }
        });

        wifiSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (wifiSwitch.getText().equals("Off") && bluetoothSocket.isConnected()) {
                        wifiSwitch.setText("On");
                        writeToBluetooth("W");
                        getWIFIName(bluetoothSocket);
                        bluetoothThread.start();
                        wifiListHeading.setVisibility(View.VISIBLE);
                        wifiListHeading.setText("Searching....");
                    } else if(wifiSwitch.getText().equals("On")) {
                        wifiSwitch.setText("Off");
                        wifiListHeading.setVisibility(View.GONE);
                        availableWifiList.setVisibility(View.GONE);
                        wifiTextView.setVisibility(View.GONE);
                        wifiPasswordEditText.setVisibility(View.GONE);
                        wifiConnectButton.setVisibility(View.GONE);
                        wifiSwitch.setChecked(false);
                    }else{
                        Toast.makeText(SettingsActivity.this, "Please connect the car via Bluetooth first", Toast.LENGTH_SHORT).show();
                        wifiSwitch.setChecked(false);
                    }
                }catch (Exception e){
                    Toast.makeText(SettingsActivity.this, "Please connect the car via Bluetooth first", Toast.LENGTH_SHORT).show();
                    wifiSwitch.setChecked(false);
                }

            }
        });
        availableWifiList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                availableWifiList.setVisibility(View.GONE);
                wifiListHeading.setVisibility(View.GONE);
                wifiTextView.setVisibility(View.VISIBLE);
                wifiPasswordEditText.setVisibility(View.VISIBLE);
                wifiConnectButton.setVisibility(View.VISIBLE);
                wifiTextView.setText(wifiName.get(position));
            }
        });
        wifiConnectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    writeToBluetooth("O");
                    writeToBluetooth(wifiTextView.getText().toString());
                    outputStream.write(3);
                    writeToBluetooth(wifiPasswordEditText.getText().toString());
                    outputStream.write(4);
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        });
    }


    String[] availableBluetooth() {
        String[] devicesName = new String[0];
        if (blueAdapter != null) {
            if (blueAdapter.isEnabled()) {
                bluetoothButton.setVisibility(View.GONE);
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
                bluetoothButton.setVisibility(View.VISIBLE);
                bluetoothButton.setText("Enable Bluetooth");
            }
        }
        return null;
    }


    public void writeToBluetooth(String s) throws IOException {
        outputStream.write(s.getBytes());
    }

    private void getWIFIName(BluetoothSocket bluetoothSocket){
        final InputStream inputStream ;
        InputStream tmpIn = null;
        try {
            tmpIn = bluetoothSocket.getInputStream();
        } catch (IOException e) {}
        inputStream = tmpIn;
        bluetoothThread = new Thread() {
            @Override
            public void run() {

                String name = "";
                while (true) {
                    byte[] buffer = new byte[1];
                    try {
                        inputStream.read(buffer);            //read bytes from input buffer
                        if(buffer[0]== 4)           //end of receiving data
                            break;
                        if(buffer[0] != 3) {  // if not end of the text
                            if(buffer[0] != 0)      // don't add empty spaces
                                name += (char) buffer[0];
                        }else{
                            wifiName.add(name);
                            System.out.println(name);
                            name = "";
                        }
                    } catch (Exception e) {
                        break;
                    }
                }
                SettingsActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(!wifiName.isEmpty()) {
                        wifiListHeading.setText(wifiName.size()+" Available Wifi");
                        availableWifiList.setVisibility(View.VISIBLE);
                        wifiAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.settings_adapter, R.id.bluetoothName, wifiName);
                        availableWifiList.setAdapter(wifiAdapter);
                        wifiAdapter.notifyDataSetChanged();
                    }else{
                        wifiListHeading.setText("No Networks Found");
                    }
                }
            });


            }

        };

    }

}