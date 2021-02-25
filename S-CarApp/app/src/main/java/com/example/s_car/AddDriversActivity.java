package com.example.s_car;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
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
import android.widget.ListView;
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
    ImageButton openDateButton,addImageButton;
    Calendar calendar;
    Button addDriverButton;
    Driver driver = new Driver() ;
    String datePicked = null;
    ListView imageListView ;
    ImagesAdapter imagesAdapter;
    int choseImage;
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
        addImageButton = findViewById(R.id.imageButtonAddDriver);
        imageListView = findViewById(R.id.imageListViewAddDriver);



        openDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar = Calendar.getInstance();
                DatePickerDialog datePickerDialog = new DatePickerDialog(AddDriversActivity.this, AddDriversActivity.this,
                        calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });

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

        addDriverButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!name.getText().toString().isEmpty() && !emailAddress.getText().toString().isEmpty()&&!phoneNumber.getText().toString().isEmpty()){
                    try {
                        if(driverType.getSelectedItem().toString().equalsIgnoreCase("Add Driver Without Time")){
                            driver.setDrivingPermission(null);
                        }else{
                            if(datePicked.isEmpty()) throw new Exception("Empty Date");
                            driver.setDrivingPermission(datePicked);
                        }
                        driver.setName(getEncryptedText(name));
                        driver.setCarKey(StartupActivity.oo.getCarKey());
                        driver.setCarNumber(StartupActivity.oo.carNumber);
                        driver.setEmailAddress(getEncryptedText(emailAddress));
                        driver.setOwnerId(StartupActivity.oo.getId());
                        driver.setPassword(Encryption.encrypt("kkk"));
                        driver.setPhoneNumber(getEncryptedText(phoneNumber));
                        driver.setImageId(choseImage);
                        new addDriver().execute(driver);
                    }catch (Exception e){
                    }
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
              if(driverType.getSelectedItem().toString().equalsIgnoreCase("Add Driver Without Time")){
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
    public void setDriverForEdit(Driver driver){
        addImageButton.setImageResource(driver.getImageId());
        name.setText(driver.getName());
        emailAddress.setText(driver.getEmailAddress());
        phoneNumber.setText(driver.getPhoneNumber());
        date.setText(driver.getDrivingPermission());

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
            this.datePicked = datePickedFormat;
        }
    }

    class addDriver extends AsyncTask<Driver , Void ,String> {

        @Override
        protected String doInBackground(Driver... drivers) {

            Driver driver = drivers[0];
            try {
                ObjectOutputStream os = null;
                ObjectInputStream ois = null;
                String line = null;
                URL url = new URL("http://192.168.1.26:8080/S_Car_Server_war_exploded/" + "AddDriver");
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setDoOutput(true);
                con.setDoInput(true);
                con.setUseCaches(false);
                con.setDefaultUseCaches(false);
                // Specify the content type that we will send binary data
                con.setRequestProperty("Content-Type", "application/octet-stream");

                os = new ObjectOutputStream(con.getOutputStream());
                os.writeObject(driver);
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

        @Override
        protected void onPostExecute(String s) {
            Toast.makeText(AddDriversActivity.this, s, Toast.LENGTH_SHORT).show();
            if(s.equalsIgnoreCase("Driver was added")){
                Intent intent = new Intent();
                setResult(Activity.RESULT_OK,intent);
                finish();
            }
        }
    }

}