package com.stemi.stemiapp.fragments;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
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

import com.google.gson.Gson;
import com.stemi.stemiapp.R;
import com.stemi.stemiapp.activity.TrackActivity;
import com.stemi.stemiapp.databases.MedicineDetailsTable;
import com.stemi.stemiapp.databases.MedicineTable;
import com.stemi.stemiapp.databases.TrackMedicationDB;
import com.stemi.stemiapp.model.DataPassListener;
import com.stemi.stemiapp.model.DataSavedEvent;
import com.stemi.stemiapp.model.MedicineDetails;
import com.stemi.stemiapp.model.MedicinesTakenOrNot;
import com.stemi.stemiapp.model.MessageEvent;
import com.stemi.stemiapp.model.SetTimeEvent;
import com.stemi.stemiapp.model.TrackMedication;
import com.stemi.stemiapp.preference.AppSharedPreference;
import com.stemi.stemiapp.utils.AppConstants;
import com.stemi.stemiapp.utils.CommonUtils;
import com.stemi.stemiapp.utils.GlobalClass;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.ContentValues.TAG;

/**
 * Created by Pooja on 26-07-2017.
 */

public class MedicationFragment extends Fragment implements AppConstants, View.OnClickListener, TrackActivity.OnBackPressedListener {
    @BindView(R.id.tv_medication_today)
    TextView tvMedicationToday;
    @BindView(R.id.bt_addNewMedicine)
    Button addNewMedicine;
    MyDialogFragment infoDialogFragment;

    @BindView(R.id.morningContainer)
    RelativeLayout morningContainer;
    @BindView(R.id.noonContainer)
    RelativeLayout noonContainer;
    @BindView(R.id.nightContainer)
    RelativeLayout nightContainer;

    @BindView(R.id.ivInfo)
    ImageView morningMedicineInfo;
    @BindView(R.id.ivInfo1)
    ImageView noonMedicineInfo;
    @BindView(R.id.ivInfo2)
    ImageView nightMedicineInfo;

    String savedDate;
    LinearLayout layout2;
    AppSharedPreference appSharedPreference;
    long getCount;

    ArrayList<MedicineDetails> morningMedicineList;
    ArrayList<MedicineDetails> afternoonMedicineList;
    ArrayList<MedicineDetails> nightMedicineList;

    LinearLayout morning, afternoon, night;

    MedicineDetailsTable medicineDetailsTable;
    MedicineDetails medicineDetails;
    TrackMedicationDB trackMedicationDB;
    MedicineTable medicineTable;
    ArrayList<String> medicineWithDate;
    Boolean checkedImg = false;
    MedicineDetails medicineContains;
    DataPassListener mCallback;
    private boolean alreadySaved;

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
        medicineDetails = new MedicineDetails();
        medicineDetailsTable = new MedicineDetailsTable();
        trackMedicationDB = new TrackMedicationDB(getActivity());
        // layout2 = (LinearLayout) morningContainer.findViewById(R.id.imageTypeLayout);

        // Setting fonts
        callSavedMethod();

        long count = medicineTable.getMedicineDetailsCount();
        Log.e(TAG, "onCreateView: Profile Count" + count);


       /* if (count > 0) {
            getMedicineDetails("Morning", morningContainer);
            getMedicineDetails("Afternoon", noonContainer);
            getMedicineDetails("Night", nightContainer);
        }*/
        CommonUtils.setRobotoRegularFonts(getActivity(), tvMedicationToday);
        ((TrackActivity) getActivity()).setOnBackPressedListener(this);
        ((TrackActivity) getActivity()).setOnBackPressedListener(this);
        ((TrackActivity) getActivity()).setActionBarTitle("Medication");

