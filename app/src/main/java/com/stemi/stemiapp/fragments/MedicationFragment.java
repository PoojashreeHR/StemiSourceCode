package com.stemi.stemiapp.fragments;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.stemi.stemiapp.R;
import com.stemi.stemiapp.activity.RegistrationActivity;
import com.stemi.stemiapp.activity.TrackActivity;
import com.stemi.stemiapp.customviews.AnswerTemplateView;
import com.stemi.stemiapp.databases.DBforUserDetails;
import com.stemi.stemiapp.model.DataPassListener;
import com.stemi.stemiapp.model.HealthQuestions;
import com.stemi.stemiapp.model.MedicineDetails;
import com.stemi.stemiapp.model.MedicineInfo;
import com.stemi.stemiapp.preference.AppSharedPreference;
import com.stemi.stemiapp.utils.AppConstants;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.ContentValues.TAG;

/**
 * Created by Pooja on 26-07-2017.
 */

public class MedicationFragment extends Fragment implements AppConstants,View.OnClickListener {
    @BindView(R.id.tv_medication_today)
    TextView tvMedicationToday;
    @BindView(R.id.bt_addNewMedicine)
    Button addNewMedicine;
    ArrayList<MedicineInfo> Amedicines;
    MyDialogFragment infoDialogFragment;

    @BindView(R.id.morningContainer)RelativeLayout morningContainer;
    @BindView(R.id.noonContainer)RelativeLayout noonContainer;
    @BindView(R.id.nightContainer)RelativeLayout nightContainer;

    @BindView(R.id.ivInfo)ImageView morningMedicineInfo;
    @BindView(R.id.ivInfo1)ImageView noonMedicineInfo;
    @BindView(R.id.ivInfo2)ImageView nightMedicineInfo;

    LinearLayout layout2;
    AppSharedPreference appSharedPreference;
    DBforUserDetails dBforUserDetails;
    long getCount;
    Boolean checkedImg = false;
    DataPassListener mCallback;

    public MedicationFragment() {
        // Required empty public constructor
    }

