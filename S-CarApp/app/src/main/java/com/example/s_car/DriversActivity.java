package com.example.s_car;

import android.app.Activity;
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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class DriversActivity extends AppCompatActivity {
    ListView driversListView;
    DriversAdapter driversAdapter;
    Button deleteDriverButton,editDriverButton;
    int addDriverActivity = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drivers);

        ////////////////////
        driversListView = (ListView) findViewById(R.id.driversListView);
        deleteDriverButton = (Button) findViewById(R.id.deleteDriverDriverActivity);
        editDriverButton = (Button) findViewById(R.id.editDriverDriverActivity);

        new getDrivers().execute(StartupActivity.oo.id);

        driversListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                editDriverButton.setVisibility(View.VISIBLE);
                deleteDriverButton.setVisibility(View.VISIBLE);


            }
        });

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
                startActivityForResult(intent,addDriverActivity);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == addDriverActivity) {
            if(resultCode == Activity.RESULT_OK){
                new getDrivers().execute(StartupActivity.oo.id);
            }
        }
    }//onActivityResult

    class getDrivers extends AsyncTask<Integer, Void ,ArrayList<Driver>> {

        @Override
        protected ArrayList<Driver> doInBackground(Integer... ints) {

            int ownerId = ints[0];
            try {
                ObjectOutputStream os = null;
                ObjectInputStream ois = null;
                String line = null;
                URL url = new URL("http://192.168.1.26:8080/S_Car_Server_war_exploded/" + "GetDrivers");
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
                ArrayList<Driver> drivers = new ArrayList<>();
                while(true){
                    Driver driver = (Driver) ois.readObject();
                    if(driver.getId() == 0) break;
                    drivers.add(driver);
                }
                return  drivers;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<Driver> drivers) {
            if(drivers != null){
                driversAdapter = new DriversAdapter(getApplicationContext(),drivers);
                driversListView.setAdapter(driversAdapter);
                driversAdapter.notifyDataSetChanged();
            }else{
                Toast.makeText(DriversActivity.this, "Empty", Toast.LENGTH_SHORT).show();
            }
        }
    }

}