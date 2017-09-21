package com.stemi.stemiapp.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatRadioButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import com.stemi.stemiapp.R;
import com.stemi.stemiapp.customviews.CircleImageView;
import com.stemi.stemiapp.databases.DBforUserDetails;
import com.stemi.stemiapp.model.RegisteredUserDetails;
import com.stemi.stemiapp.preference.AppSharedPreference;
import com.stemi.stemiapp.utils.GlobalClass;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProfileActivity extends AppCompatActivity {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

     @BindView(R.id.toolbar) Toolbar toolbar;

    @BindView(R.id.btn_add_profile)
    Button btnAddProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);

        //toolbar = (Toolbar) findViewById(R.id.track_toolbar);
        toolbar.setTitle("All Profiles");
        toolbar.setTitleTextColor(getResources().getColor(R.color.appBackground));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        DBforUserDetails dBforUserDetails = new DBforUserDetails(this);
        List<RegisteredUserDetails> allUsers = dBforUserDetails.getAllUsers();

        recyclerView.setAdapter(new ProfileAdapter(allUsers));
    }

    @OnClick(R.id.btn_add_profile)
    public void onAddProfileClick(View view){
        Intent intent = new Intent(ProfileActivity.this, RegistrationActivity.class);
        startActivity(intent);
    }

    private class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.ProfileViewHolder>{

        private List<RegisteredUserDetails> registeredUsers;

        public ProfileAdapter(List<RegisteredUserDetails> registeredUsers){
            this.registeredUsers = registeredUsers;
        }

        @Override
        public ProfileViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View v = LayoutInflater.from(ProfileActivity.this).inflate(R.layout.item_profile, null);
            ProfileViewHolder profileViewHolder = new ProfileViewHolder(v);
            return profileViewHolder;
        }

        @Override
        public void onBindViewHolder(final ProfileViewHolder profileViewHolder, int i) {
            final RegisteredUserDetails user = this.registeredUsers.get(i);
            profileViewHolder.profileName.setText(user.getName());
            profileViewHolder.profileGender.setText(user.getGender());
            profileViewHolder.profileAge.setText(user.getAge());
            profileViewHolder.profileImage.setImageURI(Uri.parse(user.getImgUrl()));
            if(GlobalClass.userID.equals(user.getUniqueId())){
                profileViewHolder.profileCheck.setChecked(true);
            }
            else{
                profileViewHolder.profileCheck.setChecked(false);
            }
            profileViewHolder.profileCheck.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //AppCompatRadioButton radioButton = (AppCompatRadioButton) view;
                    boolean checked = user.getUniqueId().equals(GlobalClass.userID);
                    if(!checked){
                        AlertDialog.Builder builder;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            builder = new AlertDialog.Builder(ProfileActivity.this, android.R.style.Theme_Material_Light_Dialog_Alert);
                        } else {
                            builder = new AlertDialog.Builder(ProfileActivity.this);
                        }

                        builder.setTitle("Switch User");
                        builder.setMessage("Do you really want to switch user ? ")
                                .setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        GlobalClass.userID = user.getUniqueId();
                                        new AppSharedPreference(ProfileActivity.this)
                                                .setUserId(user.getUniqueId());
                                        notifyDataSetChanged();
                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        profileViewHolder.profileCheck.setChecked(false);
                                    }
                                });

                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                    }



                }
            });
        }

        @Override
        public int getItemCount() {
            return this.registeredUsers.size();
        }

        class ProfileViewHolder extends RecyclerView.ViewHolder{

            AppCompatRadioButton profileCheck;
            CircleImageView profileImage;
            TextView profileName;
            TextView profileAge;
            TextView profileGender;

            public ProfileViewHolder(View itemView) {
                super(itemView);
                profileCheck = (AppCompatRadioButton) itemView.findViewById(R.id.profile_check);
                profileImage = (CircleImageView) itemView.findViewById(R.id.profile_image);
                profileName = (TextView) itemView.findViewById(R.id.profile_name);
                profileAge = (TextView) itemView.findViewById(R.id.profile_age);
                profileGender = (TextView) itemView.findViewById(R.id.profile_gender);
            }
        }
    }
}
