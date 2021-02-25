package com.example.s_car;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class EventViewAdapter extends BaseAdapter {
    ArrayList<Object> eventsList = new ArrayList<>();
    Context context;
    private static final int EVENT =0;
    private static final int HEADER =1;
    public EventViewAdapter(Context context , ArrayList<Object> events){
        this.eventsList = events;
        this.context = context;
    }
    @Override
    public int getItemViewType(int position){
        if(eventsList.get(position) instanceof Event){
            return EVENT;
        }else{
            return HEADER;
        }
    }

    @Override
    public int getCount() {
        return eventsList.size();
    }

    @Override
    public Object getItem(int position) {
        return eventsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int index, View view, ViewGroup viewGroup) {
        if(view == null){
            switch (getItemViewType(index)){
                case EVENT:
                    view = LayoutInflater.from(context).inflate(R.layout.event_view, viewGroup, false);
                    TextView eventTitle = (TextView)view.findViewById(R.id.eventTitleTextViewEventView);
                    TextView eventTime = (TextView)view.findViewById(R.id.eventTextViewEventView);
                    TextView eventLocation = (TextView)view.findViewById(R.id.eventLocationTextViewEventView);

                    break;
                case HEADER:
                    view = LayoutInflater.from(context).inflate(R.layout.date_header, viewGroup, false);
                    break;
            }
        }
        switch (getItemViewType(index)){
            case EVENT:
                TextView eventTitle = (TextView)view.findViewById(R.id.eventTitleTextViewEventView);
                TextView eventTime = (TextView)view.findViewById(R.id.eventTextViewEventView);
                TextView eventLocation = (TextView)view.findViewById(R.id.eventLocationTextViewEventView);

                eventTitle.setText(((Event)eventsList.get(index)).getTitle());
                eventTime.setText(((Event)eventsList.get(index)).getDate().toString());
                eventLocation.setText(((Event)eventsList.get(index)).getFullAddress());
                break;
            case HEADER:
                TextView dateTextView = (TextView)view.findViewById(R.id.dateTextViewDateHeader);
                dateTextView.setText(eventsList.get(index).toString());
                break;
        }
       return  view;
    }
}
