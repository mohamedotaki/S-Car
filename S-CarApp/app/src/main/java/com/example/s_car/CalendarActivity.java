package com.example.s_car;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

public class CalendarActivity extends AppCompatActivity implements Serializable {

    int addEventActivity =1;
    ListView eventsListView ;
    EventViewAdapter eventAdapter;
    ArrayList<Object> finalEvents;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        eventsListView = findViewById(R.id.eventsListViewCalenderActivity);
        new getEvents().execute(StartupActivity.oo.getId());
        eventsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(finalEvents.get(position)instanceof Event){
                    Intent intent = new Intent(CalendarActivity.this, AddEventActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("EventToEdit",(Event)finalEvents.get(position));
                    intent.putExtras(bundle);
                    startActivityForResult(intent,addEventActivity);
                }
            }
        });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        menu.removeItem(R.id.logoutButton);
        menu.removeItem(R.id.deleteButton);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.addButtonMenu) {
            Intent intent = new Intent(getApplicationContext(), AddEventActivity.class);
            startActivityForResult(intent, addEventActivity);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == addEventActivity) {
            if(resultCode == Activity.RESULT_OK){
                new getEvents().execute(StartupActivity.oo.getId());
                eventAdapter.notifyDataSetChanged();
            }
        }
    }//onActivityResult


    class getEvents extends AsyncTask<Integer, Void ,ArrayList<Event>> {
        ArrayList<Date> dates = new ArrayList<>();
        boolean dailyEvents =false;
        @Override
        protected ArrayList<Event> doInBackground(Integer... ints) {
            int ownerId = ints[0];
            try {
                ObjectOutputStream os = null;
                ObjectInputStream ois = null;
                String line = null;
                URL url = new URL("http://192.168.1.3:8080/S_Car_Server_war_exploded/" + "GetEvents");
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setDoOutput(true);
                con.setDoInput(true);
                con.setUseCaches(false);
                con.setDefaultUseCaches(false);
                // Specify the content type that we will send binary data
                con.setRequestProperty("Content-Type", "application/octet-stream");
                os = new ObjectOutputStream(con.getOutputStream());
                os.writeObject(ownerId);
                os.flush();
                os.close();

                ois = new ObjectInputStream(con.getInputStream());
                ArrayList<Event> events = new ArrayList<>();
                while(true){
                    Event event = (Event) ois.readObject();
                    if(event.getId() == 0) break;
                    if(!Encryption.decrypt(event.getDate()).equalsIgnoreCase("daily")) {
                        dates.add(new SimpleDateFormat("dd/MM/yyyy").parse(Encryption.decrypt(event.getDate())));
                    }else {
                        dailyEvents =true;
                    }
                    events.add(event);
                }
                return  events;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<Event> events) {
            finalEvents = new ArrayList<>();
            Set<Date> dateSet = new LinkedHashSet<>();
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            String date="";
            try {
            if(events != null){
                Collections.sort(dates);
                dateSet.addAll(dates);
                dates.clear();
                dates.addAll(dateSet);
                if(dailyEvents){
                    finalEvents.add("Daily");
                    for(int i=0;i<events.size();i++){
                        if(Encryption.decrypt(events.get(i).getDate()).equalsIgnoreCase("Daily")) {
                            finalEvents.add(events.get(i));
                        }
                    }
                }
                for(int i =0; i<dates.size();i++){
                    date = dateFormat.format(dates.get(i));
                    finalEvents.add(date);
                    for(int x=0;x<events.size();x++){

                            if(Encryption.decrypt(events.get(x).getDate()).equalsIgnoreCase(date)){
                                finalEvents.add(events.get(x));
                            }

                    }
                }
                eventAdapter = new EventViewAdapter(CalendarActivity.this,finalEvents);
                eventsListView.setAdapter(eventAdapter);
                eventAdapter.notifyDataSetChanged();
            }else{
                Toast.makeText(CalendarActivity.this, "Empty", Toast.LENGTH_SHORT).show();
            }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}