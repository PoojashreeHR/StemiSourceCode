package com.stemi.stemiapp.fragments;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.stemi.stemiapp.R;
import com.stemi.stemiapp.activity.RegistrationActivity;
import com.stemi.stemiapp.activity.TrackActivity;
import com.stemi.stemiapp.customviews.CircleImageView;
import com.stemi.stemiapp.databases.DBforUserDetails;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

public class ProfilePhotoFragment extends Fragment implements View.OnClickListener{
    private static final int SELECT_PICTURE = 1;

    private String selectedImagePath;
    private CircleImageView img;
    private Button register;
    DBforUserDetails dBforUserDetails;

    public ProfilePhotoFragment() {
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
        View view = inflater.inflate(R.layout.fragment_profile_photo, container, false);

        dBforUserDetails = new DBforUserDetails(getActivity());
        img = (CircleImageView) view.findViewById(R.id.circleImg);
        register = (Button) view.findViewById(R.id.bt_register);
        register.setOnClickListener(this);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Picture"), SELECT_PICTURE);
            }
        });
        return view;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                Uri selectedImageUri = data.getData();
                selectedImagePath = getPath(selectedImageUri);
                Log.e(TAG, "Image Path : " + selectedImagePath);
                RegistrationActivity.registeredUserDetails.setImgUrl(selectedImagePath);
                img.setImageURI(selectedImageUri);
            }
        }
    }

    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = this.getActivity().managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_register:
                    String deviceId = Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.ANDROID_ID);
                    Log.e(TAG, "onClick: "+ deviceId);
                    RegistrationActivity.registeredUserDetails.setUniqueId(deviceId);
                   // dBforUserDetails.removeNote(RegistrationActivity.registeredUserDetails.getName());
                    dBforUserDetails.addEntry(RegistrationActivity.registeredUserDetails);
                    Log.e(TAG, "onClick: DB COUNT " + dBforUserDetails.getProfilesCount() );
                    Toast.makeText(getActivity(), "One row added successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getActivity(), TrackActivity.class));
                    getActivity().finish();
        }
    }
}
