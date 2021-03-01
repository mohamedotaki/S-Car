package com.example.s_car;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
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
    public View getView(final int index, View view, ViewGroup viewGroup) {
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
                ImageButton editButton = (ImageButton)view.findViewById(R.id.editButtonEventView);
                try {
                    eventTitle.setText(Encryption.decrypt(((Event)eventsList.get(index)).getTitle()));
                    eventTime.setText(Encryption.decrypt(((Event)eventsList.get(index)).getTime()));
                    eventLocation.setText((((Event)eventsList.get(index)).getFullAddress()));
                    editButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                Intent intent = new Intent(context,AddEventActivity.class);
                                intent.putExtra("eventId", ((Event) eventsList.get(index)).getId());
                                intent.putExtra("eventTitle", Encryption.decrypt(((Event) eventsList.get(index)).getTitle()));
                                intent.putExtra("eventDate", Encryption.decrypt(((Event) eventsList.get(index)).getDate()));
                                intent.putExtra("eventTime", Encryption.decrypt(((Event) eventsList.get(index)).getTime()));
                                intent.putExtra("eventAddress1", Encryption.decrypt(((Event) eventsList.get(index)).getAddress1()));
                                intent.putExtra("eventTown", Encryption.decrypt(((Event) eventsList.get(index)).getTown()));
                                intent.putExtra("eventCounty", Encryption.decrypt(((Event) eventsList.get(index)).getCounty()));
                                context.startActivity(intent);
                            }
                            catch (Exception e){

                            }


                        }
                    });
                    break;
                }catch (Exception e){ break;}

            case HEADER:
                TextView dateTextView = (TextView)view.findViewById(R.id.dateTextViewDateHeader);
                dateTextView.setText(eventsList.get(index).toString());
                break;
        }
       return  view;
    }
}
