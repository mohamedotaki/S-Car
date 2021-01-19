package com.example.s_car;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.ParcelUuid;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.s_car.Local_Database.Local_Database;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Set;

public class HomeActivity extends AppCompatActivity {
    Button controlButton, settingsButton,driversButton;
    OutputStream outputStream;
    BluetoothSocket bluetoothSocket;
    public static Local_Database database;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        controlButton = (Button)findViewById(R.id.controlButton);
        settingsButton = (Button)findViewById(R.id.settingsButton);
        driversButton = (Button)findViewById(R.id.DriversButton);

        sharedPreferences = getSharedPreferences("settings", MODE_PRIVATE);



         // database = Room.databaseBuilder(getApplicationContext(), Local_Database.class,"S_Car_Database").build();
        //new GetData().execute();

        try {
          //  availableBluetooth();
        } catch (Exception e) {
            Toast.makeText(HomeActivity.this, "Not Connected to Car", Toast.LENGTH_SHORT).show();
        }

        ///////Buttons on click listener
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
        driversButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { goTo(DriversActivity.class);
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        menu.removeItem(R.id.addNewDriverButton);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.logoutButton:
                sharedPreferences.edit().putBoolean("LoggedIn",false);
                finish();
                goTo(StartupActivity.class);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    void availableBluetooth() throws Exception {
        BluetoothAdapter blueAdapter = BluetoothAdapter.getDefaultAdapter();
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