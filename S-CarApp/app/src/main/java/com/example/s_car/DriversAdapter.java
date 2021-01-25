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


    ArrayList<User> customers = new ArrayList<User>();
    Context context;
    String CN, CID;

    public DriversAdapter(Context context, ArrayList<User> customers) {
        this.customers = customers;
        this.context = context;
    }

    @Override
    public int getCount() {
        return customers.size();
    }

    @Override
    public User getItem(int index) {
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

        final User customer = getItem(index);

        try {

            name.setText(Encryption.decrypt(customer.getName()));
            int id = context.getResources().getIdentifier("car.png","Drawable" , context.getPackageName());
            driverImage.setImageResource(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        validTill.setText(customer.getDrivingPermission());

        return view;
    }

}