package edu.scu.mparihar.mainproject;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Mj on 26-May-16.
 */
public class ProfileRecyclerViewAdapter extends RecyclerView.Adapter<ProfileRecyclerViewAdapter.MyViewHolder>  {

    private List<ProfileData> profileDatas;

    public ProfileRecyclerViewAdapter(List<ProfileData> demoData) {
        this.profileDatas = demoData;
    }
    @Override
    public ProfileRecyclerViewAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.profile_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        ProfileData profileData1 = profileDatas.get(position);
        holder.entryName.setText(profileData1.getName());
        // TODO check conditions and print accordingly.

        if (profileData1.getType().equals("Silent")) {
            holder.entryType.setImageResource(R.drawable.ic_volume_off_50dp);
        } else if (profileData1.getType().equals("Vibrate Mode")) {
            holder.entryType.setImageResource(R.drawable.ic_vibration_24dp);
        } else {
            holder.entryType.setImageResource(R.drawable.ic_volume_up_50dp);
        }
    }

    @Override
    public int getItemCount() {
//        Log.i("In Recycler Adapter", EventData.get(0).getName() + "::" + EventData.get(1).getName());
        return profileDatas.size();
    }

    public final static class MyViewHolder extends RecyclerView.ViewHolder{

        public TextView entryName;
        public ImageView entryType;

        public MyViewHolder(View itemView) {
            super(itemView);
            entryName = (TextView) itemView.findViewById(R.id.profile_list_name);
            entryType = (ImageView) itemView.findViewById(R.id.profile_list_type);
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
