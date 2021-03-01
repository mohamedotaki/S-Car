package com.example.s_car;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddEventActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener,TimePickerDialog.OnTimeSetListener{

    EditText eventTitle,address1,town,county;
    TextView date;
    SwitchCompat repeatDailySwitch;
    boolean repeatDaily;
    ImageButton calendarButton;
    Button addEvent;
    Calendar calendar,time;
    String datePicked = "",timePicked ="";
    int hr=0,min=0;
    TimePickerDialog timePickerDialog;
    Event event = new Event();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);
        eventTitle = findViewById(R.id.eventTitleEditTextAddEvent);
        address1 = findViewById(R.id.address1EditTextAddEvent);
        town = findViewById(R.id.townEditTextAddEvent);
        county = findViewById(R.id.countyEditTextAddEvent);
        repeatDailySwitch = findViewById(R.id.repeatDailySwitchAddEvent);
        date = findViewById(R.id.dateTextViewAddEvent);
        calendarButton =findViewById(R.id.dateImageButtonAddEvent);
        addEvent = findViewById(R.id.addEventButtonAddEvent);

        Intent intent = getIntent();
        event.setId(intent.getIntExtra("eventId",0));
        event.setTitle(intent.getStringExtra("eventTitle"));
        event.setDate(intent.getStringExtra("eventDate"));
        event.setTime(intent.getStringExtra("eventTime"));
        event.setAddress1(intent.getStringExtra("eventAddress1"));
        event.setTown(intent.getStringExtra("eventTown"));
        event.setCounty( intent.getStringExtra("eventCounty"));
        if(event.getId() != 0){
            setEventToEdit();
        }


        repeatDailySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    repeatDaily = true;
                    date.setText("");
                    datePicked="Daily";
                    timePicked="";

                }else{
                    repeatDaily = false;
                    date.setText("");
                    datePicked="";
                    timePicked="";
                }
            }
        });

        calendarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePickerDialog = new TimePickerDialog(AddEventActivity.this,AddEventActivity.this,12,0,false);
                timePickerDialog.updateTime(hr,min);
                if(repeatDaily){
                    timePickerDialog.show();
                }else{
                    calendar = Calendar.getInstance();
                    DatePickerDialog datePickerDialog = new DatePickerDialog(AddEventActivity.this, AddEventActivity.this,
                            calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
                    datePickerDialog.show();
                }



            }
        });

        addEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    event.setOwnerId(StartupActivity.oo.getId());
                    event.setTitle(Encryption.encrypt(eventTitle.getText().toString()));
                    event.setAddress1(Encryption.encrypt(address1.getText().toString()));
                    event.setTown(Encryption.encrypt(town.getText().toString()));
                    event.setCounty(Encryption.encrypt(county.getText().toString()));
                    event.setDate(Encryption.encrypt(datePicked));
                    event.setTime(Encryption.encrypt(timePicked));
                }catch (Exception e){}
                new addEvent().execute(event);
            }
        });


    }
    public void setEventToEdit(){
        eventTitle.setText(event.getTitle());
        address1.setText(event.getAddress1());
        town.setText(event.getTown());
        county.setText(event.getCounty());
        timePicked = event.getTime();
        datePicked = event.getDate();
        date.setText(datePicked+" at "+timePicked);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        hr = hourOfDay;
        min = minute;
        time = Calendar.getInstance();
        time.set(0,0,0,hr,min);
        timePicked = (String) DateFormat.format("hh:mm aa",time);
        date.setText(datePicked +" at "+ timePicked);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date currentDate = Calendar.getInstance().getTime();
        calendar.set(year,month,dayOfMonth);
        Date datePicked = calendar.getTime();

        if(currentDate.after(datePicked)) {
            Toast.makeText(AddEventActivity.this, "Please Pick Date in the Future", Toast.LENGTH_SHORT).show();
        }else{
            String datePickedFormat = dateFormat.format(datePicked);
            this.datePicked = datePickedFormat;
            timePickerDialog.show();
        }
    }

    class addEvent extends AsyncTask<Event , Void ,Boolean> {

        @Override
        protected Boolean doInBackground(Event... events) {

            Event event = events[0];
            try {
                ObjectOutputStream os = null;
                ObjectInputStream ois = null;
                String line = null;
                URL url = new URL("http://192.168.1.26:8080/S_Car_Server_war_exploded/" + "AddEvent");
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setDoOutput(true);
                con.setDoInput(true);
                con.setUseCaches(false);
                con.setDefaultUseCaches(false);
                // Specify the content type that we will send binary data
                con.setRequestProperty("Content-Type", "application/octet-stream");

                os = new ObjectOutputStream(con.getOutputStream());
                os.writeObject(event);
                os.flush();
                os.close();

                ois = new ObjectInputStream(con.getInputStream());
                boolean result = ois.readBoolean();
                return result;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if(result){
                Toast.makeText(AddEventActivity.this, "Event Inserted", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                setResult(Activity.RESULT_OK,intent);
                finish();
            }
        }
    }
}