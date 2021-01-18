package com.example.s_car;

import android.icu.text.CaseMap;
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
        ArrayList<Drivers> arrayList = new ArrayList<>();
        for(int i =0 ; i <10;i++) {
            Drivers d = new Drivers(i , i+"ssss", "08922011526",false,"0505151");
            arrayList.add(d);
        }
        driversAdapter = new DriversAdapter(this,arrayList);
        drivers.setAdapter(driversAdapter);
        driversAdapter.notifyDataSetChanged();

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

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}