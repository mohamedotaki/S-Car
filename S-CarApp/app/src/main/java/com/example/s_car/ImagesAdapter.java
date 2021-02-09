package com.example.s_car;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class ImagesAdapter extends BaseAdapter {


    ArrayList<String> images = new ArrayList<String>();
    ArrayList<Integer> imageID = new ArrayList<Integer>();
    Context context;

    public ImagesAdapter(Context context) {
        this.context = context;
        images.add("car");
        images.add("blackcar");
        images.add("driver1");
        images.add("driver2");
        images.add("logo");
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public Integer getItem(int index) {
        return imageID.get(index);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int index, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(context).inflate(R.layout.image_adapter, viewGroup, false);
        final ImageView imageView;
        imageView = view.findViewById(R.id.imageButtonImageAdapter);

        int id = context.getResources().getIdentifier(images.get(index),"drawable" , context.getPackageName());
        imageID.add(id);
        imageView.setImageResource(id);




        return view;
    }

}