package com.example.s_car;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Path;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;

public class DriversAdapter extends BaseAdapter {


    ArrayList<Driver> customers = new ArrayList<Driver>();
    Context context;
    String CN, CID;

    public DriversAdapter(Context context, ArrayList<Driver> customers) {
        this.customers = customers;
        this.context = context;
    }

    @Override
    public int getCount() {
        return customers.size();
    }

    @Override
    public Driver getItem(int index) {
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

        final Driver customer = getItem(index);

        try {

            name.setText(Encryption.decrypt(customer.getName()));
            driverImage.setImageResource(customer.getImageId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        validTill.setText(customer.getDrivingPermission());

        return view;
    }

}