package com.stemi.stemiapp.fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract.Events;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.gson.Gson;
import com.stemi.stemiapp.R;
import com.stemi.stemiapp.activity.TrackActivity;
import com.stemi.stemiapp.databases.MedicineTable;
import com.stemi.stemiapp.model.MedicineDetails;
import com.stemi.stemiapp.model.MessageEvent;
import com.stemi.stemiapp.preference.AppSharedPreference;
import com.stemi.stemiapp.utils.AppConstants;
import com.stemi.stemiapp.utils.CommonUtils;
import com.stemi.stemiapp.utils.GlobalClass;

import org.greenrobot.eventbus.EventBus;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.ContentValues.TAG;

/**
 * Created by Pooja on 04-09-2017.
 */

public class AddMedicineFragment extends Fragment implements View.OnClickListener, AppConstants, TrackActivity.OnBackPressedListener {
    @BindView(R.id.ll_color)
    LinearLayout color;
    @BindView(R.id.ll_medicine)
    LinearLayout medicineType;

    @BindView(R.id.etMedicineName)
    EditText medicineNamee;

    @BindView(R.id.morningMedicine)
    TextView morningMedicine;
    @BindView(R.id.noonMedicine)
    TextView noonMedicine;
    @BindView(R.id.nightMedicine)
    TextView nightMedicine;

    @BindView(R.id.tvMorningTime)
    TextView morningTime;
    @BindView(R.id.tvNoonTime)
    TextView noonTime;
    @BindView(R.id.tvNightTime)
    TextView nightTime;

    @BindView(R.id.medicineDays)
    EditText medicineDays;
    @BindView(R.id.medicineRemainder)
    CheckBox medicineRemainder;

    public final static String DATA_RECEIVE = "dataRecieve";

    MedicineDetails medicineDetails;
    int setTime;
    int timeHour, timeMinute;
    long time;
    long endTime;
    String typeOfMedicine;
    int colorOfMedicine;
    String numberOfDays;
    TimePickerDialog timepickerdialog1;
    Gson gson;
    AppSharedPreference appSharedPreference;
    MedicineTable medicineTable;
    ArrayList<MedicineDetails> showReceivedData;
    int checkEditOrNot = 0;

