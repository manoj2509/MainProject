package edu.scu.mparihar.mainproject;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Mj on 25-May-16.
 */
public class EventRecyclerViewAdapter extends RecyclerView.Adapter<EventRecyclerViewAdapter.MyViewHolder> {
    private List<EventData> eventDatas;
    List<String> Day = new ArrayList<>(Arrays.asList("Mo", "Tu", "We", "Th", "Fr", "Sa", "Su"));

    public EventRecyclerViewAdapter(List<EventData> demoData) {
        this.eventDatas = demoData;
    }
    // receive eventDatas objects here. Data required to be called outside here.
    @Override
    public EventRecyclerViewAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.event_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        EventData eventData1 = eventDatas.get(position);
        holder.entryName.setText(eventData1.getName());
        // TODO check conditions and print accordingly.
        String temp, tempDate;
        if (!eventData1.getBeaconId().equalsIgnoreCase("-1")) {
            temp = "Beacon ID: " + eventData1.beaconId.toUpperCase();
            holder.entryTime.setVisibility(View.GONE);
        } else {
            temp = eventData1.getStartTime() + " to " + eventData1.getEndTime();
            if (eventData1.getRepeatFlag() == 0) {
                tempDate = eventData1.getDate();
            } else {
                int len = eventData1.getRepeatArray().length();
                String FlagToDays = "";
                String flagArray = eventData1.getRepeatArray();
                int flag;
                for (int i = 0; i < len; i++) {
                    flag = 0;
                    if (flagArray.substring(i, i + 1).equals("1")) {
                        FlagToDays += Day.get(i);
                        flag = 1;
                    }
                    if (i != len - 1 && flag == 1) {
                        FlagToDays += ", ";
                    }
                }
                tempDate = FlagToDays;
            }
            holder.entryTime.setText(tempDate);
        }
        holder.entryExtra.setText(temp);
    }

    @Override
    public int getItemCount() {
//        Log.i("In Recycler Adapter", eventDatas.get(0).getName() + "::" + eventDatas.get(1).getName());
        return eventDatas.size();
    }

    public final static class MyViewHolder extends RecyclerView.ViewHolder{

        public TextView entryName, entryTime, entryExtra;
        public MyViewHolder(View itemView) {
            super(itemView);
            entryName = (TextView) itemView.findViewById(R.id.event_list_name);
            entryTime = (TextView) itemView.findViewById(R.id.event_list_time);
            entryExtra = (TextView) itemView.findViewById(R.id.event_list_date);
//            itemView.setOnClickListener(new View.OnClickListener() {
//
////                @Override
////                public void onClick(View v) {
////                    int pos = getAdapterPosition();
////
////                    Intent intent = new Intent(itemView.getContext(), ViewActivity.class).putExtra("Position", Integer.toString(pos));
////                    itemView.getContext().startActivity(intent);
////
////                }
//            });
        }
    }
}