   /* @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        // This makes sure that the host activity has implemented the callback interface
        // If not, it throws an exception
        try
        {
            mCallback = (DataPassListener) context;
        }
        catch (ClassCastException e)
        {
            throw new ClassCastException(context.toString()+ " must implement OnImageClickListener");
        }
    }*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_medication, container, false);
        ButterKnife.bind(this, view);
        initView();
        ((TrackActivity) getActivity()).setActionBarTitle("Medication");
        return view;
    }

    public void initView() {
        dBforUserDetails = new DBforUserDetails(getActivity());
        // dBforUserDetails.removeMedicalDetails("Pooja");
        appSharedPreference = new AppSharedPreference(getActivity());
        long count = dBforUserDetails.getMedicineDetailsCount();
        Log.e(TAG, "onCreateView: Profile Count" + count);

        morningMedicineInfo.setOnClickListener(this);
        noonMedicineInfo.setOnClickListener(this);
        nightMedicineInfo.setOnClickListener(this);

        if (count > 0) {
            getMedicineDetails("Morning",morningContainer);
            getMedicineDetails("Afternoon",noonContainer);
            getMedicineDetails("Night",nightContainer);
        }

        tvMedicationToday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment dialogfragment = new DatePickerDialogClass();
                dialogfragment.show(getActivity().getFragmentManager(), "Date Picker Dialog");
            }
        });

        addNewMedicine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((TrackActivity) getActivity()).showFragment(new AddMedicineFragment());
            }
        });
    }


    public void getMedicineDetails(String time, final RelativeLayout layout){
        ArrayList<String> medicine = dBforUserDetails.getMedicine(time);
        layout2 = (LinearLayout) layout.findViewById(R.id.imageTypeLayout);
        ImageView remove_img;
        for (int i = 0; i < medicine.size(); i++) {

            final FrameLayout frameLayout = new FrameLayout(getActivity());
            FrameLayout.LayoutParams params2 = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            frameLayout.setLayoutParams(params2);

             remove_img = new ImageView(getActivity());
            remove_img.setId(R.id.checkedImg);

            final RelativeLayout imgLayout = new RelativeLayout(getActivity());
            imgLayout.setGravity(Gravity.RIGHT);
            RelativeLayout.LayoutParams params3 = new RelativeLayout.LayoutParams(80, 80);
            params3.setMargins(0,5,10,0);
            params3.addRule(RelativeLayout.ALIGN_RIGHT);
            imgLayout.setLayoutParams(params3);
            if(checkedImg) {
                remove_img.setImageResource(R.drawable.ic_unchecked);
                remove_img.setTag(0);
                imgLayout.addView(remove_img);
            }else {
                remove_img.setImageResource(R.drawable.ic_checked);
                remove_img.setTag(R.drawable.ic_checked);
                imgLayout.addView(remove_img);
            }
            int colorOfMedicine;

            String s = medicine.get(i);
            Gson gsonObj = new Gson();
            MedicineDetails medicineDetails = gsonObj.fromJson(s, MedicineDetails.class);
            Log.e(TAG, "onCreateView: " + medicineDetails);

            colorOfMedicine = medicineDetails.getMedicineColor();

            if (medicineDetails.getMedicineMorning().equals("Morning")) {
                String medicineType = medicineDetails.getMedicineType();
                colorOfMedicine = medicineDetails.getMedicineColor();
                ImageView image = setMedicineColor(medicineType, colorOfMedicine);
                frameLayout.addView(image);
                frameLayout.addView(imgLayout);
                layout2.addView(frameLayout);
                //morningContainer.addView(child);
            }else if (medicineDetails.getMedicineAfternoon().equals("Afternoon")) {
                String medicineType = medicineDetails.getMedicineType();
                colorOfMedicine = medicineDetails.getMedicineColor();
                ImageView image = setMedicineColor(medicineType, colorOfMedicine);
                frameLayout.addView(image);
                frameLayout.addView(imgLayout);
                layout2.addView(frameLayout);
                //  noonContainer.addView(child);

            }else if (medicineDetails.getMedicineNight().equals("Night")) {
                String medicineType = medicineDetails.getMedicineType();
                colorOfMedicine = medicineDetails.getMedicineColor();
               // setMedicineColor(medicineType, colorOfMedicine);
                ImageView image = setMedicineColor(medicineType, colorOfMedicine);
                frameLayout.addView(image);
                frameLayout.addView(imgLayout);
                layout2.addView(frameLayout);
            }

            final ImageView finalRemove_img = remove_img;
            frameLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(checkedImg){
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            finalRemove_img.setImageDrawable(getResources().getDrawable(R.drawable.ic_checked));
                        }
                        finalRemove_img.setTag(0);
                        checkedImg = false;
                    }else {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            finalRemove_img.setImageDrawable(getResources().getDrawable(R.drawable.ic_unchecked));
                        }
                        finalRemove_img.setTag(R.drawable.ic_checked);
                        checkedImg = true;
                    }

                       /* if(remove_img.getTag().equals(R.drawable.ic_checked)){
                            remove_img.getResources().getDrawable(R.drawable.ic_unchecked);
                            remove_img.setTag(0);
                        }else {
                            remove_img.getResources().getDrawable(R.drawable.ic_checked);
                            remove_img.setTag(R.drawable.ic_checked);

                        }*/
                    }
            });
        }
    }


    public ImageView setMedicineColor(String medicineType, int colorOfMedicine) {
        ImageView image = new ImageView(getActivity());
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(60,60);
        params.setMargins(10, 20, 10, 20);
        image.setLayoutParams(params);

        if (medicineType.equals("Tablet")) {
            image.setBackgroundResource(0);
            image.setBackgroundResource(R.drawable.ic_tablet);
            Drawable drawable = image.getBackground().mutate();
            drawable.clearColorFilter();
            drawable.setColorFilter(new PorterDuffColorFilter(colorOfMedicine, PorterDuff.Mode.SRC_IN));

//            layout2.addView(frameLayout);
        } else {
            Log.e(TAG, "onCreateView: " + "NO TABLET ");
        }

        if (medicineType.contains("Capsule")) {
            image.setBackgroundResource(0);
            image.setBackgroundResource(R.drawable.ic_capsule);
            Drawable drawable = image.getBackground().mutate();
            drawable.clearColorFilter();
            drawable.setColorFilter(new PorterDuffColorFilter(colorOfMedicine, PorterDuff.Mode.SRC_IN));

        }
        if (medicineType.contains("Syrup")) {
            image.setBackgroundResource(0);
            image.setBackgroundResource(R.drawable.ic_syrup);
            Drawable drawable = image.getBackground().mutate();
            drawable.clearColorFilter();
            drawable.setColorFilter(new PorterDuffColorFilter(colorOfMedicine, PorterDuff.Mode.SRC_IN));
//            frameLayout.addView(image);
//            frameLayout.addView(imgLayout);
//            layout2.addView(frameLayout);
          //  layout.addView(image);
        }

        /*frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });*/
        return image;


    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.ivInfo:
                //dBforUserDetails.getMedicine("Morning");
                ArrayList<String> Mmedicine = dBforUserDetails.getMedicine("Morning");
                ArrayList<MedicineDetails> medicines = new ArrayList<>();
                for (int i = 0; i < Mmedicine.size(); i++) {
                    MedicineDetails medicineInfo = new MedicineDetails();

                    String s = Mmedicine.get(i);
                    Gson gsonObj = new Gson();
                    MedicineDetails medicineDetails = gsonObj.fromJson(s, MedicineDetails.class);
                    Log.e(TAG, "onCreateView: " + medicineDetails);

                    medicineInfo = medicineDetails;
                    medicines.add(medicineInfo);

                }
                infoDialogFragment = new MyDialogFragment(medicines, "MORNING MEDICINES", 1);
                infoDialogFragment.setCancelable(false);
                infoDialogFragment.show(getActivity().getFragmentManager(), "Medicine Info");
                break;

            case R.id.ivInfo1:
                ArrayList<String> Amedicine =  dBforUserDetails.getMedicine("Afternoon");
                ArrayList<MedicineDetails> Amedicines = new ArrayList<>();
                for (int i = 0; i < Amedicine.size(); i++) {
                    MedicineDetails AmedicineInfo = new MedicineDetails();

                    String s = Amedicine.get(i);
                    Gson gsonObj = new Gson();
                    MedicineDetails medicineDetails = gsonObj.fromJson(s, MedicineDetails.class);
                    Log.e(TAG, "onCreateView: " + medicineDetails);

                    AmedicineInfo = medicineDetails;
                    Amedicines.add(AmedicineInfo);
                }
                infoDialogFragment = new MyDialogFragment(Amedicines,"AFTERNOON MEDICINES",2);
                infoDialogFragment.setCancelable(false);
                infoDialogFragment.show(getActivity().getFragmentManager(), "Medicine Info");
                break;

            case R.id.ivInfo2:
                ArrayList<String> Nmedicine =  dBforUserDetails.getMedicine("Night");
                if(Nmedicine.size() > 0) {
                    ArrayList<MedicineDetails> Nmedicines = new ArrayList<>();
                    for (int i = 0; i < Nmedicine.size(); i++) {
                        MedicineDetails NmedicineInfo = new MedicineDetails();

                        String s = Nmedicine.get(i);
                        Gson gsonObj = new Gson();
                        MedicineDetails medicineDetails = gsonObj.fromJson(s, MedicineDetails.class);
                        Log.e(TAG, "onCreateView: " + medicineDetails);

                        NmedicineInfo = medicineDetails;
                        Nmedicines.add(NmedicineInfo);
                    }
                    infoDialogFragment = new MyDialogFragment(Nmedicines, "NIGHT MEDICINES", 3);
                    infoDialogFragment.setCancelable(false);
                    infoDialogFragment.show(getActivity().getFragmentManager(), "Medicine Info");
                }
                break;
        }
    }

    public class DatePickerDialogClass extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datepickerdialog = new DatePickerDialog(getActivity(),
                    AlertDialog.THEME_DEVICE_DEFAULT_LIGHT, this, year, month, day);

            return datepickerdialog;
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            Date parseDate = null;
            String Date1 = day + "-" + (month + 1) + "-" + year;
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            try {
                parseDate = dateFormat.parse(Date1);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            String stDate = dateFormat.format(parseDate); //2016/11/16 12:08:43
            Log.e("Comparing Date :", " Your date is correct");
            tvMedicationToday.setText(stDate);
        }
    }

    Button saveButton;
    @SuppressLint("ValidFragment")
    public class MyDialogFragment extends DialogFragment {
        private RecyclerView mRecyclerView;
        ArrayList<MedicineDetails> info;
        String time;
        int id;
      //  private MedicineInfoRecycler adapter;
        // this method create view for your Dialog
        MyDialogFragment(ArrayList<MedicineDetails> medicineInfo,String session,int id){
            this.info = medicineInfo;
            this.time = session;
            this.id = id;
        }
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            //inflate layout with recycler view
            View v = inflater.inflate(R.layout.custom_dialog_fragment, container, false);
            saveButton = (Button) v.findViewById(R.id.saveButton);

            TextView heading = (TextView)v.findViewById(R.id.heading);
            TextView noItems = (TextView)v.findViewById(R.id.ifNoItems);

            Button closeButton = (Button) v.findViewById(R.id.closeButton);
            heading.setText(time);
            final MedicineInfoRecycler adapter = new MedicineInfoRecycler(getActivity(),info);

            saveButton.setEnabled(false);

            if(info.size() == 0){
                noItems.setVisibility(View.VISIBLE);
            }else {
                noItems.setVisibility(View.GONE);
                mRecyclerView = (RecyclerView) v.findViewById(R.id.infoRecycler);
                mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                //setadapter
                mRecyclerView.setAdapter(adapter);

            }
            closeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    infoDialogFragment.dismiss();
                }
            });

            saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ArrayList<MedicineDetails> medDetails = adapter.getList();
                    if(id == 1){
                        dBforUserDetails.removeMedicine(medDetails,getActivity(),1);
                        infoDialogFragment.dismiss();

                    }else if(id == 2){
                        dBforUserDetails.removeMedicine(medDetails,getActivity(),2);
                        infoDialogFragment.dismiss();

                    }else if(id==3){
                        dBforUserDetails.removeMedicine(medDetails,getActivity(),3);
                        infoDialogFragment.dismiss();
                    }
                    infoDialogFragment.setCancelable(false);
                    ((TrackActivity) getActivity()).showFragment(new MedicationFragment());
                }

            });
            //get your recycler view and populate it.
            return v;
        }
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    public class MedicineInfoRecycler extends RecyclerView.Adapter<MedicineInfoRecycler.MyViewHolder> {
        Context mContext;
        ArrayList<MedicineDetails> medicineInfo;
        ArrayList<MedicineDetails> deletedData = new ArrayList<>();
        ArrayList<MedicineDetails> editData = new ArrayList<>();
        DataPassListener mCallback = (DataPassListener) getActivity();

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView name;
            public RelativeLayout img;
            ImageView editImage, deleteImage;

            public MyViewHolder(View view) {
                super(view);
                name = (TextView) view.findViewById(R.id.textView8);
                img = (RelativeLayout) view.findViewById(R.id.ivMedicine);
                editImage = (ImageView) view.findViewById(R.id.ivEdit);
                deleteImage = (ImageView) view.findViewById(R.id.ivDelete);

                // answerTemplateView = (AnswerTemplateView) view.findViewById(R.id.answerLayout);
            }
        }

        public MedicineInfoRecycler(Context context, ArrayList<MedicineDetails> info) {
            this.medicineInfo = info;
            this.mContext = context;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.dialog_single_row, parent, false);

            return new MedicineInfoRecycler.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {


                ImageView imageView = setMedicineColor(medicineInfo.get(position).getMedicineType(),medicineInfo.get(position).getMedicineColor());
                holder.img.addView(imageView);
                holder.name.setText(medicineInfo.get(position).getMedicineName());

                holder.editImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        editData.add(medicineInfo.get(position));
                        mCallback.passData(editData);
                        infoDialogFragment.dismiss();
                        //((TrackActivity) getActivity()).showFragment(new AddMedicineFragment());

                    }
                });
                holder.deleteImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(position == medicineInfo.size()){
                            deletedData.add(medicineInfo.get(position-1));
                            medicineInfo.remove(position-1);
                            notifyItemRemoved(position-1);
                            saveButton.setTextColor(getResources().getColor(R.color.appBackground));
                            saveButton.setEnabled(true);
                        }else {
                            deletedData.add(medicineInfo.get(position));
                            medicineInfo.remove(position);
                            notifyItemRemoved(position);
                            saveButton.setTextColor(getResources().getColor(R.color.appBackground));
                            saveButton.setEnabled(true);
                        }
                    }
                });
        }

        public  ArrayList<MedicineDetails> getList(){
            return deletedData;
        }

        @Override
        public int getItemCount() {
            return medicineInfo.size();
        }
    }

}
