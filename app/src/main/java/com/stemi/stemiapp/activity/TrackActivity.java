package com.stemi.stemiapp.activity;


import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.net.Uri;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.stemi.stemiapp.R;
import com.stemi.stemiapp.customviews.CircleImageView;
import com.stemi.stemiapp.customviews.CustomViewPager;
import com.stemi.stemiapp.databases.UserDetailsTable;
import com.stemi.stemiapp.fragments.AddMedicineFragment;
import com.stemi.stemiapp.fragments.HospitalFragment;
import com.stemi.stemiapp.fragments.LearnFragment;
import com.stemi.stemiapp.fragments.SOSFragment;
import com.stemi.stemiapp.fragments.StatusFragment;
import com.stemi.stemiapp.fragments.TrackFragment;
import com.stemi.stemiapp.model.DataPassListener;
import com.stemi.stemiapp.model.MedicineDetails;
import com.stemi.stemiapp.model.MessageEvent;
import com.stemi.stemiapp.model.RegisteredUserDetails;
import com.stemi.stemiapp.model.UserEventDetails;
import com.stemi.stemiapp.preference.AppSharedPreference;
import com.stemi.stemiapp.utils.CommonUtils;
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
    @Override
    protected void onResume() {
        super.onResume();

        UserDetailsTable dBforUserDetails = new UserDetailsTable(this);
        RegisteredUserDetails registeredUserDetails = dBforUserDetails.getUserDetails(GlobalClass.userID);
        Log.e(TAG, "GlobalClass.userID = " + GlobalClass.userID);
        if (registeredUserDetails.getImgUrl() != null) {
            profileImage.setImageURI(Uri.parse(registeredUserDetails.getImgUrl()));
            profileImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(TrackActivity.this, ProfileActivity.class);
                    startActivity(i);
                }
            });
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation_layout);

        toolbar = (Toolbar) findViewById(R.id.track_toolbar);
        toolbar.setTitle("Learn");
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
        assert navigationView != null;
        navigationView.setNavigationItemSelectedListener(this);


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

    public void showFragment(Fragment fragment) {

        String TAG = fragment.getClass().getSimpleName();
        viewPager.setVisibility(View.GONE);
        mainContainer.setVisibility(View.VISIBLE);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        fragmentTransaction.replace(R.id.mainContainer, fragment, TAG);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commitAllowingStateLoss();
    }

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


    public class TabListener implements TabLayout.OnTabSelectedListener {

        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            int position = tab.getPosition();
            if (viewPager.getVisibility() == View.GONE) {
                viewPager.setVisibility(View.VISIBLE);
                mainContainer.setVisibility(View.GONE);
            }

            if (tab.isSelected()) {
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

        }
    }

    @Override
    public void passData(ArrayList<MedicineDetails> data) {
        AddMedicineFragment addMedicineFragment = new AddMedicineFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList("RECIEVE DATA", data);
        // args.putString(AddMedicineFragment.DATA_RECEIVE, data);
        addMedicineFragment.setArguments(args);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.mainContainer, addMedicineFragment).commit();
    }

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
        toolbarTitle.setText("Learn");
        ubTateTab(0,TrackActivity.this);

        tabTwo = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        CommonUtils.setRobotoLightFonts(this, tabTwo);
        tabTwo.setText("Track");
        tabTwo.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_track, 0, 0);
        tabLayout.getTabAt(1).setCustomView(tabTwo);

        tabThree = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        CommonUtils.setRobotoLightFonts(this, tabThree);
        tabThree.setText("SOS");
        tabThree.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_sos, 0, 0);
        tabLayout.getTabAt(2).setCustomView(tabThree);

        tabFour = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        CommonUtils.setRobotoLightFonts(this, tabFour);
        tabFour.setText("Hospital");
        tabFour.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_hospital, 0, 0);
        tabLayout.getTabAt(3).setCustomView(tabFour);

        tabFive = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        CommonUtils.setRobotoLightFonts(this, tabFive);
        tabFive.setText("Stats");
        tabFive.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_stats, 0, 0);
        tabLayout.getTabAt(4).setCustomView(tabFive);

    }

    /**
     * Adding fragments to ViewPager
     *
     * @param viewPager
     */
    private void setupViewPager(ViewPager viewPager) {

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        adapter.addFrag(new LearnFragment(), "Learn");
        adapter.addFrag(new TrackFragment(), "Track");
        adapter.addFrag(new SOSFragment(), "SOS");
        adapter.addFrag(new HospitalFragment(), "Hospital");
        adapter.addFrag(new StatusFragment(), "Status");
        viewPager.setAdapter(adapter);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();
        switch (id) {
            case R.id.Registration:
                startActivity(new Intent(TrackActivity.this, RegistrationActivity.class));
                break;
            case R.id.logout:
                removeSharedPreferenceData();
                startActivity(new Intent(TrackActivity.this, MainActivity.class));
                finish();

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        assert drawer != null;
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

  /*  protected void backstackFragment() {
        Log.d("Stack count", getSupportFragmentManager().getBackStackEntryCount() + "");

        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            finish();
        }
        getSupportFragmentManager().popBackStack();
        removeCurrentFragment();
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
            backstackFragment();
        }
        else{
            super.onBackPressed();
        }

    }*/

    protected void backstackFragment() {
        Log.d("Stack count", getSupportFragmentManager().getBackStackEntryCount() + "");

        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            finish();
        }
        getSupportFragmentManager().popBackStack();
        removeCurrentFragment();
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            onBackPressedListener.doBack();


           /* new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    backstackFragment();
                }
            },1000);
        }*/
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
// This method will be called when a SomeOtherEvent is posted

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        backstackFragment();
        // Toast.makeText(this, event.message, Toast.LENGTH_SHORT).show();
    }

    private void removeCurrentFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        Fragment currentFrag = getSupportFragmentManager()
                .findFragmentById(R.id.mainContainer);
        viewPager.setVisibility(View.VISIBLE);
        mainContainer.setVisibility(View.GONE);

        if (currentFrag != null) {

            getSupportFragmentManager().popBackStack();
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
    }

    private void removeSharedPreferenceData() {
        appSharedPreferences.removeAllSPData();
//        Intent intent = new Intent(this, MainActivity.class);
//        finish();
//        startActivity(intent);
    }

}
