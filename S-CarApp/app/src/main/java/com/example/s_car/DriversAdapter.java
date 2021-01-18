package com.example.s_car;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;

public class DriversAdapter extends BaseAdapter {


    ArrayList<Drivers> customers = new ArrayList<Drivers>();
    Context context;
    String CN, CID;

    public DriversAdapter(Context context, ArrayList<Drivers> customers) {
        this.customers = customers;
        this.context = context;
    }

    @Override
    public int getCount() {
        return customers.size();
    }

    @Override
    public Drivers getItem(int index) {
        return customers.get(index);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int index, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(context).inflate(R.layout.drivers_addapter, viewGroup, false);
        ImageView driverImage = (ImageView) view.findViewById(R.id.driverImageAdapter);
        TextView name = (TextView) view.findViewById(R.id.driverNameTextViewAdapter);
        TextView validTill = (TextView) view.findViewById(R.id.validTillTextViewAdapter);

        final Drivers customer = getItem(index);

        name.setText(customer.getName());

        return view;
    }

}