    public AddMedicineFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("NewApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_medicine, container, false);
        ButterKnife.bind(this, view);
        formIsValid(color);
        formIsValid(medicineType);

        //Setting fonts
        CommonUtils.setRobotoRegularFonts(getActivity(),medicineNamee);
        ((TrackActivity) getActivity()).setOnBackPressedListener(this);

        appSharedPreference = new AppSharedPreference(getActivity());
        medicineDetails = new MedicineDetails();
        medicineTable = new MedicineTable();
        Bundle args = getArguments();
        if (args != null) {
            showReceivedData = args.getParcelableArrayList("RECIEVE DATA");
            Log.e(TAG, "onCreateView: " + showReceivedData.get(0));
            medicineNamee.setText(showReceivedData.get(0).getMedicineName());
            medicineDays.setText(showReceivedData.get(0).getMedicineDays());
            medicineRemainder.setChecked(showReceivedData.get(0).getMedicineRemainder());

            for (int i = 0; i < color.getChildCount(); i++) {
                View v = color.getChildAt(i);
                if (v instanceof FrameLayout) {
                    // TextView textView = null;
                    FrameLayout frameLayout = (FrameLayout) v;
                    View view1 = frameLayout.getChildAt(1);
                    View tvView = frameLayout.getChildAt(0);
                    ImageView iv = (ImageView) view1;
                    TextView textView = (TextView) tvView;
//                        colorModes(i, textView,iv);
                    // textView.setSelected(false);
                    int[] medicineColor = getResources().getIntArray(R.array.medicineColor);
                    if (medicineColor[i] == showReceivedData.get(0).getMedicineColor()) {
                        colorOfMedicine = medicineColor[i];
                        iv.setImageDrawable(getResources().getDrawable(R.drawable.ic_color_selected));
                    }
                }

            }
            for (int i = 0; i < medicineType.getChildCount(); i++) {
                View v = medicineType.getChildAt(i);
                if (v instanceof TextView) {
                    final TextView textView = (TextView) v;
                    if (textView.getText().equals(showReceivedData.get(0).getMedicineType())) {
                        typeOfMedicine = textView.getText().toString();
                        textView.setTextColor(getResources().getColor(R.color.appBackground));
                        for (Drawable drawable : textView.getCompoundDrawables()) {
                            if (drawable != null) {
                                drawable.setColorFilter(new PorterDuffColorFilter(getResources().getColor(R.color.appBackground), PorterDuff.Mode.SRC_IN));
                            }
                        }
                    }

                }

            }

            if (!showReceivedData.get(0).getMedicineMorning().equals("")) {
                morningMedicine.setBackgroundResource(R.drawable.oval_shape_textview);
                morningMedicine.setTextColor(getResources().getColor(R.color.white));
                morningTime.setVisibility(View.VISIBLE);
                morningMedicine.setSelected(false);
                morningTime.setText(showReceivedData.get(0).getMedicineMorningTime());
            }

            if (!showReceivedData.get(0).getMedicineAfternoon().equals("")) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    noonMedicine.setBackground(getResources().getDrawable(R.drawable.oval_shape_textview));
                } else {
                    noonMedicine.setBackgroundResource(R.drawable.oval_shape_textview);
                }
                noonMedicine.setTextColor(getResources().getColor(R.color.white));
                noonTime.setVisibility(View.VISIBLE);
                noonMedicine.setSelected(true);
                noonTime.setText(showReceivedData.get(0).getMedicineNoonTime());
            }
            if (!showReceivedData.get(0).getMedicineNight().equals("")) {
                nightMedicine.setBackgroundResource(R.drawable.oval_shape_textview);
                nightMedicine.setTextColor(getResources().getColor(R.color.white));
                nightTime.setVisibility(View.VISIBLE);
                nightMedicine.setSelected(true);
                nightTime.setText(showReceivedData.get(0).getMedicineNightTime());
            }

