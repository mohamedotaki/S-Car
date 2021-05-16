package com.example.s_car;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class StartupActivity extends AppCompatActivity {

    private static final int per = 1;
    private static final String TAG = "StartupActivity";
    ImageView coverImage, carImage;
    long time = 4000;
    DatabaseHandler db;
    Button register, login;
    EditText password, email;
    CheckBox rememberLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startup);

        db = new DatabaseHandler(this);
        password = findViewById(R.id.passwordEditText);
        email = findViewById(R.id.emailEditText);
        register = findViewById(R.id.registerButton);
        login = findViewById(R.id.loginButton);
        coverImage = findViewById(R.id.startupWhiteCover);
        carImage = findViewById(R.id.startupImageView);
        rememberLogin = findViewById(R.id.rememberLogin);

        Animation(coverImage);
        VerifyPermissions();
        checkIfLoggedIn();
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!email.getText().toString().isEmpty() && !password.getText().toString().isEmpty() && checkInternet()) {
                    new verifyLogin().execute(email.getText().toString(), password.getText().toString());
                    email.getText().clear();
                    password.getText().clear();
                } else if(!email.getText().toString().isEmpty() && !password.getText().toString().isEmpty() && !checkInternet()) {
                    try {
                        User user = db.getUser(Encryption.encrypt(email.getText().toString()),
                                Encryption.encrypt(password.getText().toString()));
                        if(user != null){
                            Toast.makeText(StartupActivity.this, "Offline Mode", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("currentUser", user);
                            intent.putExtras(bundle);
                            startActivity(intent);
                            finish();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(StartupActivity.this, "Email & Password can't be empty", Toast.LENGTH_SHORT).show();
                }
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Register.class);
                startActivity(intent);
            }
        });


    }

    public void Animation(ImageView image) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(image, "x", 1000f);
        animator.setDuration(time);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(animator);
        animatorSet.start();
    }

    private void checkIfLoggedIn() {
        SharedPreferences preferences = getSharedPreferences("CheckBox", MODE_PRIVATE);
        String checkBox = preferences.getString("remember", "");
        if (checkBox.equals("true")) {
            // GoMain();
        } else if (checkBox.equals("false")) {
            Toast.makeText(StartupActivity.this, "Please Login", Toast.LENGTH_SHORT).show();

        }
    }

    private void VerifyPermissions() {
        Log.d(TAG, "verifyPermissions: asking user for permissions");
        String[] permissions = {
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.BLUETOOTH,
                Manifest.permission.BLUETOOTH_ADMIN,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.READ_PHONE_STATE};
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), permissions[0]) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this.getApplicationContext(), permissions[1]) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this.getApplicationContext(), permissions[2]) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this.getApplicationContext(), permissions[3]) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this.getApplicationContext(), permissions[4]) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this.getApplicationContext(), permissions[5]) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this.getApplicationContext(), permissions[6]) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this.getApplicationContext(), permissions[7]) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this.getApplicationContext(), permissions[8]) == PackageManager.PERMISSION_GRANTED) {
        } else {
            ActivityCompat.requestPermissions(StartupActivity.this, permissions, per);

        }
    }

    private boolean checkInternet() {
        // code from https://stackoverflow.com/questions/4238921/detect-whether-there-is-an-internet-connection-available-on-android
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        VerifyPermissions();
    }

    class verifyLogin extends AsyncTask<String, Void, User> {
        @Override
        protected User doInBackground(String... strings) {

            String email = strings[0];
            String pass = strings[1];
            try {
                ObjectOutputStream os = null;
                ObjectInputStream ois = null;
                URL url = new URL("http://192.168.1.3:8080/S_Car_Server_war_exploded/" + "Login");
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setDoOutput(true);
                con.setDoInput(true);
                con.setUseCaches(false);
                con.setDefaultUseCaches(false);
                // Specify the content type that we will send binary data
                con.setRequestProperty("Content-Type", "application/octet-stream");

                os = new ObjectOutputStream(con.getOutputStream());
                os.writeObject(Encryption.encrypt(email));
                os.writeObject(Encryption.encrypt(pass));
                os.flush();
                os.close();
                ois = new ObjectInputStream(con.getInputStream());
                return (User) ois.readObject();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(User user) {
            if (user != null) {
                //update or add user in local database
                if(db.getUserCount()>0){
                    if(db.getUser(user.getEmailAddress(),user.getPassword()) != null) {
                        db.updateUser(user);
                    }else {
                        db.addUser(user);
                    }
                }else{
                    db.addUser(user);
                }
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("currentUser", user);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(StartupActivity.this, "Wrong Email or Password", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
