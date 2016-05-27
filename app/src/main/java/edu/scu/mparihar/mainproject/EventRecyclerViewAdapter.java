package edu.scu.mparihar.mainproject;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Mj on 25-May-16.
 */
public class EventRecyclerViewAdapter extends RecyclerView.Adapter<EventRecyclerViewAdapter.MyViewHolder> {
    private List<EventData> EventData;

    public EventRecyclerViewAdapter(List<EventData> demoData) {
        this.EventData = demoData;
    }
    // receive EventData objects here. Data required to be called outside here.
    @Override
    public EventRecyclerViewAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.event_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        EventData eventData1 = EventData.get(position);
        holder.entryName.setText(eventData1.getName());
        // TODO check conditions and print accordingly.
        String temp = eventData1.getStartTime() + " to " + eventData1.getEndTime();
        holder.entryTime.setText(temp);
    }

    @Override
    public int getItemCount() {
//        Log.i("In Recycler Adapter", EventData.get(0).getName() + "::" + EventData.get(1).getName());
        return EventData.size();
    }

    public final static class MyViewHolder extends RecyclerView.ViewHolder{

        public TextView entryName, entryTime;
        public MyViewHolder(View itemView) {
            super(itemView);
            entryName = (TextView) itemView.findViewById(R.id.event_list_name);
            entryTime = (TextView) itemView.findViewById(R.id.event_list_time);
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
