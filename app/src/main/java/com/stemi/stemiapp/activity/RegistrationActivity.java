package com.stemi.stemiapp.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.stemi.stemiapp.R;
import com.stemi.stemiapp.databases.UserDetailsTable;
import com.stemi.stemiapp.fragments.UserDetailsFragment;
import com.stemi.stemiapp.model.RegisteredUserDetails;

public class RegistrationActivity extends AppCompatActivity {

    public static RegisteredUserDetails registeredUserDetails = new RegisteredUserDetails();

    private String userId;
    private RegisteredUserDetails userDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        userId = getIntent().getStringExtra("userid");
        UserDetailsTable userDetailsTable = new UserDetailsTable(this);
        userDetails = userDetailsTable.getUserDetails(userId);
        showFragment(new UserDetailsFragment());
    }

    public void showFragment(Fragment fragment) {

        Bundle bundle = new Bundle();
        bundle.putParcelable("user", userDetails);
        fragment.setArguments(bundle);
        String TAG = fragment.getClass().getSimpleName();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        fragmentTransaction.replace(R.id.mainContainer, fragment, TAG);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commitAllowingStateLoss();
    }

    protected void backstackFragment() {
        Log.d("Stack count", getSupportFragmentManager().getBackStackEntryCount() + "");

        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            finish();
        }
        getSupportFragmentManager().popBackStack();
        removeCurrentFragment();
    }

    private void removeCurrentFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        Fragment currentFrag = getSupportFragmentManager()
                .findFragmentById(R.id.mainContainer);

        if (currentFrag != null) {
            transaction.remove(currentFrag);
        }
        transaction.commitAllowingStateLoss();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            finish();
        }

    }

    protected void enableNavigationIcon() {

/*
        toolbar.setNavigationIcon(R.mipmap.back_arrow);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backstackFragment();
            }
        });
*/
    }

    protected void disableNavigationIcon() {
        //*toolbar.setNavigationIcon(null);*/
    }

    protected void setToolbarTitle(int resID) {
       // toolbar.setTitle(resID);
    }
}
