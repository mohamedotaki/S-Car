package com.example.s_car;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Register extends AppCompatActivity {

    Button registerButton;
    EditText name, password , phoneNumber, emailAddress, carNumber, conformPassword;
    static String result= "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ///////////////////////////////Items
        registerButton = (Button) findViewById(R.id.registerButtonRegister);
        name = (EditText) findViewById(R.id.nameEditTextRegister);
        password = (EditText) findViewById(R.id.passwordEditTextRegister);
        conformPassword = (EditText) findViewById(R.id.conformPasswordEditTextRegister);
        carNumber = (EditText) findViewById(R.id.carNumberEditTextRegister);
        emailAddress = (EditText) findViewById(R.id.EmailAddressEditTextRegister);
        phoneNumber = (EditText) findViewById(R.id.phoneNumberEditTextRegister);



        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!name.getText().toString().isEmpty() && !password.getText().toString().isEmpty() && !conformPassword.getText().toString().isEmpty()
                && !carNumber.getText().toString().isEmpty() && !emailAddress.getText().toString().isEmpty() &&!phoneNumber.getText().toString().isEmpty()){
                    if(password.getText().toString().equals(conformPassword.getText().toString())){
                        try {
                            Owner owner = new Owner(getEncryptedText(name), getEncryptedText(emailAddress), getEncryptedText(phoneNumber),
                                    getEncryptedText(carNumber), getEncryptedText(password));
                             new registerUser().execute(owner);
                            Toast.makeText(Register.this, result, Toast.LENGTH_SHORT).show();
                        }catch (Exception e){ }
                    }else{
                        Toast.makeText(Register.this, "The passwords don't match", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(Register.this, "Can't be empty", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    static class registerUser extends AsyncTask<Owner, Void, Void> {

        @Override
        protected Void doInBackground(Owner... owners) {
            Owner owner = owners[0];
            try {
                ObjectInputStream ois = null;
                URL url = new URL("http://192.168.1.5:8080/S_Car_Server_war_exploded/" + "Register");
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setDoOutput(true);
                con.setDoInput(true);
                con.setUseCaches(false);
                con.setDefaultUseCaches(false);
                // Specify the content type that we will send binary data
                con.setRequestProperty("Content-Type", "application/octet-stream");

                ObjectOutputStream oos = new ObjectOutputStream(con.getOutputStream());
                oos.writeObject(owner);
                oos.flush();
                ois = new ObjectInputStream(con.getInputStream());


                result = ois.readObject().toString();
                oos.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    String getEncryptedText(EditText text) throws Exception {
        return Encryption.encrypt(text.getText().toString());
    }

}