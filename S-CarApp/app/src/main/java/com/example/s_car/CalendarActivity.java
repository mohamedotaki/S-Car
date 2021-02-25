package com.example.s_car;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.ArrayList;

public class CalendarActivity extends AppCompatActivity {

    int addEventActivity =1;
    ListView eventsListView ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        eventsListView = findViewById(R.id.eventsListViewCalenderActivity);
        ArrayList<Object> events = new ArrayList<>();

        events.add(new String("01/03/2021"));
        events.add(new Event(1,"doctor","01/03/2021","12 old racecourse","ballyhaunis","mayo"));
        events.add(new Event(2,"school","01/03/2021","12 old racecourse","ballyhaunis","mayo"));
        events.add(new String("02/03/2021"));
        events.add(new Event(3,"football","02/03/2021","12 old racecourse","ballyhaunis","mayo"));
        events.add(new String("03/03/2021"));
        events.add(new Event(4,"friends","03/03/2021","12 old racecourse","ballyhaunis","mayo"));
        events.add(new String("04/03/2021"));
        events.add(new Event(5,"oooo","04/03/2021","12 old racecourse","ballyhaunis","mayo"));
        EventViewAdapter adapter = new EventViewAdapter(getApplicationContext(),events);
        eventsListView.setAdapter(adapter);
        adapter.notifyDataSetChanged();












    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        menu.removeItem(R.id.logoutButton);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.addButtonMenu:
                Intent intent = new Intent(getApplicationContext(),AddEventActivity.class);
                startActivityForResult(intent,addEventActivity);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == addEventActivity) {
            if(resultCode == Activity.RESULT_OK){
                //dp somthing if the event was added
            }
        }
    }//onActivityResult


}