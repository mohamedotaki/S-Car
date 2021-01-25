package com.example.s_car;

import android.content.Intent;
import android.icu.text.CaseMap;
import android.os.AsyncTask;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class DriversActivity extends AppCompatActivity {
    ListView drivers;
    DriversAdapter driversAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drivers);

        ////////////////////
        drivers = (ListView) findViewById(R.id.driversListView);

        new getDrivers().execute(StartupActivity.oo.carKey);

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
            case R.id.addNewDriverButton:
                Intent intent = new Intent(getApplicationContext(),AddDriversActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    class getDrivers extends AsyncTask<String , Void ,ArrayList<User>> {

        @Override
        protected ArrayList<User> doInBackground(String... strings) {

            String carKey = strings[0];
            try {
                ObjectOutputStream os = null;
                ObjectInputStream ois = null;
                String line = null;
                URL url = new URL("http://192.168.1.5:8080/S_Car_Server_war_exploded/" + "GetDrivers");
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setDoOutput(true);
                con.setDoInput(true);
                con.setUseCaches(false);
                con.setDefaultUseCaches(false);
                // Specify the content type that we will send binary data
                con.setRequestProperty("Content-Type", "application/octet-stream");
                os = new ObjectOutputStream(con.getOutputStream());
                os.writeObject(carKey);
                os.flush();
                os.close();

                ois = new ObjectInputStream(con.getInputStream());
                ArrayList<User> drivers = new ArrayList<>();
                while(true){
                    User user = (User) ois.readObject();
                    if(user.getId() == 0) break;
                    drivers.add(user);
                }
                return  drivers;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<User> user) {
            if(user != null){
                driversAdapter = new DriversAdapter(getApplicationContext(),user);
                drivers.setAdapter(driversAdapter);
                driversAdapter.notifyDataSetChanged();
            }else{
                Toast.makeText(DriversActivity.this, "Empty", Toast.LENGTH_SHORT).show();
            }
        }
    }

}