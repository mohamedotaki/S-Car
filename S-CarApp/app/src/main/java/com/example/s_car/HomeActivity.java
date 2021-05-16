package com.example.s_car;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.ParcelUuid;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;

public class HomeActivity extends AppCompatActivity {
    Button controlButton, settingsButton, driversButton, calendarButton, mapButton, statusButton;
    TextView timeLeft, accountType, userName;
    public static BluetoothSocket bluetoothSocket;
    private User user;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        controlButton = findViewById(R.id.controlButton);
        mapButton = findViewById(R.id.mapButtonHome);
        calendarButton = findViewById(R.id.calendarButtonHomeActivity);
        settingsButton = findViewById(R.id.settingsButton);
        driversButton = findViewById(R.id.DriversButton);
        statusButton = findViewById(R.id.statusButton);
        accountType = findViewById(R.id.accountTypeTextViewHome);
        timeLeft = findViewById(R.id.timeLeftTextViewHome);
        userName = findViewById(R.id.userNameTextViewHome);

        new connectToBluetooth().execute();
        Intent intent = getIntent();
        Bundle receivedData = intent.getExtras();
        if (receivedData != null) {
            user = (User) receivedData.getSerializable("currentUser");
        }


        sharedPreferences = getSharedPreferences("settings", MODE_PRIVATE);


        try {
            userName.setText(Encryption.decrypt(user.getName()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!user.getDrivingPermission().equalsIgnoreCase("")) {
            accountType.setText("Driver");
            timeLeft.setVisibility(View.GONE);
            if(!user.getDrivingPermission().equalsIgnoreCase("No Time")) {
                timeLeft.setVisibility(View.VISIBLE);
                timeLeft.setText(calculateTimeLeft(user.getDrivingPermission()) + "");
            }
        } else {
            accountType.setText("Owner");
            timeLeft.setVisibility(View.GONE);
        }

        ///////Buttons on click listener
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("currentUser", user);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        driversButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user.getDrivingPermission().equals("")) {
                    Intent intent = new Intent(getApplicationContext(), DriversActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("currentUser", user);
                    intent.putExtras(bundle);
                    startActivity(intent);
                } else
                    Toast.makeText(HomeActivity.this, "Only Owners Have Access", Toast.LENGTH_SHORT).show();
            }
        });
        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MapActivity.class);
                startActivity(intent);
            }
        });
        calendarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user.getDrivingPermission().equals("")) {
                    Intent intent = new Intent(getApplicationContext(), CalendarActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("currentUser", user);
                    intent.putExtras(bundle);
                    startActivity(intent);
                } else
                    Toast.makeText(HomeActivity.this, "Only Owners Have Access", Toast.LENGTH_SHORT).show();
            }
        });
        controlButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (bluetoothSocket.isConnected()) {
                        Intent intent = new Intent(getApplicationContext(), ControlCar.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("currentUser", user);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    } else throw new Exception();
                } catch (Exception e) {
                    Toast.makeText(HomeActivity.this, "Please Connect to Car Using Bluetooth", Toast.LENGTH_SHORT).show();
                }
            }
        });
        statusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), StatusActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("currentUser", user);
                    intent.putExtras(bundle);
                    startActivity(intent);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        menu.removeItem(R.id.addButtonMenu);
        menu.removeItem(R.id.deleteButton);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.logoutButton) {
            sharedPreferences.edit().putBoolean("LoggedIn", false);
            finish();
            goTo(StartupActivity.class);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    static BluetoothSocket connectToDevice(BluetoothDevice device, User user) throws Exception {
        ParcelUuid[] uuids = device.getUuids();
        BluetoothSocket socket = device.createRfcommSocketToServiceRecord(uuids[0].getUuid());
        if (!socket.isConnected()) {
            socket.connect();
        }
        if (socket.isConnected()) {
           // OutputStream outputStream = socket.getOutputStream();
           // outputStream.write(1);
            //outputStream.write(user.getCarKey().getBytes());
            //outputStream.write(4);
            return socket;
        }
        return null;
    }


    private void goTo(Class activity) {
        Intent intent = new Intent(getApplicationContext(), activity);
        startActivity(intent);
    }

    private String calculateTimeLeft(String time) {
        Date currentDate = Calendar.getInstance().getTime();
        Date timeLeft = new Date(time);
        // int left = currentDate.compareTo(timeLeft);
        return time;
    }

    class connectToBluetooth extends AsyncTask<Void , Void ,Boolean> {
        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                BluetoothAdapter blueAdapter = BluetoothAdapter.getDefaultAdapter();
                String bluetoothDeviceName = sharedPreferences.getString("bluetoothDeviceName", "noName");
                if (!bluetoothDeviceName.equalsIgnoreCase("noName")) {
                    if (blueAdapter != null) {
                        if (blueAdapter.isEnabled()) {
                            Set<BluetoothDevice> bondedDevices = blueAdapter.getBondedDevices();
                            if (bondedDevices.size() > 0) {
                                Object[] devices = (Object[]) bondedDevices.toArray();
                                for (int i = 0; i < bondedDevices.size(); i++) {
                                    BluetoothDevice device = (BluetoothDevice) devices[i];
                                    if (bluetoothDeviceName.equalsIgnoreCase(device.getName())) {
                                        bluetoothSocket = connectToDevice(device, user);
                                        if (bluetoothSocket.isConnected()) {
                                            return true;
                                        }
                                    }
                                }
                            }

                        }
                    }
                } else {
                    return false;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if(result){
                Toast.makeText(HomeActivity.this, "Connected to the Car", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(HomeActivity.this, "Please connect to the car in settings", Toast.LENGTH_SHORT).show();
            }
        }
    }

}