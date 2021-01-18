package com.example.s_car;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Register extends AppCompatActivity {

    Button registerButton;
    EditText name, password , phoneNumber, emailAddress, carNumber, conformPassword;
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

                     //Server Code




                    }else{
                        Toast.makeText(Register.this, "The passwords don't match", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(Register.this, "Can't be empty", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


}