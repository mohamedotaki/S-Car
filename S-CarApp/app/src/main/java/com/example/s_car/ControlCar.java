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
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
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

import com.google.android.gms.common.util.Strings;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.Set;

public class ControlCar extends AppCompatActivity {
    TextView frontSensor, backSensor, leftSensor, rightSensor;
    Handler sensorsHandler;
    Button stop;
    ImageButton forwardButton, leftButton, rightButton, reverseButton, parkingAssistButton, autoPilotButton;
    BluetoothSocket bluetoothSocket = HomeActivity.bluetoothSocket;
    OutputStream outputStream;
    Thread bluetoothThread;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control_car);
        parkingAssistButton = findViewById(R.id.parkingAssistButton);
        forwardButton = findViewById(R.id.topArrow);
        leftButton = findViewById(R.id.leftArrow);
        rightButton = findViewById(R.id.rightArrow);
        reverseButton = findViewById(R.id.bottomArrow);
        stop = findViewById(R.id.StopButton);
        autoPilotButton = findViewById(R.id.autoPilotButton);
        frontSensor = (TextView) findViewById(R.id.frontSensorTextViewControlCar);
        backSensor = (TextView) findViewById(R.id.backSensorTextViewControlCar);
        leftSensor = (TextView) findViewById(R.id.leftSensorTextViewControlCar);
        rightSensor = (TextView) findViewById(R.id.rightSensorTextViewControlCar);



        getSensorsValue(bluetoothSocket);


        try {
            outputStream = bluetoothSocket.getOutputStream();
            write("C");
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(ControlCar.this, "Couldn't connect", Toast.LENGTH_SHORT).show();
            finish();
        }
        autoPilotButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                autoPilotButton.setImageResource(R.drawable.auto_pilot_on);
                try {
                    write("A");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
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

    public void write(String s) throws IOException {
        outputStream.write(s.getBytes());
    }

    @Override
    public void onBackPressed() {
        try {
            write("E");
        } catch (IOException e) {
        }

        super.onBackPressed();
    }

    private void getSensorsValue(BluetoothSocket bluetoothSocket){
        final InputStream inputStream ;
        sensorsHandler = new Handler();
        InputStream tmpIn = null;
        try {
            tmpIn = bluetoothSocket.getInputStream();
        } catch (IOException e) {}
        inputStream = tmpIn;
         new Thread(new Runnable(){
             @Override
             public void run(){
                 int bytes =0;
                 while (true) {
                     byte[] buffer = new byte[9];

                     try {
                         bytes += inputStream.read(buffer, bytes ,buffer.length - bytes);      //read bytes from input buffer
                         if(buffer[8] == 'x') {
                             bytes =0;
                             int frontValue , rightValue, leftValue, backValue;
                             frontValue = (short) (((buffer[0]) & 0xFF) << 8 | (buffer[1]) & 0xFF);
                             backValue = (short) (((buffer[2]) & 0xFF) << 8 | (buffer[3]) & 0xFF);
                             rightValue = (short) (((buffer[4]) & 0xFF) << 8 | (buffer[5]) & 0xFF);
                             leftValue = (short) (((buffer[6]) & 0xFF) << 8 | (buffer[7]) & 0xFF); // not left
                             sensorsHandler.post(() -> {
                                changeValueColor(rightSensor,rightValue);
                                 rightSensor.setText(String.valueOf(rightValue));
                                 changeValueColor(frontSensor,frontValue);
                                 frontSensor.setText(String.valueOf(frontValue));
                                 changeValueColor(backSensor,backValue);
                                 backSensor.setText(String.valueOf(backValue));
                                 changeValueColor(leftSensor,leftValue);
                                 leftSensor.setText(String.valueOf(leftValue));


                             });
                         }

                     } catch (Exception e) {
                         e.printStackTrace();
                         break;
                     }
                 }
                 try {
                     inputStream.close();
                 } catch (IOException e) {
                     e.printStackTrace();
                 }

             }

        }).start();
    }
    void changeValueColor(TextView textView , int value){
        int red = Color.RED;
        int black = Color.BLACK;
        if(value <20 && textView.getCurrentTextColor() != red ) {
            textView.setTextColor(Color.RED);
        }else if (value > 20 && textView.getCurrentTextColor() != black) {
            textView.setTextColor(Color.BLACK);
        }
    }
}