        alreadySaved = false;
        return view;
    }

    public void initView() {
        medicineTable = new MedicineTable();
        // dBforUserDetails.removeMedicalDetails("Pooja");
        appSharedPreference = new AppSharedPreference(getActivity());

        morningMedicineList = new ArrayList<>();
        afternoonMedicineList = new ArrayList<>();
        nightMedicineList = new ArrayList<>();
        medicineWithDate = new ArrayList<>();


        morningMedicineInfo.setOnClickListener(this);
        noonMedicineInfo.setOnClickListener(this);
        nightMedicineInfo.setOnClickListener(this);


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


    public void callSavedMethod() {
        if (tvMedicationToday.getText().equals("Today  ")) {
            Date dt = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");// set format for date
            String todaysDate = dateFormat.format(dt); // parse it like
            savedDate = todaysDate;
        } else {
            savedDate = tvMedicationToday.getText().toString();
        }
        medicineWithDate = medicineTable.getAllMedicineDEtails(GlobalClass.userID, savedDate);


        if (medicineWithDate != null && medicineWithDate.size() > 0) {
            Log.e(TAG, "callSavedMethod: " + "CALL HERE");
            //    getMedicineDetails()
            for (int i = 0; i < medicineWithDate.size(); i++) {
                String s = medicineWithDate.get(i);
                Gson gsonObj = new Gson();
                medicineContains = gsonObj.fromJson(s, MedicineDetails.class);

                if (medicineContains.getMedicineMorning() != null) {
                    morningMedicineList.add(medicineDetails);
                    getMedicineDetails(medicineContains, "Morning", morningContainer);
                }
                if (medicineContains.getMedicineAfternoon() != null) {
                    afternoonMedicineList.add(medicineDetails);
                    getMedicineDetails(medicineContains, "Afternoon", noonContainer);
                }
                if (medicineContains.getMedicineNight() != null) {
                    nightMedicineList.add(medicineDetails);
                    getMedicineDetails(medicineContains, "Night", nightContainer);
                    // getMedicineDetails(medicineContains, "Night", nightContainer);
                }
            }
        } else {
            //  if(layout2!=null) {
            removeAllChildViews(morning);
            removeAllChildViews(afternoon);
            removeAllChildViews(night);// }
            //  morningContainer.removeAllViews();
            // noonContainer.removeAllViews();
            //  nightContainer.removeAllViews();
        }


        /*}else {
            long count = medicineTable.getMedicineDetailsCount();
            Log.e(TAG, "onCreateView: Profile Count" + count);

            if (count > 0) {
                getMedicineDetails(savedDate, morningContainer);
                getMedicineDetails(savedDate, noonContainer);
                getMedicineDetails(savedDate, nightContainer);
            }*/
        //  }

    }

    void removeAllChildViews(ViewGroup viewGroup) {
        if (viewGroup != null) {
            ViewGroup parent = viewGroup;
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                View child = viewGroup.getChildAt(i);
                if (child instanceof FrameLayout) {
                    parent.removeView(child);
                    //removeAllChildViews(((ViewGroup) child));
                } /*else {
                    viewGroup.removeView(child);
                }*/
            }
        }
    }

    ArrayList<MedicineDetails> m1 = new ArrayList<>();

    public void getMedicineDetails(final MedicineDetails medInfo, final String time, final RelativeLayout layout) {
        //final ArrayList<String> medicine = medicineTable.getMedicine(appSharedPreference.getUserId(), time);
        //for (int i = 0; i < medInfo.size(); i++) {
        morning = (LinearLayout) morningContainer.findViewById(R.id.imageTypeLayout);
        afternoon = (LinearLayout) noonContainer.findViewById(R.id.imageTypeLayout);
        night = (LinearLayout) nightContainer.findViewById(R.id.imageTypeLayout);

        String medicineTime = "";
        if (time.equals("Morning")) {
            for (int j = 0; j < medInfo.getMedicineMorning().size(); j++) {
                final FrameLayout frameLayout = new FrameLayout(getActivity());
                FrameLayout.LayoutParams params2 = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                params2.gravity = Gravity.CENTER_VERTICAL;
                frameLayout.setLayoutParams(params2);

                final ImageView remove_img;
                remove_img = new ImageView(getActivity());
                int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 15, getResources().getDisplayMetrics());
                int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 15, getResources().getDisplayMetrics());

                FrameLayout.LayoutParams params3 = new FrameLayout.LayoutParams(width, height);
                params3.gravity = Gravity.RIGHT;
                params3.setMargins(10, 15, 0, 0);
                remove_img.setId(R.id.checkedImg);
                remove_img.setLayoutParams(params3);

                int colorOfMedicine;
/*

            String s = medInfo.get(i);
            Gson gsonObj = new Gson();
            final MedicineDetails medicineDetails = gsonObj.fromJson(s, MedicineDetails.class);
            beforeAdd.add(medicineDetails);
            Log.e(TAG, "onCreateView: " + medicineDetails);
*/

                if (checkedImg) {
                    remove_img.setImageResource(R.drawable.ic_checked_1);
                    remove_img.setTag(j);
                } else {
                    remove_img.setImageResource(R.drawable.ic_unchecked_1);
                    remove_img.setTag(0);
                    //medicineDetails.setMoringChecked(false);
                    //medicineDetails.setAfternoonChecked(false);
                    //medicineDetails.setNightChecked(false);
                }

                //  MedicineDetails meds = medInfo.get(i);
                final ArrayList<MedicinesTakenOrNot> medTaken = medInfo.getMedicineMorning();
                //     if (medicineDetails.getMedicineMorning().get(j).getMedName().equals("Morning")){
                //   medicineTime =medicineDetails.getMedicineMorning().;
                String medicineType = medInfo.getMedicineMorning().get(j).getType();
                colorOfMedicine = medInfo.getMedicineMorning().get(j).getMedColor();
                ImageView image = setMedicineColor(medicineType, colorOfMedicine, frameLayout);
                if (medInfo.getMedicineMorning().get(j).getTakenorNot()) {
                    remove_img.setImageDrawable(getResources().getDrawable(R.drawable.ic_checked_1));
                }
                frameLayout.addView(image);
                frameLayout.addView(remove_img);
                // layout2.addView(imgLayout);
                morning.addView(frameLayout);
                final int finalJ = j;
                frameLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        for (int morningTaken = 0; morningTaken < medTaken.size(); morningTaken++) {
                            if (morningTaken == finalJ) {
                                if (medTaken.get(morningTaken).getTakenorNot()) {
                                    remove_img.setImageDrawable(getResources().getDrawable(R.drawable.ic_unchecked_1));
                                    remove_img.setTag(0);
                                    medInfo.getMedicineMorning().get(morningTaken).setTakenorNot(false);
                                } else {
                                    remove_img.setImageDrawable(getResources().getDrawable(R.drawable.ic_checked_1));
                                    remove_img.setTag(R.drawable.ic_checked_1);
                                    medInfo.getMedicineMorning().get(morningTaken).setTakenorNot(true);
                                }
                                break;
                            }
                        }
                    }
                });
            }
        }

        if (time.equals("Afternoon")) {
            for (int k = 0; k < medInfo.getMedicineAfternoon().size(); k++) {
                final FrameLayout frameLayout = new FrameLayout(getActivity());
                FrameLayout.LayoutParams params2 = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                params2.gravity = Gravity.CENTER_VERTICAL;
                frameLayout.setLayoutParams(params2);
                final ImageView remove_img;

                remove_img = new ImageView(getActivity());
                int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 15, getResources().getDisplayMetrics());
                int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 15, getResources().getDisplayMetrics());

                FrameLayout.LayoutParams params3 = new FrameLayout.LayoutParams(width, height);
                params3.gravity = Gravity.RIGHT;
                params3.setMargins(10, 15, 0, 0);
                remove_img.setId(R.id.checkedImg);
                remove_img.setLayoutParams(params3);

                int colorOfMedicine;