            checkEditOrNot = 1;
        }else {
            morningMedicine.setBackgroundResource(R.drawable.oval_shape_textview);
            morningMedicine.setTextColor(getResources().getColor(R.color.white));
            morningTime.setVisibility(View.VISIBLE);
        }


        morningMedicine.setOnClickListener(this);
        noonMedicine.setOnClickListener(this);
        nightMedicine.setOnClickListener(this);
        morningTime.setOnClickListener(this);
        noonTime.setOnClickListener(this);
        nightTime.setOnClickListener(this);
        medicineRemainder.setOnClickListener(this);

        medicineDays.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() != 0) {
                    numberOfDays = medicineDays.getText().toString();
                }
            }
        });
        ((TrackActivity) getActivity()).setActionBarTitle("Add Medicine");
        return view;
    }

    public static String getDate(Calendar cal) {
        return "" + (cal.get(Calendar.MONTH) + 1) + "/" +
                (cal.get(Calendar.DATE));
    }


    public boolean formIsValid(final LinearLayout layout) {
        if (layout == color) {
            for (int i = 0; i < layout.getChildCount(); i++) {
                View v = layout.getChildAt(i);
                if (v instanceof FrameLayout) {
                    // TextView textView = null;
                    FrameLayout frameLayout = (FrameLayout) v;
                    View view1 = frameLayout.getChildAt(1);
                    View tvView = frameLayout.getChildAt(0);
                    ImageView iv = (ImageView) view1;
                    TextView textView = (TextView) tvView;
                    colorModes(i, textView, iv);
                    textView.setSelected(false);
                    final int tempVar = i;
                    textView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            checkVar(layout, tempVar);
                        }
                    });
                }
            }
        } else {
            for (int i = 0; i < layout.getChildCount(); i++) {
                View v = layout.getChildAt(i);
                if (v instanceof TextView) {
                    final TextView textView = (TextView) v;
                    textView.setSelected(false);
                    typeModes(i, textView);

                    final int tempVar = i;

                    textView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            checkVar(layout, tempVar);
                        }
                    });
                }
            }
        }
        return true;
    }

    public void typeModes(int value, TextView textView) {
        //GradientDrawable gd = (GradientDrawable) textView.getBackground().getCurrent();
        switch (value) {
            case 0:
                for (Drawable drawable : textView.getCompoundDrawables()) {
                    if (drawable != null) {
                        drawable.clearColorFilter();
                        //    drawable.setColorFilter(new PorterDuffColorFilter(getResources().getColor(R.color.colorLightGrey), PorterDuff.Mode.SRC_IN));
                    }
                }
                textView.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                break;
            case 1:
                textView.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                for (Drawable drawable : textView.getCompoundDrawables()) {
                    if (drawable != null) {
                        drawable.clearColorFilter();
                        //  drawable.setColorFilter(new PorterDuffColorFilter(getResources().getColor(R.color.colorLightGrey), PorterDuff.Mode.SRC_IN));
                    }
                }
                break;
            case 2:
                textView.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                for (Drawable drawable : textView.getCompoundDrawables()) {
                    if (drawable != null) {
                        drawable.clearColorFilter();

                        // drawable.setColorFilter(new PorterDuffColorFilter(getResources().getColor(R.color.colorLightGrey), PorterDuff.Mode.SRC_IN));
                    }
                }
                break;

        }

    }

    public void colorModes(int i, TextView textView, ImageView iv) {
        textView.setBackgroundResource(R.drawable.textview_rounded_background);
        GradientDrawable gd = (GradientDrawable) textView.getBackground().getCurrent();
        switch (i) {
            case 0:
                gd.setColor(getResources().getColor(R.color.white));
                iv.setImageResource(0);
                //gd.setStroke(1,getResources().getColor(R.color.colorDarkGrey));
                break;
            case 1:
                gd.setColor(getResources().getColor(R.color.lightBlue));
                iv.setImageResource(0);
                break;
            case 2:
                gd.setColor(getResources().getColor(R.color.darkBlue));
                iv.setImageResource(0);
                break;
            case 3:
                gd.setColor(getResources().getColor(R.color.darkPurple));
                iv.setImageResource(0);
                break;
            case 4:
                gd.setColor(getResources().getColor(R.color.red));
                iv.setImageResource(0);
                break;
            case 5:
                gd.setColor(getResources().getColor(R.color.orange1));
                iv.setImageResource(0);
                break;
            case 6:
                gd.setColor(getResources().getColor(R.color.yellow));
                iv.setImageResource(0);
                break;
            case 7:
                gd.setColor(getResources().getColor(R.color.green));
                iv.setImageResource(0);
                break;
        }
    }


    @SuppressLint("NewApi")
    public boolean checkVar(LinearLayout layout, int tempVar) {
        boolean clicked = false;
        int[] medicineColor = getResources().getIntArray(R.array.medicineColor);

        for (int j = 0; j < layout.getChildCount(); j++) {
            if (layout == color) {
                View v1 = layout.getChildAt(j);
                if (v1 instanceof FrameLayout) {
                    FrameLayout frameLayout = (FrameLayout) v1;
                    View view1 = frameLayout.getChildAt(0);
                    TextView textView = (TextView) view1;
                    View ivView = frameLayout.getChildAt(1);
                    ImageView iv = (ImageView) ivView;

                    if (tempVar == j) {
                        if (textView.isSelected()) {
                            //tv.setSelected(false);
                        } else {
                            textView.setSelected(true);
                        }
                    } else {
                        textView.setSelected(false);
                    }

                    if (textView.isSelected()) {
                        colorOfMedicine = medicineColor[j];
                        iv.setImageDrawable(getResources().getDrawable(R.drawable.ic_color_selected));
                    } else {
                        colorModes(j, textView, iv);
                    }
                }
            } else {
                View v = layout.getChildAt(j);
                TextView tv = (TextView) v;
                if (tempVar == j) {
                    if (tv.isSelected()) {
                        //tv.setSelected(false);
                    } else {
                        tv.setSelected(true);
                    }
                } else {
                    tv.setSelected(false);
                }

                if (tv.isSelected()) {
/*                    if (layout == color) {
                        colorOfMedicine = medicineColor[j];
                        iv.setImageDrawable(getResources().getDrawable(R.drawable.ic_color_selected));
                        //  tv.setBackgroundResource(R.drawable.ic_color_selected);

                    } else {*/
                    typeOfMedicine = tv.getText().toString();

                    tv.setTextColor(getResources().getColor(R.color.appBackground));
                    for (Drawable drawable : tv.getCompoundDrawables()) {
                        if (drawable != null) {
                            drawable.setColorFilter(new PorterDuffColorFilter(getResources().getColor(R.color.appBackground), PorterDuff.Mode.SRC_IN));
                        }
                    }
                    //}
                } /*else if (layout == color) {
                    colorModes(j, tv);
                }*/ else {
                    typeModes(j, tv);
                }
                clicked = tv.isSelected();
            }
        }
        return clicked;
    }

    @Override
    public void setMenuVisibility(boolean menuVisible) {
        if (getActivity() != null) {
            if (menuVisible) {
                ((TrackActivity) getActivity()).setActionBarTitle("Hospital");

            }
        }
        super.setMenuVisibility(menuVisible);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onClick(View v) {
        Boolean clicked = true;
        switch (v.getId()) {
            case R.id.morningMedicine:
                if (!morningMedicine.isSelected()) {
                    morningMedicine.setBackground(getResources().getDrawable(R.drawable.button_transparent_oval_shape));
                    morningMedicine.setTextColor(getResources().getColor(R.color.colorPrimary));
                    morningTime.setVisibility(View.GONE);
                    morningMedicine.setSelected(true);
                } else {
                    morningMedicine.setBackground(getResources().getDrawable(R.drawable.oval_shape_textview));
                    morningMedicine.setTextColor(getResources().getColor(R.color.white));
                    morningTime.setVisibility(View.VISIBLE);
                    morningMedicine.setSelected(false);
                }
                break;
            case R.id.noonMedicine:
                if (!noonMedicine.isSelected()) {
                    noonMedicine.setBackground(getResources().getDrawable(R.drawable.oval_shape_textview));
                    noonMedicine.setTextColor(getResources().getColor(R.color.white));
                    noonTime.setVisibility(View.VISIBLE);
                    noonMedicine.setSelected(true);
                } else {
                    noonMedicine.setBackground(getResources().getDrawable(R.drawable.button_transparent_oval_shape));
                    noonMedicine.setTextColor(getResources().getColor(R.color.colorPrimary));
                    noonTime.setVisibility(View.GONE);
                    noonMedicine.setSelected(false);
                }
                break;
            case R.id.nightMedicine:
                if (!nightMedicine.isSelected()) {
                    nightMedicine.setBackground(getResources().getDrawable(R.drawable.oval_shape_textview));
                    nightMedicine.setTextColor(getResources().getColor(R.color.white));
                    nightTime.setVisibility(View.VISIBLE);
                    nightMedicine.setSelected(true);
                } else {
                    nightMedicine.setBackground(getResources().getDrawable(R.drawable.button_transparent_oval_shape));
                    nightMedicine.setTextColor(getResources().getColor(R.color.colorPrimary));
                    nightTime.setVisibility(View.GONE);
                    nightMedicine.setSelected(false);
                }
                break;
            case R.id.tvMorningTime:
                if (morningTime.getVisibility() == View.VISIBLE) {
                    DialogFragment dialogfragment = new TimePickerTheme3class();
                    dialogfragment.show(getActivity().getFragmentManager(), "Time Picker with Theme 3");
                    setTime = 0;

                }
                break;
            case R.id.tvNoonTime:
                if (noonTime.getVisibility() == View.VISIBLE) {
                    DialogFragment dialogfragment = new TimePickerTheme3class();
                    dialogfragment.show(getActivity().getFragmentManager(), "Time Picker with Theme 3");
                    setTime = 1;
                }

                break;
            case R.id.tvNightTime:
                if (nightTime.getVisibility() == View.VISIBLE) {
                    setTime = 2;
                    DialogFragment dialogfragment = new TimePickerTheme3class();
                    dialogfragment.show(getActivity().getFragmentManager(), "Time Picker with Theme 3");

                }
                break;
            case R.id.medicineRemainder:
                if (!medicineRemainder.isChecked()) {
                    Toast.makeText(getActivity(), "Please Check here", Toast.LENGTH_SHORT).show();
                    medicineRemainder.setChecked(false);
                } else {
                    medicineRemainder.setChecked(true);
                }
        }
    }

    private long addEvent(String title, String description, long startTime) {

        //to get number of days
        Calendar cal = Calendar.getInstance();
        cal.setTimeZone(TimeZone.getTimeZone("GMT"));
        //adding days into Date in Java
        cal.add(Calendar.DATE, Integer.parseInt(medicineDays.getText().toString()));
        String s1 = getDate(cal) + " " + morningTime.getText().toString();
        endTime = CommonUtils.parseToMilliseconds(s1);
        Log.e(TAG, "onCreateView: date after 3 days : " + getDate(cal) + " " + endTime);

        final SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy h:mm a");
        cal.setTimeInMillis(endTime);
        Log.e(TAG, "syncWithCalender: emdmilli to date Duration " + sdf.format(cal.getTime()));

        //add to calander
        ContentResolver cr = getActivity().getContentResolver();
        Uri eventUriString = Uri.parse("content://com.android.calendar/events");

        ContentValues eventValues = new ContentValues();

        eventValues.put(Events.CALENDAR_ID, 1); // id, We need to choose from our mobile for primary its 1
        eventValues.put(Events.TITLE, title);
        eventValues.put(Events.DESCRIPTION, description);
        if (startTime < System.currentTimeMillis()) {
            startTime += (24 * 60 * 60 * 1000);
            eventValues.put(Events.DTSTART, startTime);
        } else {
            eventValues.put(Events.DTSTART, startTime);
        }
        eventValues.put(Events.DTEND, startTime + (60 * 60 * 1000));
        //eventValues.put("allDay",1);
        //  eventValues.put(Events.RRULE,"FREQ=DAILY;COUNT=5");
        eventValues.put(Events.EVENT_TIMEZONE, TimeZone.getDefault().toString());
        //   eventValues.put("eventStatus", 1); // This information is sufficient for most entries tentative (0), confirmed (1) or canceled (2):
        eventValues.put(Events.HAS_ALARM, 1); // 0 for false, 1 for true

        Uri eventUri = cr.insert(eventUriString, eventValues);
        long eventID = Long.parseLong(eventUri.getLastPathSegment());

//System.out.println("event id is::"+eventID);
        return eventID;


    }

    @Override
    public void doBack() {
        if (validatefields()) {
            if (medicineRemainder.isChecked()) {
                syncWithCalender();
                medicineDetails.setMedicineRemainder(true);
            } else {
                medicineDetails.setMedicineRemainder(false);
            }
            storeMedicalDetails();
            EventBus.getDefault().post(new MessageEvent("Hello!"));
            //  ((TrackActivity) getActivity()).showFragment(new MedicationFragment());
            //Toast.makeText(getActivity(), "You pressed Back", Toast.LENGTH_SHORT).show();
        } else {

        }
    }

    public class TimePickerTheme3class extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            timepickerdialog1 = new TimePickerDialog(getActivity(),
                    AlertDialog.THEME_DEVICE_DEFAULT_LIGHT, this, timeHour, timeMinute, false);

            return timepickerdialog1;
        }

        @Override
        public void onResume() {
            super.onResume();

            if (getView() == null) {
                return;
            }
            getView().setFocusableInTouchMode(true);
            getView().requestFocus();
            getView().setOnKeyListener(new View.OnKeyListener() {

                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {

                    if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                        Toast.makeText(getActivity(), "You pressed Back Button", Toast.LENGTH_SHORT).show();
                        return true;
                    }
                    return false;
                }
            });
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            String time = hourOfDay + ":" + minute;
            try {
                final SimpleDateFormat sdf = new SimpleDateFormat("H:mm");
                final Date dateObj = sdf.parse(time);
                String msgDate = (String) android.text.format.DateFormat.format("h:mm a", dateObj);
                Log.e("TIME IN 12hr Format : ", " " + msgDate);
                if (setTime == 0) {
                    morningTime.setText(msgDate);
                } else if (setTime == 1) {
                    noonTime.setText(msgDate);
                } else if (setTime == 2) {
                    nightTime.setText(msgDate);
                }

            } catch (final ParseException e) {
                e.printStackTrace();
            }
        }

    }

    public void storeMedicalDetails() {
        medicineDetails.setDate("");
        medicineDetails.setPersonName(GlobalClass.userID);
        medicineDetails.setMedicineName(medicineNamee.getText().toString());
        medicineDetails.setMedicineDays(medicineDays.getText().toString());
        medicineDetails.setMedicineColor(colorOfMedicine);
        medicineDetails.setMedicineType(typeOfMedicine);

        if (morningTime.getVisibility() == View.VISIBLE) {
            medicineDetails.setMedicineMorning(morningMedicine.getText().toString());
            medicineDetails.setMedicineMorningTime(morningTime.getText().toString());
        } else {
            medicineDetails.setMedicineMorning("");
            medicineDetails.setMedicineMorningTime("");
        }
        if (noonTime.getVisibility() == View.VISIBLE) {
            medicineDetails.setMedicineAfternoon(noonMedicine.getText().toString());
            medicineDetails.setMedicineNoonTime(noonTime.getText().toString());
        } else {
            medicineDetails.setMedicineAfternoon("");
            medicineDetails.setMedicineNoonTime("");
        }
        if (nightTime.getVisibility() == View.VISIBLE) {
            medicineDetails.setMedicineNight(nightMedicine.getText().toString());
            medicineDetails.setMedicineNightTime(nightTime.getText().toString());
        } else {
            medicineDetails.setMedicineNight("");
            medicineDetails.setMedicineNightTime("");
        }



        if (checkEditOrNot == 1) {
            medicineTable.getMedicineToEdit(showReceivedData, medicineDetails);
        } else {
            medicineTable.addMedicineDetails(medicineDetails, medicineDetails.getPersonName());
            long getCount = medicineTable.getMedicineDetailsCount();

            Log.e(TAG, "onCreateView: Get Medicine Count " + getCount);
        }
    }

    public void syncWithCalender() {
        if (morningTime.getVisibility() == View.VISIBLE) {
            String date3 = new SimpleDateFormat("dd-MMM-yyyy").format(new Date());
            String s2 = date3 + " " + morningTime.getText().toString();
            long startTime = CommonUtils.parseToMilliseconds(s2);
            addEvent(medicineNamee.getText().toString(), "Take Capsule", startTime);
        }
        if (noonTime.getVisibility() == View.VISIBLE) {
            String date3 = new SimpleDateFormat("dd-MMM-yyyy").format(new Date());
            String s2 = date3 + " " + noonTime.getText().toString();
            long startTime = CommonUtils.parseToMilliseconds(s2);
            addEvent(medicineNamee.getText().toString(), "Take Capsule", startTime);
        }
        if (nightTime.getVisibility() == View.VISIBLE) {
            String date3 = new SimpleDateFormat("dd-MMM-yyyy").format(new Date());
            String s2 = date3 + " " + nightTime.getText().toString();
            long startTime = CommonUtils.parseToMilliseconds(s2);
            addEvent(medicineNamee.getText().toString(), "Take Capsule", startTime);
        }
    }
  /*  @Override
    //Pressed return button - returns to the results menu
    public void onResume() {
        super.onResume();
        medicineNamee.setFocusableInTouchMode(true);
        medicineDays.setFocusableInTouchMode(true);
        medicineNamee.requestFocus();
        medicineDays.requestFocus();
        medicineNamee.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    if (validatefields()) {
                        if (medicineRemainder.isChecked()) {
                            syncWithCalender();
                            medicineDetails.setMedicineRemainder(true);
                        } else {
                            medicineDetails.setMedicineRemainder(false);
                        }
                        storeMedicalDetails();
                        ((TrackActivity) getActivity()).showFragment(new MedicationFragment());
                        Toast.makeText(getActivity(), "You pressed Back", Toast.LENGTH_SHORT).show();
                        return true;
                    }
                }
                return false;
            }
        });
        medicineDays.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    if (validatefields()) {
                        if (medicineRemainder.isChecked()) {
                            syncWithCalender();
                            medicineDetails.setMedicineRemainder(true);
                        } else {
                            medicineDetails.setMedicineRemainder(false);
                        }
                        storeMedicalDetails();
                        ((TrackActivity) getActivity()).showFragment(new MedicationFragment());
                        Toast.makeText(getActivity(), "You pressed Back", Toast.LENGTH_SHORT).show();
                        return true;
                    }else {

                    }
                }
                return false;
            }
        });
    }*/

    public boolean validatefields() {
        boolean valid = true;

        String medicineName = medicineNamee.getText().toString();
        if (TextUtils.isEmpty(medicineName)) {
            medicineNamee.setError("Required");
            valid = false;
        } else {
            medicineNamee.setError(null);
        }

        String medicineType = typeOfMedicine;
        if (TextUtils.isEmpty(medicineType)) {
            Toast.makeText(getActivity(), "Select medicine type", Toast.LENGTH_SHORT).show();
            valid = false;
        }

      /*  String Mmedicine = morningMedicine.getText().toString();
        if (TextUtils.isEmpty(medicineName)) {
            morningMedicine.setError("Required");
            valid = false;
        }else {
            medicineNamee.setError(null);
        }*/

        int medicineColor = colorOfMedicine;
        if (medicineColor == 0) {
            Toast.makeText(getActivity(), "Select medicine Color", Toast.LENGTH_SHORT).show();
            valid = false;
        }

      /* // String medicineColor = med.getText().toString();
        if (TextUtils.isEmpty(mobileNumber)) {
            etPhone.setError("Required");
            valid = false;
        } else if (mobileNumber.length() < 10 || mobileNumber.length() > 10) {
            etPhone.setError("Enter Valid Mobile Number");
            valid = false;
        } else {
            etPhone.setError(null);
        }*/

        String days = medicineDays.getText().toString();
        if (TextUtils.isEmpty(days)) {
            medicineDays.setError("Required");
            valid = false;
        } else {
            medicineDays.setError(null);
        }
        return valid;
    }
}





/*

    //add 1hr to current time
    final SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy h:mm a");
    final String currentDateandTime = s2;
    Date date = null;
                try {
                        date = sdf.parse(currentDateandTime);
                        } catch (ParseException e) {
                        e.printStackTrace();
                        }
final Calendar calendar = Calendar.getInstance();

        calendar.setTime(date);
        calendar.add(Calendar.HOUR, 1);
        Log.e(TAG, "addEvent: ADD 1 hr to Current time " +  calendar.getTime());
        String format = sdf.format(calendar.getTime());
        long endAlarm = CommonUtils.parseToMilliseconds(format);
        Log.e(TAG, "syncWithCalender: Current End time : " + endAlarm );

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        calendar.setTimeInMillis(endAlarm);
        Log.e(TAG, "syncWithCalender: emdmilli to date "+sdf.format(calendar.getTime()));

*/


// to set a alaram
               /* AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, 14);
                calendar.set(Calendar.MINUTE, 30);
                int weekInMillis = 24 * 60 * 60 * 1000;

                alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                        weekInMillis, PendingIntent.getBroadcast(getActivity(), 0, null, PendingIntent.FLAG_UPDATE_CURRENT));
*/