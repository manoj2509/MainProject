package edu.scu.mparihar.mainproject;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mj on 22-May-16.
 */
public class ProfileFragment extends Fragment {
    public ProfileFragment() {
        // Required empty public constructor
    }

    public Bundle b;
    List<ProfileData> data = new ArrayList<>();
    private RecyclerView mRecyclerView;
    public static ProfileRecyclerViewAdapter mRecyclerviewAdapterProfile;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO add data also.
        b = getArguments();
        data = b.getParcelableArrayList("ProfileData");
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_profile, container, false);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.profile_recycler_view);
//        mRecyclerView.setHasFixedSize(true);

        mRecyclerviewAdapterProfile = new ProfileRecyclerViewAdapter(data);
        if (data.size() > 0) {
            TextView textView = (TextView) v.findViewById(R.id.profile_default);
            textView.setVisibility(View.INVISIBLE);
        }
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mRecyclerView.addItemDecoration(new DividerItemDecoration(getResources().getDrawable(R.drawable.divider_drawable)));

        mRecyclerView.setAdapter(mRecyclerviewAdapterProfile);
        mRecyclerviewAdapterProfile.notifyDataSetChanged();

        return v;
    }
}