/*

            String s = medInfo.get(i);
            Gson gsonObj = new Gson();
            final MedicineDetails medicineDetails = gsonObj.fromJson(s, MedicineDetails.class);
            beforeAdd.add(medicineDetails);
            Log.e(TAG, "onCreateView: " + medicineDetails);
*/

                if (checkedImg) {
                    remove_img.setImageResource(R.drawable.ic_checked_1);
                    remove_img.setTag(R.drawable.ic_checked_1);
                } else {
                    remove_img.setImageResource(R.drawable.ic_unchecked_1);
                    remove_img.setTag(0);
                    //medicineDetails.setMoringChecked(false);
                    //medicineDetails.setAfternoonChecked(false);
                    //medicineDetails.setNightChecked(false);
                }

                // MedicineDetails meds = medInfo.get(i);
                final ArrayList<MedicinesTakenOrNot> medTaken = medInfo.getMedicineAfternoon();

//             if (medicineDetails.getMedicineAfternoon().get(k).getMedName().equals("Afternoon")) {
                //    medicineTime =medicineDetails.getMedicineAfternoon();
                String medicineType = medInfo.getMedicineAfternoon().get(k).getType();
                colorOfMedicine = medInfo.getMedicineAfternoon().get(k).getMedColor();
                ImageView image = setMedicineColor(medicineType, colorOfMedicine, frameLayout);
                if (medInfo.getMedicineAfternoon().get(k).getTakenorNot()) {
                    remove_img.setImageDrawable(getResources().getDrawable(R.drawable.ic_checked_1));
                }

                frameLayout.addView(image);
                frameLayout.addView(remove_img);
                // layout2.addView(imgLayout);
                afternoon.addView(frameLayout);
                //   onClickMedicine(frameLayout, remove_img, time, meds);
                final int finalK = k;
                frameLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        for (int morningTaken = 0; morningTaken < medTaken.size(); morningTaken++) {
                            if (morningTaken == finalK) {
                                if (medTaken.get(morningTaken).getTakenorNot()) {
                                    remove_img.setImageDrawable(getResources().getDrawable(R.drawable.ic_unchecked_1));
                                    remove_img.setTag(0);
                                    medInfo.getMedicineAfternoon().get(morningTaken).setTakenorNot(false);
                                } else {
                                    remove_img.setImageDrawable(getResources().getDrawable(R.drawable.ic_checked_1));
                                    remove_img.setTag(R.drawable.ic_checked_1);
                                    medInfo.getMedicineAfternoon().get(morningTaken).setTakenorNot(true);
                                }
                                break;
                            }
                        }
                    }
                });
                //  noonContainer.addView(child);
                // }
            }
        }
        if (time.equals("Night")) {
            for (int m = 0; m < medInfo.getMedicineNight().size(); m++) {
                final FrameLayout frameLayout = new FrameLayout(getActivity());
                FrameLayout.LayoutParams params2 = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                params2.gravity = Gravity.CENTER_VERTICAL;
                frameLayout.setLayoutParams(params2);
                final ImageView remove_img;

                remove_img = new ImageView(getActivity());
                int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 15, getResources().getDisplayMetrics());
                int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 15, getResources().getDisplayMetrics());

                FrameLayout.LayoutParams params3 = new FrameLayout.LayoutParams(width, height);
                params3.gravity = Gravity.RIGHT;
                params3.setMargins(10, 15, 0, 0);
                remove_img.setId(R.id.checkedImg);
                remove_img.setLayoutParams(params3);

                int colorOfMedicine;
