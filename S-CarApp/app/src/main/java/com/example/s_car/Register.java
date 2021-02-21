package com.example.s_car;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.UUID;

public class Register extends AppCompatActivity {

    Button registerButton;
    EditText name, password , phoneNumber, emailAddress, carNumber, conformPassword;
    static String result= "";
    ImageButton addImageButton;
    ListView imageListView ;
    ImagesAdapter imagesAdapter;
    int choseImage;
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
        addImageButton = findViewById(R.id.imageButtonRegister);
        imageListView = findViewById(R.id.imageListViewRegister);

        addImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageListView.setVisibility(View.VISIBLE);
                imagesAdapter = new ImagesAdapter(getApplicationContext());
                imageListView.setAdapter(imagesAdapter);
                imagesAdapter.notifyDataSetChanged();

            }
        });

        imageListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                choseImage =  imagesAdapter.getItem(position);
                addImageButton.setImageResource(choseImage);
                imageListView.setVisibility(View.GONE);
            }
        });


        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!name.getText().toString().isEmpty() && !password.getText().toString().isEmpty() && !conformPassword.getText().toString().isEmpty()
                && !carNumber.getText().toString().isEmpty() && !emailAddress.getText().toString().isEmpty() &&!phoneNumber.getText().toString().isEmpty()){
                    if(password.getText().toString().equals(conformPassword.getText().toString())){
                        try {
                            String keyNo = UUID.randomUUID().toString();
                            User user = new User(getEncryptedText(name), getEncryptedText(emailAddress), getEncryptedText(phoneNumber),
                                    getEncryptedText(carNumber), getEncryptedText(password),Encryption.encrypt(keyNo),choseImage);
                             new registerUser().execute(user);
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

    class registerUser extends AsyncTask<User, Void, String> {

        @Override
        protected String doInBackground(User... users) {
            User user = users[0];
            try {
                ObjectInputStream ois = null;
                URL url = new URL("http://192.168.1.26:8080/S_Car_Server_war_exploded/" + "Register");
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setDoOutput(true);
                con.setDoInput(true);
                con.setUseCaches(false);
                con.setDefaultUseCaches(false);
                // Specify the content type that we will send binary data
                con.setRequestProperty("Content-Type", "application/octet-stream");

                ObjectOutputStream oos = new ObjectOutputStream(con.getOutputStream());
                oos.writeObject(user);
                oos.flush();
                ois = new ObjectInputStream(con.getInputStream());


                result = ois.readObject().toString();
                oos.close();
                return result;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Toast.makeText(Register.this, s, Toast.LENGTH_SHORT).show();
            if (s.equalsIgnoreCase("registered")){
                finish();
            }
        }
    }

    String getEncryptedText(EditText text) throws Exception {
        return Encryption.encrypt(text.getText().toString());
    }

}