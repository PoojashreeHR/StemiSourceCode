package com.stemi.stemiapp.activity;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;
import android.support.design.widget.TabLayout;
import android.widget.Toast;

import com.stemi.stemiapp.R;
import com.stemi.stemiapp.customviews.CircleImageView;
import com.stemi.stemiapp.customviews.CustomViewPager;
import com.stemi.stemiapp.fragments.AddMedicineFragment;
import com.stemi.stemiapp.fragments.BloodTestFragment;
import com.stemi.stemiapp.fragments.HospitalFragment;
import com.stemi.stemiapp.fragments.LearnFragment;
import com.stemi.stemiapp.fragments.MedicationFragment;
import com.stemi.stemiapp.fragments.SOSFragment;
import com.stemi.stemiapp.fragments.StatusFragment;
import com.stemi.stemiapp.fragments.TrackFragment;
import com.stemi.stemiapp.model.DataPassListener;
import com.stemi.stemiapp.model.MedicineDetails;
import com.stemi.stemiapp.model.MessageEvent;
import com.stemi.stemiapp.model.UserEventDetails;
import com.stemi.stemiapp.preference.AppSharedPreference;
import com.stemi.stemiapp.utils.AppConstants;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class TrackActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, DataPassListener{

    TabLayout tabLayout;
    Toolbar toolbar;
    CustomViewPager viewPager;
    private DrawerLayout drawer;
    RelativeLayout mainContainer;
    AppSharedPreference appSharedPreferences;
    public static UserEventDetails userEventDetails;
    CircleImageView profileImg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation_layout);

        toolbar = (Toolbar) findViewById(R.id.track_toolbar);
        toolbar.setTitle("Learn");
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorPrimary));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        profileImg = (CircleImageView) findViewById(R.id.profileImg);
        userEventDetails = new UserEventDetails();
        appSharedPreferences = new AppSharedPreference(this);
        viewPager = (CustomViewPager) findViewById(R.id.viewpager);
        viewPager.setPagingEnabled(true);
        mainContainer = (RelativeLayout) findViewById(R.id.mainContainer);

        if(viewPager.getVisibility() == View.VISIBLE) {
            mainContainer.setVisibility(View.GONE);
            setupViewPager(viewPager);
        }else {
            viewPager.setVisibility(View.VISIBLE);
            setupViewPager(viewPager);
        }

        if(appSharedPreferences.getProfileUrl(AppConstants.PROFILE_URL).equals("")){
            profileImg.setImageResource(R.drawable.ic_user);
        }else {
            profileImg.setImageURI(Uri.parse(appSharedPreferences.getProfileUrl(AppConstants.PROFILE_URL)));
        }
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        assert navigationView != null;
        navigationView.setNavigationItemSelectedListener(this);


        tabLayout = (TabLayout)findViewById(R.id.tab);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();
        tabLayout.setTabTextColors(R.color.colorDarkGrey,R.color.appBackground);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                if(viewPager.getVisibility() == View.GONE) {
                    viewPager.setVisibility(View.VISIBLE);
                    mainContainer.setVisibility(View.GONE);
                }
//                for(int i=0;i<tabLayout.getChildCount();i++)
//                {

              //  }

                    /*TabLayout.Tab tab = tabLayout.getTabAt(i);
                    Drawable icon = tab.getIcon();

                    if (icon != null) {
                        icon = DrawableCompat.wrap(icon);
                        DrawableCompat.setTintList(icon, colors);
                    }*/
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    protected OnBackPressedListener onBackPressedListener;

    public interface OnBackPressedListener {
        void doBack();
    }

    public void setOnBackPressedListener(OnBackPressedListener onBackPressedListener) {
        this.onBackPressedListener = onBackPressedListener;
    }
    public void setActionBarTitle(String title){
        toolbar.setTitle(title);
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

    @Override
    public void passData(ArrayList<MedicineDetails> data) {
        AddMedicineFragment addMedicineFragment = new AddMedicineFragment ();
        Bundle args = new Bundle();
        args.putParcelableArrayList("RECIEVE DATA", data );
       // args.putString(AddMedicineFragment.DATA_RECEIVE, data);
        addMedicineFragment .setArguments(args);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.mainContainer, addMedicineFragment).commit();
    }

    @Override
    public void goBack(){

    }
    public CustomViewPager getViewPager(){
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

        TextView tabOne = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabOne.setText("Learn");
        tabOne.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_learn, 0, 0);
        tabLayout.getTabAt(0).setCustomView(tabOne);

        TextView tabTwo = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabTwo.setText("Track");
        tabTwo.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_track, 0, 0);
        tabLayout.getTabAt(1).setCustomView(tabTwo);

        TextView tabThree = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabThree.setText("SOS");
        tabThree.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_sos,0, 0);
        tabLayout.getTabAt(2).setCustomView(tabThree);

        TextView tabfour = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabfour.setText("Hospital");
        tabfour.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_hospital,0, 0);
        tabLayout.getTabAt(3).setCustomView(tabfour);

        TextView tabFive = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabFive.setText("Stats");
        tabFive.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_stats, 0, 0);
        tabLayout.getTabAt(4).setCustomView(tabFive);

    }

    /**
     * Adding fragments to ViewPager
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
        } else{
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
        Intent intent = new Intent(this, MainActivity.class);
        finish();
        startActivity(intent);
    }

}