/*

            String s = medInfo.get(i);
            Gson gsonObj = new Gson();
            final MedicineDetails medicineDetails = gsonObj.fromJson(s, MedicineDetails.class);
            beforeAdd.add(medicineDetails);
            Log.e(TAG, "onCreateView: " + medicineDetails);
*/

                if (checkedImg) {
                    remove_img.setImageResource(R.drawable.ic_checked_1);
                    remove_img.setTag(R.drawable.ic_checked_1);
                } else {
                    remove_img.setImageResource(R.drawable.ic_unchecked_1);
                    remove_img.setTag(0);
                    //medicineDetails.setMoringChecked(false);
                    //medicineDetails.setAfternoonChecked(false);
                    //medicineDetails.setNightChecked(false);
                }

                //  MedicineDetails meds = medInfo.get(i);
                final ArrayList<MedicinesTakenOrNot> medTaken = medInfo.getMedicineNight();

                //if (medicineDetails.getMedicineNight().equals("Night")) {
                // medicineTime =medicineDetails.getMedicineNight();
                String medicineType = medInfo.getMedicineNight().get(m).getType();
                colorOfMedicine = medInfo.getMedicineNight().get(m).getMedColor();
                ImageView image = setMedicineColor(medicineType, colorOfMedicine, frameLayout);
                if (medInfo.getMedicineNight().get(m).getTakenorNot()) {
                    remove_img.setImageDrawable(getResources().getDrawable(R.drawable.ic_checked_1));
                }
                frameLayout.addView(image);
                frameLayout.addView(remove_img);
                //   layout2.addView(imgLayout);
                night.addView(frameLayout);
                final int finalM = m;
                frameLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        for (int morningTaken = 0; morningTaken < medTaken.size(); morningTaken++) {
                            if (morningTaken == finalM) {
                                if (medTaken.get(morningTaken).getTakenorNot()) {
                                    remove_img.setImageDrawable(getResources().getDrawable(R.drawable.ic_unchecked_1));
                                    remove_img.setTag(0);
                                    medInfo.getMedicineNight().get(morningTaken).setTakenorNot(false);
                                } else {
                                    remove_img.setImageDrawable(getResources().getDrawable(R.drawable.ic_checked_1));
                                    remove_img.setTag(R.drawable.ic_checked_1);
                                    medInfo.getMedicineNight().get(morningTaken).setTakenorNot(true);
                                }
                                break;
                            }
                        }
                    }
                });

            }
        }

        medicineContains = medInfo;
        //   }
    }


  /*  public void onClickMedicine(FrameLayout frameLayout, final ImageView finalRemove_img, final String type, final MedicineDetails medicineDetails) {
        frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (finalRemove_img.getTag().equals(R.drawable.ic_checked_1)) {
                    finalRemove_img.setImageDrawable(getResources().getDrawable(R.drawable.ic_unchecked_1));
                    finalRemove_img.setTag(0);
                } else {
                    finalRemove_img.setImageDrawable(getResources().getDrawable(R.drawable.ic_checked_1));
                    finalRemove_img.setTag(R.drawable.ic_checked_1);
                }
*//*
                    if(type.equals("Morning")) {
                        medicineTable.updateMedicineData(medicineDetails,1,false);
                        //medicineDetails.setMoringChecked(false);
                      //  m1.add(medicineDetails);
                    } else if(type.equals("Afternoon")) {
                        medicineTable.updateMedicineData(medicineDetails,2,false);                       // m1.add(medicineDetails);
                    } else if(type.equals("Night")) {
                        medicineTable.updateMedicineData(medicineDetails,3,false);                      //  m1.add(medicineDetails);

                    }*//*

            }
        });*/

    public ImageView setMedicineColor(String medicineType, int colorOfMedicine, FrameLayout frameLayout) {
        ImageView image = new ImageView(getActivity());
        int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 25, getResources().getDisplayMetrics());
        int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 25, getResources().getDisplayMetrics());
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(width, height);
        params.setMargins(15, 0, 15, 0);
        params.gravity = Gravity.CENTER_VERTICAL | Gravity.LEFT;
        image.setLayoutParams(params);

        if (medicineType.equals("Tablet")) {
            image.setBackgroundResource(0);
            image.setBackgroundResource(R.drawable.ic_tablet_1);
            Drawable drawable = image.getBackground().mutate();
            drawable.clearColorFilter();
            drawable.setColorFilter(new PorterDuffColorFilter(colorOfMedicine, PorterDuff.Mode.SRC_IN));

//            layout2.addView(frameLayout);
        } else {
            Log.e(TAG, "onCreateView: " + "NO TABLET ");
        }

        if (medicineType.contains("Capsule")) {
            image.setBackgroundResource(0);
            image.setBackgroundResource(R.drawable.ic_capsule_1);
            Drawable drawable = image.getBackground().mutate();
            drawable.clearColorFilter();
            drawable.setColorFilter(new PorterDuffColorFilter(colorOfMedicine, PorterDuff.Mode.SRC_IN));

        }
        if (medicineType.contains("Syrup")) {
            image.setBackgroundResource(0);
            image.setBackgroundResource(R.drawable.ic_syrup_1);
            Drawable drawable = image.getBackground().mutate();
            drawable.clearColorFilter();
            drawable.setColorFilter(new PorterDuffColorFilter(colorOfMedicine, PorterDuff.Mode.SRC_IN));
        }
        return image;


    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.ivInfo:
                //dBforUserDetails.getMedicine("Morning");
                ArrayList<String> Mmedicine = medicineTable.getMedicine(GlobalClass.userID, "Morning", savedDate);
                ArrayList<MedicinesTakenOrNot> medicines = medicineContains.getMedicineMorning();
               /* for (int i = 0; i < medicines.size(); i++) {
                    MedicinesTakenOrNot medicineInfo = new MedicinesTakenOrNot();
*//*
                    Gson gsonObj = new Gson();
                    MedicineDetails medicineDetails = gsonObj.fromJson(s, MedicineDetails.class);
                    Log.e(TAG, "onCreateView: " + medicineDetails);*//*

                    medicineInfo = medicineContains.getMedicineMorning().get(i);
                    medicines.add(medicineInfo);
*/
                //     }
                infoDialogFragment = new MyDialogFragment(medicines, "MORNING MEDICINES", 1);
                infoDialogFragment.setCancelable(false);
                infoDialogFragment.show(getActivity().getFragmentManager(), "Medicine Info");
                break;

            case R.id.ivInfo1:
                //   ArrayList<String> Amedicine = medicineTable.getMedicine(GlobalClass.userID, "Afternoon", savedDate);
                ArrayList<MedicinesTakenOrNot> Amedicines = medicineContains.getMedicineAfternoon();

              /*  for (int i = 0; i < Amedicine.size(); i++) {
                    MedicineDetails AmedicineInfo = new MedicineDetails();

                    String s = Amedicine.get(i);
                    Gson gsonObj = new Gson();
                    MedicineDetails medicineDetails = gsonObj.fromJson(s, MedicineDetails.class);
                    Log.e(TAG, "onCreateView: " + medicineDetails);

                    AmedicineInfo = medicineDetails;
                    Amedicines.add(AmedicineInfo);*/
                // }
                infoDialogFragment = new MyDialogFragment(Amedicines, "AFTERNOON MEDICINES", 2);
                infoDialogFragment.setCancelable(false);
                infoDialogFragment.show(getActivity().getFragmentManager(), "Medicine Info");
                break;

            case R.id.ivInfo2:
                //  ArrayList<String> Nmedicine = medicineTable.getMedicine(GlobalClass.userID, "Night", savedDate);
                ArrayList<MedicinesTakenOrNot> Nmedicines = medicineContains.getMedicineNight();
              /*  for (int i = 0; i < Nmedicine.size(); i++) {
                    MedicineDetails NmedicineInfo = new MedicineDetails();

                    String s = Nmedicine.get(i);
                    Gson gsonObj = new Gson();
                    MedicineDetails medicineDetails = gsonObj.fromJson(s, MedicineDetails.class);
                    Log.e(TAG, "onCreateView: " + medicineDetails);

                    NmedicineInfo = medicineDetails;
                    Nmedicines.add(NmedicineInfo);
                }*/
                infoDialogFragment = new MyDialogFragment(Nmedicines, "NIGHT MEDICINES", 3);
                infoDialogFragment.setCancelable(false);
                infoDialogFragment.show(getActivity().getFragmentManager(), "Medicine Info");
                break;
        }
    }

    public void storeData() {
        MedicineDetailsTable medicineDetailsTable = new MedicineDetailsTable();

        if (tvMedicationToday.getText().equals("Today  ")) {
            Date dt = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");// set format for date
            String todaysDate = dateFormat.format(dt); // parse it like
            savedDate = todaysDate;
            medicineContains.setDate(savedDate);
        } else {
            savedDate = tvMedicationToday.getText().toString();
            medicineContains.setDate(savedDate);
        }
        boolean date = medicineTable.getDate(savedDate, GlobalClass.userID);
        if (!date) {
            medicineTable.addMedicineDetails(medicineContains, GlobalClass.userID);
            EventBus.getDefault().post(new MessageEvent("Hello!"));
            ((TrackActivity) getActivity()).setActionBarTitle("Track");
        } else {
            medicineTable.updateDataWithDate(GlobalClass.userID, savedDate, medicineContains);
            EventBus.getDefault().post(new MessageEvent("Hello!"));
            ((TrackActivity) getActivity()).setActionBarTitle("Track");
        }
    }

    @Override
    public void doBack() {
        if (morning != null || afternoon != null || night != null) {
            Boolean checkedOrNot = false;
            TrackMedication med = new TrackMedication();
            Log.e(TAG, "doBack: " + medicineContains);
            storeData();

        }

            /*for (int i = 0; i < m1.size(); i++) {
*//*
                String MedicineString = new Gson().toJson(beforeAdd.get(i));
                med.setMedicines(MedicineString);

                storeData(i);
*//*
                if (m1.get(i).getMoringChecked() || m1.get(i).getAfternoonChecked() || m1.get(i).getNightChecked()) {
                    checkedOrNot = true;
                } else {
                    checkedOrNot = false;
                }
            }
            if (checkedOrNot) {
                med.setHadAllMedicines(true);
            } else {
                med.setHadAllMedicines(false);
            }
            med.setUserId(appSharedPreference.getUserId());

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");// set format for date
            if (tvMedicationToday.getText().equals("Today  ")) {
                Date dt = new Date();
                String todaysDate = dateFormat.format(dt);
                Date date = null;// parse it like
                try {
                    date = dateFormat.parse(todaysDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                med.setDateTime(date);
            } else {
                Date date = null;
                try {
                    date = dateFormat.parse(tvMedicationToday.getText().toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                med.setDateTime(date);
            }

            trackMedicationDB.addEntry(med);
            EventBus.getDefault().post(new DataSavedEvent(""));
            Log.e(TAG, "doBack: " + checkedOrNot + "");
            // EventBus.getDefault().post(new MessageEvent("Hello!"));
        } else {
            EventBus.getDefault().post(new MessageEvent("Hello!"));
            ((TrackActivity) getActivity()).setActionBarTitle("Track");
        }*/
    }

    @Override
    public void onStart() {
        EventBus.getDefault().register(this);
        super.onStart();
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSetTimeEvent(SetTimeEvent event) {
        tvMedicationToday.setText(event.getDate());
        callSavedMethod();
    }

    public void saveAllData() {
        if(!alreadySaved) {
            Log.e("fragment", "MedicationFragment saveAllData()");
            alreadySaved = true;
        }
    }

    public static class DatePickerDialogClass extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datepickerdialog = new DatePickerDialog(getActivity(),
                    AlertDialog.THEME_DEVICE_DEFAULT_LIGHT, this, year, month, day);
            datepickerdialog.getDatePicker().setMaxDate(System.currentTimeMillis());
            return datepickerdialog;
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            Date parseDate = null;
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat month_date = new SimpleDateFormat("MMM");
            cal.set(Calendar.MONTH, (month));
            String month_name = month_date.format(cal.getTime());

            Log.e("", "" + month_name);

            String date1 = day + " " + month_name + " " + year;
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
            try {
                parseDate = dateFormat.parse(date1);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            String stDate = dateFormat.format(parseDate); //2016/11/16 12:08:43
            Log.e("Comparing Date :", " Your date is correct");
            EventBus.getDefault().post(new SetTimeEvent(0, stDate));
            // tvMedicationToday.setText(stDate);
        }
    }

    Button saveButton;

    @SuppressLint("ValidFragment")
    public class MyDialogFragment extends DialogFragment {
        private RecyclerView mRecyclerView;
        ArrayList<MedicinesTakenOrNot> info;
        String time;
        int id;

        //  private MedicineInfoRecycler adapter;
        // this method create view for your Dialog
        MyDialogFragment(ArrayList<MedicinesTakenOrNot> medicineInfo, String session, int id) {
            this.info = medicineInfo;
            this.time = session;
            this.id = id;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            //inflate layout with recycler view
            View v = inflater.inflate(R.layout.custom_dialog_fragment, container, false);
            saveButton = (Button) v.findViewById(R.id.saveButton);


/*            DisplayMetrics metrics = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) dialogLayout.getLayoutParams();
            params.width = (metrics.widthPixels)/2;
            params.height = (metrics.heightPixels)/2;
            dialogLayout.setLayoutParams(params);*/

            TextView heading = (TextView) v.findViewById(R.id.heading);
            TextView noItems = (TextView) v.findViewById(R.id.ifNoItems);

            Button closeButton = (Button) v.findViewById(R.id.closeButton);
            heading.setText(time);
            final MedicineInfoRecycler adapter = new MedicineInfoRecycler(getActivity(), info, id);

            saveButton.setEnabled(false);

            if (info.size() == 0) {
                noItems.setVisibility(View.VISIBLE);
            } else {
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
                    // ArrayList<MedicineDetails> medDetails = adapter.getList();

                    medicineTable.updateDataWithDate(GlobalClass.userID, savedDate, medicineContains);
                    //infoDialogFragment.setCancelable(false);
                    infoDialogFragment.dismiss();
                    ((TrackActivity) getActivity()).showFragment(new MedicationFragment());
                   /* MedicineDetails medDetailList = medicineContains;
                    if (id == 1) {
                        medicineTable.updateMedicine(medDetails., getActivity(), 1);
                        infoDialogFragment.dismiss();

                    } else if (id == 2) {
                        medicineTable.updateMedicine(medDetails, getActivity(), 2);
                        infoDialogFragment.dismiss();

                    } else if (id == 3) {
                        medicineTable.updateMedicine(medDetails, getActivity(), 3);
                        infoDialogFragment.dismiss();
                    }
                    infoDialogFragment.setCancelable(false);
                    ((TrackActivity) getActivity()).showFragment(new MedicationFragment());
                }*/
                }
            });
            //get your recycler view and populate it.
            return v;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
