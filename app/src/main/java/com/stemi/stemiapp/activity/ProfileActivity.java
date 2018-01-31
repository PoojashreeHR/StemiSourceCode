package com.stemi.stemiapp.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
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
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.stemi.stemiapp.R;
import com.stemi.stemiapp.customviews.CircleImageView;
import com.stemi.stemiapp.databases.UserDetailsTable;
import com.stemi.stemiapp.model.RegisteredUserDetails;
import com.stemi.stemiapp.preference.AppSharedPreference;
import com.stemi.stemiapp.utils.AppConstants;
import com.stemi.stemiapp.utils.CompressImageUtil;
import com.stemi.stemiapp.utils.GlobalClass;

import java.io.File;
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
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        UserDetailsTable dBforUserDetails = new UserDetailsTable(this);
        List<RegisteredUserDetails> allUsers = dBforUserDetails.getAllUsers();

        recyclerView.setAdapter(new ProfileAdapter(allUsers, this));
    }

    @OnClick(R.id.btn_add_profile)
    public void onAddProfileClick(View view){
        Intent intent = new Intent(ProfileActivity.this, RegistrationActivity.class);
        startActivity(intent);
    }

    private class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.ProfileViewHolder>{

        private List<RegisteredUserDetails> registeredUsers;
        Context mContext;
        public ProfileAdapter(List<RegisteredUserDetails> registeredUsers,Context context){
            this.registeredUsers = registeredUsers;
            this.mContext = context;
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
            profileViewHolder.profileGender.setText(user.getGender().toUpperCase());
            profileViewHolder.profileAge.setText(user.getAge());
            if(user.getImgUrl() == null){
                profileViewHolder.profileImage.setImageResource(R.drawable.ic_user);
            }else {

                Bitmap bitmap = new CompressImageUtil().compressImage(ProfileActivity.this,user.getImgUrl());
                profileViewHolder.profileImage.setImageBitmap(bitmap);
            }
            if(GlobalClass.userID != null && GlobalClass.userID.equals(user.getUniqueId())){
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
                                        AppSharedPreference app = new AppSharedPreference(ProfileActivity.this);
                                        app.setUserId(user.getUniqueId());
                                        UserDetailsTable dBforUserDetails = new UserDetailsTable(ProfileActivity.this);
                                        RegisteredUserDetails userDetails = dBforUserDetails.getUserDetails(GlobalClass.userID);
                                        app.addAmbulanceNumb(AppConstants.AMBULANCE_NUMB,userDetails.getAmbulance_numb());
                                        if(userDetails != null && userDetails.getHeight() != null) {
                                            int heightInCms = (int) Double.parseDouble(userDetails.getHeight());
                                            GlobalClass.heightInM = (double) heightInCms / 100;
                                        }

                                        startActivity(new Intent(ProfileActivity.this, TrackActivity.class));
                                        finish();


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
            profileViewHolder.profileEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String userId = user.getUniqueId();
                    Intent intent = new Intent(ProfileActivity.this, RegistrationActivity.class);
                    intent.putExtra("userid", userId);
                    startActivity(intent);
                }
            });

            profileViewHolder.profileDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(user.getUniqueId().equalsIgnoreCase(GlobalClass.userID)){
                        Toast.makeText(ProfileActivity.this, "Currrent active profile can't be deleted !", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    AlertDialog.Builder builder;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        builder = new AlertDialog.Builder(ProfileActivity.this, android.R.style.Theme_Material_Light_Dialog_Alert);
                    } else {
                        builder = new AlertDialog.Builder(ProfileActivity.this);
                    }

                    builder.setTitle("Delete User");
                    builder.setMessage("Do you really want to delete this user ? ")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    UserDetailsTable userDetailsTable = new UserDetailsTable(ProfileActivity.this);
                                    userDetailsTable.deleteEntry(user.getUniqueId());

                                    registeredUsers = userDetailsTable.getAllUsers();

                                    notifyDataSetChanged();
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    //Do Nothing
                                }
                            });

                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();



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
            ImageView profileEdit;
            ImageView profileDelete;

            public ProfileViewHolder(View itemView) {
                super(itemView);
                profileCheck = (AppCompatRadioButton) itemView.findViewById(R.id.profile_check);
                profileImage = (CircleImageView) itemView.findViewById(R.id.profile_image);
                profileName = (TextView) itemView.findViewById(R.id.profile_name);
                profileAge = (TextView) itemView.findViewById(R.id.profile_age);
                profileGender = (TextView) itemView.findViewById(R.id.profile_gender);
                profileEdit = (ImageView) itemView.findViewById(R.id.iv_edit);
                profileDelete = (ImageView) itemView.findViewById(R.id.iv_delete);
            }
        }
    }
}
