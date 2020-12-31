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
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class StartupActivity extends AppCompatActivity {

    private static final int per=1;
    private static final String TAG = "StartupActivity";
    ImageView imageView;
    long time = 5000;
    Button register, login;
    EditText password,email;
    CheckBox rememberLogin;
    ConnectionToServer connection = new ConnectionToServer();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startup);

        password = (EditText) findViewById(R.id.passwordEditText);
        email = (EditText) findViewById(R.id.emailEditText);
        register = (Button) findViewById(R.id.registerButton);
        login = (Button) findViewById(R.id.loginButton);
        imageView = (ImageView) findViewById(R.id.startupWhiteCover);
        rememberLogin = (CheckBox) findViewById(R.id.rememberLogin);


        Animation();
        VerifyPermissions();
        checkIfLoggedIn();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!email.getText().toString().isEmpty() && !password.getText().toString().isEmpty() ){
                    Intent intent = new Intent(getApplicationContext(),HomeActivity.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(StartupActivity.this,"Email & Password can't be empty",Toast.LENGTH_SHORT).show();
                }

                   // connection.login();

            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),Register.class);
                startActivity(intent);
            }
        });





    }
    public void Animation(){
        ObjectAnimator animator = ObjectAnimator.ofFloat(imageView,"x",1000f);
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
}