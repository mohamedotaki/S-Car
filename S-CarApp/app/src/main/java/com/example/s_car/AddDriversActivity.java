package com.example.s_car;

import android.app.DatePickerDialog;
import android.os.AsyncTask;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddDriversActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{
    Spinner driverType;
    EditText emailAddress , phoneNumber , name ;
    TextView date;
    ImageButton openDateButton;
    Calendar calendar;
    Button addDriverButton;
    User driver  = new User();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_drivers);
////////////////////////////////////
        driverType = (Spinner) findViewById(R.id.adminSpinnerAddDriver);
        emailAddress = (EditText) findViewById(R.id.emailAddressEditTextAddDriver);
        phoneNumber = (EditText) findViewById(R.id.phoneNumberEditTextAddDriver);
        name = (EditText) findViewById(R.id.nameEditTextAddDriver);
        date = (TextView) findViewById(R.id.dateTextViewAddDriver);
        openDateButton = (ImageButton) findViewById(R.id.dateImageButtonAddDriver);
        addDriverButton = (Button) findViewById(R.id.addDriverButtonAddDriver);



        openDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar = Calendar.getInstance();
                DatePickerDialog datePickerDialog = new DatePickerDialog(AddDriversActivity.this, AddDriversActivity.this,
                        calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });

        addDriverButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!name.getText().toString().isEmpty() && !emailAddress.getText().toString().isEmpty()&&!phoneNumber.getText().toString().isEmpty()
            && !date.getText().toString().isEmpty()){
                    try {
                        driver.setDrivingPermission( (Date)date.getText());
                        driver.setName(getEncryptedText(name));
                        driver.setOwner(false);
                        driver.setCarKey(StartupActivity.oo.getCarKey());
                        driver.setCarNumber(StartupActivity.oo.carNumber);
                        driver.setEmailAddress(getEncryptedText(emailAddress));
                        driver.setPhoneNumber(getEncryptedText(phoneNumber));
                    }catch (Exception e){

                    }
                    new addDriver().execute(driver);

                }
            }
        });




        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.add_driver_spinner, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        driverType.setAdapter(adapter);

        driverType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
              if(driverType.getSelectedItem().toString().equalsIgnoreCase("admin")){
                  openDateButton.setVisibility(View.GONE);
                  date.setVisibility(View.GONE);
              }else {
                  openDateButton.setVisibility(View.VISIBLE);
                  date.setVisibility(View.VISIBLE);
              }

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    String getEncryptedText(EditText text) throws Exception {
        return Encryption.encrypt(text.getText().toString());
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date currentDate = Calendar.getInstance().getTime();
        calendar.set(year,month,dayOfMonth);
        Date datePicked = calendar.getTime();

        if(currentDate.after(datePicked)) {
            Toast.makeText(AddDriversActivity.this, "Please Pick Date in the Future", Toast.LENGTH_SHORT).show();
        }else{
            String datePickedFormat = dateFormat.format(datePicked);
            date.setText(datePickedFormat );
        }




    }

    class addDriver extends AsyncTask<User , Void ,String> {

        @Override
        protected String doInBackground(User... users) {

            User user = users[0];
            try {
                ObjectOutputStream os = null;
                ObjectInputStream ois = null;
                String line = null;
                URL url = new URL("http://192.168.1.5:8080/S_Car_Server_war_exploded/" + "AddDriver");
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setDoOutput(true);
                con.setDoInput(true);
                con.setUseCaches(false);
                con.setDefaultUseCaches(false);
                // Specify the content type that we will send binary data
                con.setRequestProperty("Content-Type", "application/octet-stream");

                os = new ObjectOutputStream(con.getOutputStream());
                os.writeObject(user);
                os.flush();
                os.close();

                ois = new ObjectInputStream(con.getInputStream());
                String result = (String) ois.readObject();
                return result;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

    }

}