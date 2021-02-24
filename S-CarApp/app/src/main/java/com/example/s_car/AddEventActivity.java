package com.example.s_car;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class AddEventActivity extends AppCompatActivity {

    EditText eventTitle,address1,town,county;
    TextView date;
    SwitchCompat repeatDaily;
    ImageButton calendarButton;
    Button addEvent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);
        eventTitle = findViewById(R.id.eventTitleEditTextAddEvent);
        address1 = findViewById(R.id.address1EditTextAddEvent);
        town = findViewById(R.id.townEditTextAddEvent);
        county = findViewById(R.id.countyEditTextAddEvent);
        repeatDaily = findViewById(R.id.repeatDailySwitchAddEvent);
        date = findViewById(R.id.dateTextViewAddEvent);
        calendarButton =findViewById(R.id.dateImageButtonAddEvent);
        addEvent = findViewById(R.id.addEventButtonAddEvent);

        repeatDaily.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    calendarButton.setVisibility(View.GONE);
                    date.setVisibility(View.GONE);
                }else{
                    calendarButton.setVisibility(View.VISIBLE);
                    date.setVisibility(View.VISIBLE);
                }
            }
        });


    }
}