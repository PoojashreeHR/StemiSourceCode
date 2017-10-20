package com.stemi.stemiapp.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.stemi.stemiapp.R;
import com.stemi.stemiapp.activity.TrackActivity;
import com.stemi.stemiapp.model.TrackElements;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by Pooja on 24-07-2017.
 */

public class TrackFragment extends Fragment {
    private GridLayoutManager lLayout;
    public TrackFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_track, container, false);
        List<TrackElements> rowListItem = getAllItemList();
        lLayout = new GridLayoutManager(getActivity(), 2);

        RecyclerView rView = (RecyclerView) view.findViewById(R.id.trackRecycler);
        rView.setHasFixedSize(true);
        rView.setLayoutManager(lLayout);

      //  ((TrackActivity) getActivity()).setActionBarTitle("Track");

        RecyclerViewAdapter rcAdapter = new RecyclerViewAdapter(getActivity(), rowListItem);
        rView.setAdapter(rcAdapter);
        ((TrackActivity)getActivity()).getViewPager().setPagingEnabled(false);

        return view;

    }

    private List<TrackElements> getAllItemList() {

        List<TrackElements> allItems = new ArrayList<>();
        allItems.add(new TrackElements("Medication", R.drawable.btn_medication));
        allItems.add(new TrackElements("Exercise", R.drawable.btn_exercise));
        allItems.add(new TrackElements("Stress",  R.drawable.btn_stress_new));
        allItems.add(new TrackElements("Smoking", R.drawable.btn_smoking));
        allItems.add(new TrackElements("Weight", R.drawable.btn_weight));
        allItems.add(new TrackElements("Blood Test Result", R.drawable.btn_blood_test));

        return allItems;
    }

    public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.RecyclerViewHolders> {

        private List<TrackElements> itemList;
        private Context context;

        public RecyclerViewAdapter(Context context, List<TrackElements> itemList) {
            this.itemList = itemList;
            this.context = context;
        }

        @Override
        public RecyclerViewHolders onCreateViewHolder(ViewGroup parent, int viewType) {

            View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.track_row_single_layout, null);
            RecyclerViewHolders rcv = new RecyclerViewHolders(layoutView);
            return rcv;
        }

        @Override
        public void onBindViewHolder(RecyclerViewHolders holder, final int position) {
            holder.name.setText(itemList.get(position).getName());
            holder.img.setImageResource(itemList.get(position).getImage());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int i = position;
                    Log.e(TAG, "onClick: You clicked on " + position);
                    switch (i) {
                        case 0:
                            ((TrackActivity) getActivity()).showFragment(new MedicationFragment());
                            break;
                        case 1:
                            ((TrackActivity) getActivity()).showFragment(new ExerciseFragment());
                            break;
                        case 2:
                            ((TrackActivity) getActivity()).showFragment(new StressFragment());
                            break;
                        case  3:
                            ((TrackActivity) getActivity()).showFragment(new SmokingFragment());
                            break;
                        case  4:
                            ((TrackActivity) getActivity()).showFragment(new WeightFragment());
                            break;
                        case  5:
                            ((TrackActivity) getActivity()).showFragment(new BloodTestFragment());
                            break;
                    }
                }
            });

        }

        @Override
        public int getItemCount() {
            return this.itemList.size();
        }


        public class RecyclerViewHolders extends RecyclerView.ViewHolder {

            public TextView name;
            public ImageView img;

            public RecyclerViewHolders(View itemView) {
                super(itemView);
                //   itemView.setOnClickListener((View.OnClickListener) context);
                name = (TextView) itemView.findViewById(R.id.faq_txt);
                img = (ImageView) itemView.findViewById(R.id.faq_img);

            }
        }
    }

    @Override
    public void setMenuVisibility(boolean menuVisible) {
     /*   if(getActivity() != null){
            if(menuVisible){
                ((TrackActivity) getActivity()).setActionBarTitle("Track");
            }
        }*/
        super.setMenuVisibility(menuVisible);
    }

  /*  @Override
    public void onResume() {
        super.onResume();
        ((TrackActivity) getActivity()).setActionBarTitle("Track");
    }*/
}
