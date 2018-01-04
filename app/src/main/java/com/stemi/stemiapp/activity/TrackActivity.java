package com.stemi.stemiapp.activity;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.stemi.stemiapp.R;
import com.stemi.stemiapp.activity.LoginView.MainActivity;
import com.stemi.stemiapp.customviews.CircleImageView;
import com.stemi.stemiapp.customviews.CustomViewPager;
import com.stemi.stemiapp.databases.UserDetailsTable;
import com.stemi.stemiapp.fragments.AddMedicineFragment;
import com.stemi.stemiapp.fragments.BloodTestFragment;
import com.stemi.stemiapp.fragments.ExerciseFragment;
import com.stemi.stemiapp.fragments.HospitalFragment;
import com.stemi.stemiapp.fragments.LearnFragment;
import com.stemi.stemiapp.fragments.MedicationFragment;
import com.stemi.stemiapp.fragments.SOSFragment;
import com.stemi.stemiapp.fragments.SmokingFragment;
import com.stemi.stemiapp.fragments.StatusFragment;
import com.stemi.stemiapp.fragments.StressFragment;
import com.stemi.stemiapp.fragments.TrackFragment;
import com.stemi.stemiapp.fragments.WeightFragment;
import com.stemi.stemiapp.model.DataPassListener;
import com.stemi.stemiapp.model.MedicineDetails;
import com.stemi.stemiapp.model.MessageEvent;
import com.stemi.stemiapp.model.RegisteredUserDetails;
import com.stemi.stemiapp.model.SaveCurrentDataEvent;
import com.stemi.stemiapp.model.UserAcceptedEvent;
import com.stemi.stemiapp.model.UserEventDetails;
import com.stemi.stemiapp.preference.AppSharedPreference;
import com.stemi.stemiapp.utils.CommonUtils;
import com.stemi.stemiapp.utils.CompressImageUtil;
import com.stemi.stemiapp.utils.GlobalClass;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class TrackActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, DataPassListener {

    private static final String TAG = "TrackActivity";
    public static TabLayout tabLayout;
    Toolbar toolbar;
    CustomViewPager viewPager;
    private DrawerLayout drawer;
    RelativeLayout mainContainer;
    AppSharedPreference appSharedPreferences;
    private CircleImageView profileImage;
    public static UserEventDetails userEventDetails;
    public static TextView tabOne, tabTwo, tabThree, tabFour, tabFive;
    public static TabLayout.Tab tab;
    TextView toolbarTitle;

    RegisteredUserDetails registeredUserDetails;
    CircleImageView nav_profile_pic;
    TextView nav_header_userName, nav_header_email;
    private Fragment hospitalFragment;
    private OnScanCompletionListener onScanCompletedListener;
    private String currentFragmentName;
    private Fragment currentFragment;
    private ViewPagerAdapter adapter;
    private boolean updateEvent;

    String toolBarText;
    @Override
    protected void onResume() {
        super.onResume();
        getSupportFragmentManager().addOnBackStackChangedListener(getListener());
        UserDetailsTable dBforUserDetails = new UserDetailsTable(this);
        registeredUserDetails = dBforUserDetails.getUserDetails(GlobalClass.userID);
        Log.e(TAG, "GlobalClass.userID = " + GlobalClass.userID);

        if (registeredUserDetails != null && registeredUserDetails.getImgUrl() != null) {
            Bitmap bitmap = new CompressImageUtil().compressImage(this, registeredUserDetails.getImgUrl());
            profileImage.setImageBitmap(bitmap);
            nav_profile_pic.setImageBitmap(bitmap);
        } else {
            profileImage.setImageResource(R.drawable.ic_user);
            nav_profile_pic.setImageResource(R.drawable.ic_user);
        }

        if (registeredUserDetails != null) {
            nav_header_userName.setText(registeredUserDetails.getName());
            nav_header_email.setText(registeredUserDetails.getEmail());
        }

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(TrackActivity.this, ProfileActivity.class);
                startActivity(i);
            }
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation_layout);

        toolbar = (Toolbar) findViewById(R.id.track_toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorPrimary));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        userEventDetails = new UserEventDetails();
        appSharedPreferences = new AppSharedPreference(this);
        viewPager = (CustomViewPager) findViewById(R.id.viewpager);
        viewPager.setPagingEnabled(false);
        mainContainer = (RelativeLayout) findViewById(R.id.mainContainer);
        profileImage = (CircleImageView) findViewById(R.id.profile_image);

        toolbarTitle = (TextView) findViewById(R.id.toolbar_title);
        hospitalFragment = new HospitalFragment();

        if (viewPager.getVisibility() == View.VISIBLE) {
            mainContainer.setVisibility(View.GONE);
            setupViewPager(viewPager);
        } else {
            viewPager.setVisibility(View.VISIBLE);
            setupViewPager(viewPager);
        }


        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);


        LayoutInflater inflater = getLayoutInflater();
        View headerView = inflater.inflate(R.layout.nav_header_main, null, false);
        navigationView.addHeaderView(headerView);

        nav_profile_pic = (CircleImageView) headerView.findViewById(R.id.iv_nav_profile_pic);
        nav_header_email = (TextView) headerView.findViewById(R.id.tv_email);
        nav_header_userName = (TextView) headerView.findViewById(R.id.tv_profile_user_name);

        assert navigationView != null;
        navigationView.setNavigationItemSelectedListener(this);

     /*   if (registeredUserDetails.getImgUrl() != null) {
            nav_header_userName.setText(registeredUserDetails.getName());
            nav_header_email.setText(registeredUserDetails.getEmail());
            Bitmap bitmap = new CompressImageUtil().compressImage(this,registeredUserDetails.getImgUrl());
            nav_profile_pic.setImageBitmap(bitmap);
        }else {
            nav_profile_pic.setImageResource(R.drawable.ic_user);
        }
*/
        tabLayout = (TabLayout) findViewById(R.id.tab);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();
        tabLayout.setTabTextColors(R.color.colorDarkGrey, R.color.appBackground);
        tabLayout.setOnTabSelectedListener(new TabListener());


    }


    protected OnBackPressedListener onBackPressedListener;

    public interface OnBackPressedListener {
        void doBack();
    }

    public void setOnBackPressedListener(OnBackPressedListener onBackPressedListener) {
        this.onBackPressedListener = onBackPressedListener;
    }

    public void setActionBarTitle(String title) {
        toolbarTitle.setText(title);
    }

    public interface OnScanCompletionListener {
        void scanCompleted(String contents);
    }

    public void setOnScanCompletedListener(OnScanCompletionListener onScanCompletionListener) {
        this.onScanCompletedListener = onScanCompletionListener;
    }

    private void clearBackStack(){
        FragmentManager fm = getSupportFragmentManager();
        for(int i = 0; i < fm.getBackStackEntryCount(); ++i) {
            fm.popBackStack();
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUserAccepetedEvent(UserAcceptedEvent event) {
        String value = event.getMessage();

        if (value.equalsIgnoreCase("1")) {
            ((ExerciseFragment) currentFragment).saveUserData();
        } else if (value.equalsIgnoreCase("2")) {
            ((StressFragment) currentFragment).saveUserData();
        } else if (value.equalsIgnoreCase("3")) {
            ((SmokingFragment) currentFragment).saveUserData();
        } else if (value.equalsIgnoreCase("4")) {
            ((WeightFragment) currentFragment).saveUserData();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSaveDataEvent(SaveCurrentDataEvent event) {
        Log.e("fragment", "event = " + event.message);


        if (currentFragmentName.equalsIgnoreCase("MedicationFragment")) {
            ((MedicationFragment) currentFragment).saveAllData();
          //  clearBackStack();
        } else if (currentFragmentName.equalsIgnoreCase("ExerciseFragment")) {
            ((ExerciseFragment) currentFragment).saveAllData();
          //  clearBackStack();
        } else if (currentFragmentName.equalsIgnoreCase("StressFragment")) {
            ((StressFragment) currentFragment).saveAllData();
            //clearBackStack();
        } else if (currentFragmentName.equalsIgnoreCase("SmokingFragment")) {
            ((SmokingFragment) currentFragment).saveAllData();
        //    clearBackStack();
        } else if (currentFragmentName.equalsIgnoreCase("WeightFragment")) {
            ((WeightFragment) currentFragment).saveAllData();
           //clearBackStack();
        } else if(currentFragmentName.equalsIgnoreCase("BloodTestFragment")){
            //ignore
            ((BloodTestFragment) currentFragment).saveAllData();
        } else if(currentFragmentName.equalsIgnoreCase("AddMedicineFragment")) {
            //ignore
            ((AddMedicineFragment) currentFragment).saveAllData();
        } else {

            }
    }

    public void showFragment(Fragment fragment) {

        String TAG = fragment.getClass().getSimpleName();
        currentFragmentName = TAG;
        currentFragment = fragment;
        viewPager.setVisibility(View.GONE);
        mainContainer.setVisibility(View.VISIBLE);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        fragmentTransaction.replace(R.id.mainContainer, fragment, TAG);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commitAllowingStateLoss();
    }

    String[] title = {
            "Learn",
            "Track",
            "SOS",
            "Hospital",
            "Statistics"
    };

    public void ubTateTab(int tabCount, Context context) {
        tab = tabLayout.getTabAt(tabCount);
        tab.select();

        if (tab.isSelected()) {
            View v = tabLayout.getTabAt(tabCount).getCustomView();
            if (v instanceof TextView) {
                TextView textView = (TextView) v;
                textView.setTextColor(context.getResources().getColor(R.color.appBackground));
                for (Drawable drawable : textView.getCompoundDrawables()) {
                    if (drawable != null) {
                        drawable.setColorFilter(new PorterDuffColorFilter(context.getResources().getColor(R.color.appBackground), PorterDuff.Mode.SRC_IN));
                    }
                }
            }
        }
    }
    Fragment customFragment;

    public class TabListener implements TabLayout.OnTabSelectedListener {

        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            int position = tab.getPosition();
            if (viewPager.getVisibility() == View.GONE) {
                viewPager.setVisibility(View.VISIBLE);
                mainContainer.setVisibility(View.GONE);
            }
        //  currentFragment = (Fragment) getSupportFragmentManager().getFragments().get(position);
           // Log.e(TAG, "onTabSelected: " + customFragment);
            switch (position) {
                case 0:
                    toolBarText = "Learn";
                    toolbarTitle.setText("Learn");
                  //  customFragment = (Fragment) getSupportFragmentManager().getFragments().get(position);
                    break;
                case 1:
                    toolBarText = "Track";
                    toolbarTitle.setText("Track");
                    //customFragment = (Fragment) getSupportFragmentManager().getFragments().get(position);
                    break;
                case 2:
                    toolBarText = "SOS";
                    toolbarTitle.setText("SOS");
                 //   customFragment = (Fragment) getSupportFragmentManager().getFragments().get(position);
                    break;
                case 3:
                    toolBarText = "Hospital";
                    toolbarTitle.setText("Hospital");
                   // customFragment = (Fragment) getSupportFragmentManager().getFragments().get(position);
                    break;
                case 4:
                    toolBarText = "Statistics";
                    toolbarTitle.setText("Statistics");
                  //  customFragment = (Fragment) getSupportFragmentManager().getFragments().get(position);
                    break;
            }
            //clearBackStack();
            if (tab.isSelected()) {
                for (int i = 0; i < tabLayout.getTabCount(); i++) {
                    if (i == position) {
                        setActionBarTitle(title[i]);
                    }
                }

                View v = tabLayout.getTabAt(position).getCustomView();
                if (v instanceof TextView) {
                    TextView textView = (TextView) v;
                    textView.setTextColor(getResources().getColor(R.color.appBackground));
                    for (Drawable drawable : textView.getCompoundDrawables()) {
                        if (drawable != null) {
                            drawable.setColorFilter(new PorterDuffColorFilter(getResources().getColor(R.color.appBackground), PorterDuff.Mode.SRC_IN));
                        }
                    }
                }
            }
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {
            for (int i = 0; i < tabLayout.getTabCount(); i++) {
                View v = tabLayout.getTabAt(i).getCustomView();
                if (v instanceof TextView) {
                    TextView textView = (TextView) v;
                    textView.setTextColor(getResources().getColor(R.color.colorLightGrey));
                    for (Drawable drawable : textView.getCompoundDrawables()) {
                        if (drawable != null) {
                            drawable.clearColorFilter();
                        }
                        //drawable.setColorFilter(new PorterDuffColorFilter(getResources().getColor(R.color.appBackground), PorterDuff.Mode.SRC_IN));
                    }
                }
            }
        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {
            int position = tab.getPosition();
            if (viewPager.getVisibility() == View.GONE) {
                viewPager.setVisibility(View.VISIBLE);
                mainContainer.setVisibility(View.GONE);
            }

            switch (position) {
                case 0:
                    toolBarText = "Learn";
                    toolbarTitle.setText("Learn");
                    clearBackStack();
                    //  customFragment = (Fragment) getSupportFragmentManager().getFragments().get(position);
                    break;
                case 1:
                    toolBarText = "Track";
                    toolbarTitle.setText("Track");
                    clearBackStack();
                    //customFragment = (Fragment) getSupportFragmentManager().getFragments().get(position);
                    break;
                case 2:
                    toolBarText = "SOS";
                    toolbarTitle.setText("SOS");
                    clearBackStack();

                    //   customFragment = (Fragment) getSupportFragmentManager().getFragments().get(position);
                    break;
                case 3:
                    toolBarText = "Hospital";
                    toolbarTitle.setText("Hospital");
                    clearBackStack();

                    // customFragment = (Fragment) getSupportFragmentManager().getFragments().get(position);
                    break;
                case 4:
                    toolBarText = "Statistics";
                    toolbarTitle.setText("Statistics");
                    //  customFragment = (Fragment) getSupportFragmentManager().getFragments().get(position);
                    break;
            }
            //
            if (tab.isSelected()) {
                for (int i = 0; i < tabLayout.getTabCount(); i++) {
                    if (i == position) {
                        setActionBarTitle(title[i]);
                    }
                }

               /* View v = tabLayout.getTabAt(position).getCustomView();
                if (v instanceof TextView) {
                    TextView textView = (TextView) v;
                    textView.setTextColor(getResources().getColor(R.color.appBackground));
                    for (Drawable drawable : textView.getCompoundDrawables()) {
                        if (drawable != null) {
                            drawable.setColorFilter(new PorterDuffColorFilter(getResources().getColor(R.color.appBackground), PorterDuff.Mode.SRC_IN));
                        }
                    }
                }
            }
        }*/
            }
        }
    }

    public void clearTabColor(TextView textView) {
        textView.setTextColor(getResources().getColor(R.color.colorPrimary));
        for (Drawable drawable : textView.getCompoundDrawables()) {
            if (drawable != null) {
                drawable.clearColorFilter();
//                drawable.setColorFilter(new PorterDuffColorFilter(getResources().getColor(R.color.appBackground), PorterDuff.Mode.SRC_IN));
            }
        }
    }

    @Override
    public void passData(ArrayList<MedicineDetails> data, String name) {
        AddMedicineFragment addMedicineFragment = new AddMedicineFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList("RECIEVE DATA", data);
        args.putString("NAME", name);
        addMedicineFragment.setArguments(args);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.mainContainer, addMedicineFragment).commit();
    }


/*    @Override
    public void passData(MedicineDetails data, String name) {
        AddMedicineFragment addMedicineFragment = new AddMedicineFragment();
        Bundle args = new Bundle();
        args.putParcelable("RECIEVE DATA", data);
    //    args.putString("NAME", name);
        // args.putString(AddMedicineFragment.DATA_RECEIVE, data);
        addMedicineFragment.setArguments(args);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.mainContainer, addMedicineFragment).commit();

    }*/

    @Override
    public void goBack() {

    }

    public CustomViewPager getViewPager() {
        return (CustomViewPager) this.viewPager;
    }

           /* @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
*/

    /**
     * Adding custom view to tab
     */
    private void setupTabIcons() {

        tabOne = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        CommonUtils.setRobotoLightFonts(this, tabOne);
        tabOne.setText("Learn");
        tabOne.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_learn, 0, 0);
        tabLayout.getTabAt(0).setCustomView(tabOne);
        tab = tabLayout.getTabAt(0);
        tab.select();
        toolBarText = "Learn";
//        toolbarTitle.setText("Learn");
        ubTateTab(0, TrackActivity.this);

        tabTwo = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        CommonUtils.setRobotoLightFonts(this, tabTwo);
        tabTwo.setText("Track");
        tabTwo.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_track, 0, 0);
        tabLayout.getTabAt(1).setCustomView(tabTwo);
        clearTabColor(tabTwo);

        tabThree = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabThree.setText("SOS");
        tabThree.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_sos, 0, 0);
        tabLayout.getTabAt(2).setCustomView(tabThree);
        clearTabColor(tabThree);

        tabFour = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        CommonUtils.setRobotoLightFonts(this, tabFour);
        tabFour.setText("Hospital");
        tabFour.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_hospital, 0, 0);
        tabLayout.getTabAt(3).setCustomView(tabFour);
        clearTabColor(tabFour);

        tabFive = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        CommonUtils.setRobotoLightFonts(this, tabFive);
        tabFive.setText("Stats");
        tabFive.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_stats, 0, 0);
        tabLayout.getTabAt(4).setCustomView(tabFive);
        clearTabColor(tabFive);

    }

    /**
     * Adding fragments to ViewPager
     *
     * @param viewPager
     */
    private void setupViewPager(ViewPager viewPager) {

        adapter = new ViewPagerAdapter(getSupportFragmentManager());

        adapter.addFrag(new LearnFragment(), "Learn");
        adapter.addFrag(new TrackFragment(), "Track");
        adapter.addFrag(new SOSFragment(), "SOS");
        adapter.addFrag(new HospitalFragment(), "Hospital");
        adapter.addFrag(new StatusFragment(), "Status");
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(0);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                //EventBus.getDefault().post(new SaveCurrentDataEvent("" + i));
                //adapter.notifyDataSetChanged();
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            onScanCompletedListener.scanCompleted(result.getContents());
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();
        switch (id) {
            case R.id.all_profiles:
                startActivity(new Intent(TrackActivity.this, ProfileActivity.class));
                break;
            case R.id.logout:
                createDialog();
                break;
            case R.id.change_password:
                startActivity(new Intent(TrackActivity.this, ResetPasswordActivity.class));
                break;
            case R.id.nav_privacy:
                startActivity(new Intent(TrackActivity.this, PrivacyActivity.class));
                break;
            case R.id.nav_terms_condition:
                startActivity(new Intent(TrackActivity.this, TermsAndConditionActivity.class));
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        assert drawer != null;
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void createDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // builder.setTitle("AlertDialog with No Buttons");
        builder.setMessage("Do you wish to logout?");
        //Yes Button
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                removeSharedPreferenceData();
                GlobalClass.userID = null;
                startActivity(new Intent(TrackActivity.this, MainActivity.class));
                finish();
                Log.i("Code2care ", "Yes button Clicked!");
            }
        });

        //No Button
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.i("Code2care ", "No button Clicked!");
                dialog.dismiss();

            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        Button nbutton = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
        nbutton.setTextColor(getResources().getColor(R.color.appBackground));
        Button pbutton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
        pbutton.setTextColor(getResources().getColor(R.color.appBackground));
    }


    protected void backstackFragment() {
        Log.d("Stack count", getSupportFragmentManager().getBackStackEntryCount() + "");

        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            finish();
        }

        getSupportFragmentManager().popBackStack();
        //   getSupportFragmentManager().addOnBackStackChangedListener(getListener());
        removeCurrentFragment();
    }

    private FragmentManager.OnBackStackChangedListener getListener() {
        FragmentManager.OnBackStackChangedListener result = new FragmentManager.OnBackStackChangedListener() {
            public void onBackStackChanged() {
                FragmentManager manager = getSupportFragmentManager();

                if (manager != null) {
                    Log.e(TAG, "onBackStackChanged: COUNT " + manager.getBackStackEntryCount());
                    if (manager.getBackStackEntryCount() > 0) {
                    //    Boolean bool = manager.popBackStackImmediate();
                    Fragment currFrag = (Fragment) manager.findFragmentById(R.id.mainContainer);
                    currFrag.onResume();
                    currFrag.setMenuVisibility(true);

                }else {
                        clearBackStack();
                        toolbarTitle.setText(toolBarText);
                    }
                }
            }
        };

        return result;
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            onBackPressedListener.doBack();

        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        backstackFragment();
    }

    private void removeCurrentFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        Fragment currentFrag = getSupportFragmentManager().findFragmentById(R.id.mainContainer);
        viewPager.setVisibility(View.VISIBLE);
        mainContainer.setVisibility(View.GONE);

        if (currentFrag != null) {
            transaction.remove(currentFrag);
        }
        transaction.commitAllowingStateLoss();
    }


    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }


        public void update() {
            notifyDataSetChanged();
            setupTabIcons();
        }

        @Override
        public int getItemPosition(Object object) {
//            if (object instanceof UpdateableFragment) {
//                ((UpdateableFragment) object).updateSelf();
//            }
//            return super.getItemPosition(object);
            return POSITION_NONE;
        }
    }

    private void removeSharedPreferenceData() {
        appSharedPreferences.removeAllSPData();
//        Intent intent = new Intent(this, MainActivity.class);
//        finish();
//        startActivity(intent);
    }

}
