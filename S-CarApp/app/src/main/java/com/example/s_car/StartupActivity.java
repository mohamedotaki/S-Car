package com.example.s_car;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class StartupActivity extends AppCompatActivity {

    private static final int per=1;
    private static final String TAG = "StartupActivity";
    ImageView coverImage , carImage;
    long time = 5000;
    Button register, login;
    EditText password,email;
    CheckBox rememberLogin;
    ConnectionToServer connection ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startup);

        password = (EditText) findViewById(R.id.passwordEditText);
        email = (EditText) findViewById(R.id.emailEditText);
        register = (Button) findViewById(R.id.registerButton);
        login = (Button) findViewById(R.id.loginButton);
        coverImage = (ImageView) findViewById(R.id.startupWhiteCover);
        carImage = (ImageView) findViewById(R.id.startupImageView);
        rememberLogin = (CheckBox) findViewById(R.id.rememberLogin);

        Animation(coverImage);

        VerifyPermissions();
        checkIfLoggedIn();
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!email.getText().toString().isEmpty() && !password.getText().toString().isEmpty() ){
                   new verifyLogin().execute(email.getText().toString(),password.getText().toString());
                    goTo(HomeActivity.class);
                    email.getText().clear();
                    password.getText().clear();
                    finish();

                }else{
                    Toast.makeText(StartupActivity.this,"Email & Password can't be empty",Toast.LENGTH_SHORT).show();
                }


            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goTo(Register.class);
            }
        });





    }
    private void goTo(Class activity){
        try {
            Intent intent = new Intent(getApplicationContext(),activity);
            startActivity(intent);
        }catch (Exception e){}

    }
    public void Animation(ImageView image){
        ObjectAnimator animator = ObjectAnimator.ofFloat(image,"x",1000f);
        animator.setDuration(time);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(animator);
        animatorSet.start();
    }

    private void checkIfLoggedIn(){
        SharedPreferences preferences = getSharedPreferences("CheckBox",MODE_PRIVATE);
        String checkBox = preferences.getString("remember","");
        if(checkBox.equals("true")){
           // GoMain();
        }else if (checkBox.equals("false")){
            Toast.makeText(StartupActivity.this,"Please Login",Toast.LENGTH_SHORT).show();

        }
    }

    public void checkLogin (){
        Runnable runnable = new Runnable(){
            public void run() {
                try {
                    InputStream is = null;
                    String result=null;
                    String line = null;
                    URL url = new URL("http://192.168.1.5/S_Car_Server_war_exploded/" + "Login");
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con.setDoInput(true);
                    con.setRequestMethod("GET");
                    is = new BufferedInputStream(con.getInputStream());
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
                    StringBuilder stringBuilder = new StringBuilder();
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line + "\n");
                    }
                    is.close();


                    result = stringBuilder.toString();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }

    private void VerifyPermissions() {
        Log.d( TAG, "verifyPermissions: asking user for permissions");
        String[] permissions = {
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.BLUETOOTH,
                Manifest.permission.BLUETOOTH_ADMIN};
        if (    ContextCompat.checkSelfPermission(this.getApplicationContext(), permissions[0]) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this.getApplicationContext(), permissions[1]) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this.getApplicationContext(), permissions[2]) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this.getApplicationContext(), permissions[3]) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this.getApplicationContext(), permissions[4]) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this.getApplicationContext(), permissions[5]) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this.getApplicationContext(), permissions[6]) == PackageManager.PERMISSION_GRANTED){
        } else {
            ActivityCompat.requestPermissions(StartupActivity.this, permissions, per);

        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        VerifyPermissions();
    }

    static class verifyLogin extends AsyncTask<String , Void ,Boolean>{

        @Override
        protected Boolean doInBackground(String... strings) {

            String email = strings[0];
            try {
                ObjectOutputStream os = null;
                ObjectInputStream ois = null;
                Boolean result= false;
                String line = null;
                URL url = new URL("http://192.168.1.5:8080/S_Car_Server_war_exploded/" + "Login");
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setDoOutput(true);
                con.setDoInput(true);
                con.setUseCaches(false);
                con.setDefaultUseCaches(false);
                // Specify the content type that we will send binary data
                con.setRequestProperty("Content-Type", "application/octet-stream");

                os = new ObjectOutputStream(con.getOutputStream());
                os.writeObject(email);
                os.flush();
                os.close();

                ois = new ObjectInputStream(con.getInputStream());
                result = ois.readBoolean();
                System.out.println(result);

            } catch (Exception e) {
                e.printStackTrace();
            }



            return null;
        }
    }
}
