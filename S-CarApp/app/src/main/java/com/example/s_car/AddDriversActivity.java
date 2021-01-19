package com.example.s_car;

import android.app.DatePickerDialog;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddDriversActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{
    Spinner driverType;
    EditText emailAddress , phoneNumber , name ;
    TextView date;
    ImageButton openDateButton;
    int year , month , day;
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



        openDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(AddDriversActivity.this, AddDriversActivity.this,year, month,day);
                datePickerDialog.show();
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


    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Date datePicked = new Date(year,month,dayOfMonth);
        Date currentDate = new Date(Calendar.YEAR,Calendar.MONTH,Calendar.DAY_OF_MONTH);
        if(currentDate.after(datePicked)){
            Toast.makeText(AddDriversActivity.this, "Please Pick Date in Future", Toast.LENGTH_SHORT).show();
        }
        Toast.makeText(AddDriversActivity.this, currentDate+"", Toast.LENGTH_SHORT).show();
        date.setText(year +"/"+ month +"/"+ dayOfMonth );

    }

}