//        notifyAll();

    }

    public class MedicineInfoRecycler extends RecyclerView.Adapter<MedicineInfoRecycler.MyViewHolder> {
        Context mContext;
        ArrayList<MedicinesTakenOrNot> medicineInfo;
        int id;
        ArrayList<MedicinesTakenOrNot> deletedData = new ArrayList<>();
        ArrayList<MedicinesTakenOrNot> editData = new ArrayList<>();
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

        public MedicineInfoRecycler(Context context, ArrayList<MedicinesTakenOrNot> info, int id) {
            this.medicineInfo = info;
            this.id = id;
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


            ImageView imageView = setMedicineColor(medicineInfo.get(position).getType(), medicineInfo.get(position).getMedColor(), null);
            holder.img.addView(imageView);
            holder.name.setText(medicineInfo.get(position).getMedName());

            holder.editImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(id==1){
                        ArrayList<MedicinesTakenOrNot> medList = medicineContains.getMedicineMorning();

                        for(int i=0; i<= medList.size(); i++){
                            if(medList.get(position).getMedName()== medList.get(i).getMedName()){
                                mCallback.passData(medicineContains, medList.get(position).getMedName());
                            }
                        }
                    }

                    editData.add(medicineInfo.get(position));
                    //   mCallback.passData(editData);
                    infoDialogFragment.dismiss();
                    //((TrackActivity) getActivity()).showFragment(new AddMedicineFragment());

                }
            });
            holder.deleteImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MedicineDetails medDetailList = medicineContains;
                    ArrayList<MedicinesTakenOrNot> medicineLists = new ArrayList<>();
                    if (id == 1) {
                        medicineLists = medDetailList.getMedicineMorning();
                        if (position == medicineLists.size()) {
                            medicineLists.remove(position - 1);
                            medDetailList.setMedicineMorning(medicineLists);
                            medicineContains = medDetailList;
                            notifyItemRemoved(position - 1);
                            saveButton.setTextColor(getResources().getColor(R.color.appBackground));
                            saveButton.setEnabled(true);

                        } else {
                            medicineLists.remove(position);
                            medDetailList.setMedicineMorning(medicineLists);
                            medicineContains = medDetailList;
                            notifyItemRemoved(position);
                            saveButton.setTextColor(getResources().getColor(R.color.appBackground));
                            saveButton.setEnabled(true);

                        }
                    } else if (id == 2) {
                        medicineLists = medDetailList.getMedicineAfternoon();
                        if (position == medicineLists.size()) {
                            medicineLists.remove(position - 1);
                            medDetailList.setMedicineAfternoon(medicineLists);
                            medicineContains = medDetailList;
                            notifyItemRemoved(position - 1);
                            saveButton.setTextColor(getResources().getColor(R.color.appBackground));
                            saveButton.setEnabled(true);

                        } else {
                            medicineLists.remove(position);
                            medDetailList.setMedicineAfternoon(medicineLists);
                            medicineContains = medDetailList;
                            notifyItemRemoved(position);
                            saveButton.setTextColor(getResources().getColor(R.color.appBackground));
                            saveButton.setEnabled(true);
                        }
                    } else if (id == 3) {
                        medicineLists = medDetailList.getMedicineNight();
                        if (position == medicineLists.size()) {
                            medicineLists.remove(position - 1);
                            medDetailList.setMedicineNight(medicineLists);
                            medicineContains = medDetailList;
                            notifyItemRemoved(position - 1);
                            saveButton.setTextColor(getResources().getColor(R.color.appBackground));
                            saveButton.setEnabled(true);

                        } else {
                            medicineLists.remove(position);
                            medDetailList.setMedicineNight(medicineLists);
                            medicineContains = medDetailList;
                            notifyItemRemoved(position);
                            saveButton.setTextColor(getResources().getColor(R.color.appBackground));
                            saveButton.setEnabled(true);

                        }
                    }

/*                    if (position == medicineLists.size()) {
                        medicineLists.remove(position-1);
                        medDetailList.setMedicineMorning(medicineLists);
                        medicineContains = medDetailList;
                        notifyItemRemoved(position-1);
                        saveButton.setTextColor(getResources().getColor(R.color.appBackground));
                        saveButton.setEnabled(true);

                    } else {
                        deletedData.add(medicineInfo.get(position));
                        medicineInfo.remove(position);
                        notifyItemRemoved(position);
                        saveButton.setTextColor(getResources().getColor(R.color.appBackground));
                        saveButton.setEnabled(true);
                    }*/
                }
            });
        }

        public ArrayList<MedicinesTakenOrNot> getList() {
            return deletedData;
        }

        @Override
        public int getItemCount() {
            return medicineInfo.size();
        }
    }

}
