package com.stemi.stemiapp.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.stemi.stemiapp.R;
import com.stemi.stemiapp.activity.MainActivity;
import com.stemi.stemiapp.activity.RegistrationActivity;
import com.stemi.stemiapp.activity.TrackActivity;
import com.stemi.stemiapp.customviews.CircleImageView;
import com.stemi.stemiapp.databases.DBforUserDetails;
import com.stemi.stemiapp.preference.AppSharedPreference;
import com.stemi.stemiapp.utils.AppConstants;
import com.stemi.stemiapp.utils.CommonUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Calendar;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

public class ProfilePhotoFragment extends Fragment implements AppConstants,View.OnClickListener{
    private static final int CAMERA = 2;
    private Uri fileUri; // file url to store image/video
    private static final int SELECT_PICTURE = 1;
    public static final int MEDIA_TYPE_IMAGE = 100;

    static Uri capturedImageUri=null;

    AppSharedPreference appSharedPreference;
    ImageView imgOption;
    private String selectedImagePath;
    private CircleImageView img;
    Button register;
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

        appSharedPreference = new AppSharedPreference(getActivity());
        dBforUserDetails = new DBforUserDetails(getActivity());
        img = (CircleImageView) view.findViewById(R.id.circleImg);
        imgOption = (ImageView) view.findViewById(R.id.bt_imgOption);
        imgOption.setOnClickListener(this);
        register = (Button) view.findViewById(R.id.bt_register);
        register.setOnClickListener(this);
        return view;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                Uri selectedImageUri = data.getData();
                selectedImagePath = getPath(selectedImageUri);
                Log.e(TAG, "Gallery Image Path : " + selectedImagePath);
                RegistrationActivity.registeredUserDetails.setImgUrl(selectedImagePath);
                img.setImageURI(selectedImageUri);

            } else if (requestCode == CAMERA) {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                File file = bitmapConvertToFile(photo,0);
                img.setImageResource(0);
               // selectedImagePath = getPath(Uri.parse(file.toString()));
               // Log.e(TAG, "Camera Image Path : " + selectedImagePath);
                RegistrationActivity.registeredUserDetails.setImgUrl(Uri.parse(file.toString()).toString());
                img.setImageURI(Uri.parse(file.toString()));
                Log.e(TAG, "Camera Image Path : " + Uri.parse(file.toString()));

                //previewCapturedImage();
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

    private void showPictureDialog() {
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(getActivity());
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {
                "Select photo from gallery",
                "Capture photo from camera"};
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                choosePhotoFromGallary();
                                break;
                            case 1:
                                takePhotoFromCamera();
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }

    private void choosePhotoFromGallary() {
    /*    Intent intent = new Intent();
        intent.setType("image*//*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select file to upload "), SELECT_PICTURE);
*/
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(galleryIntent, SELECT_PICTURE);
    }

    private void takePhotoFromCamera(){
       /* File file = new File(Environment.getExternalStorageDirectory(),  ("StemiImg"+".jpg"));
        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }else{
            file.delete();
            try {
                file.createNewFile();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        capturedImageUri = FileProvider.getUriForFile(getActivity(), "AUTHORITY", file);
       // Intent i = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        Intent i = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

        i.putExtra(MediaStore.EXTRA_OUTPUT, capturedImageUri);
        startActivityForResult(i, CAMERA);*/
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivityForResult(cameraIntent, CAMERA);

    }

    private void previewCapturedImage() {
        try {
            // bimatp factory
            fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
            BitmapFactory.Options options = new BitmapFactory.Options();
            final Bitmap bitmap = BitmapFactory.decodeFile(fileUri.getPath(), options);
            bitmapConvertToFile(bitmap,0);
            img.setImageBitmap(bitmap);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    private File bitmapConvertToFile(Bitmap bitmap, int i) {
        File croppedFile = null;
        FileOutputStream fileOutputStream = null;
        try {
            File printFolder = null;
            printFolder = CommonUtils.getOutputMediaFile(getActivity(), 100);
            assert printFolder != null;
            //croppedFile = new File(printFolder.getPath(), "IMG_" + (new SimpleDateFormat("yyyyMMddHHmmss")).format(Calendar.getInstance().getTime()) + ".jpg");

            croppedFile = new File(printFolder.getPath(), "stemiImage1.jpg");
            Bitmap out = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), false);
            fileOutputStream = new FileOutputStream(croppedFile);
            out.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
            out.recycle();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "bitmapConvertToFile: " + e.getMessage());
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.flush();
                    fileOutputStream.close();

                } catch (Exception e) {
                    Log.e(TAG, "bitmapConvertToFile: " + e.getMessage());
                }
            }
        }

        return croppedFile;
    }

    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(CommonUtils.getOutputMediaFile(getActivity(),type));
    }
    private File getDir() {
        File sdDir = Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        return new File(sdDir, "CameraAPIDemo");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_register:
                    String deviceId = Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.ANDROID_ID);
                    Log.e(TAG, "onClick: "+ deviceId);
                    long count = dBforUserDetails.getProfilesCount();
                    count = count+1;
                    RegistrationActivity.registeredUserDetails.setUniqueId(deviceId+"_"+ count);
                    appSharedPreference.addProfileName(PROFILE_NAME,RegistrationActivity.registeredUserDetails.getUniqueId());
                   // dBforUserDetails.removeNote(RegistrationActivity.registeredUserDetails.getName());
                    dBforUserDetails.addEntry(RegistrationActivity.registeredUserDetails);
                    Log.e(TAG, "onClick: DB COUNT " + dBforUserDetails.getProfilesCount() );
                    Toast.makeText(getActivity(), "One row added successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getActivity(), TrackActivity.class));
                    getActivity().finish();
                    break;
            case R.id.bt_imgOption:
                showPictureDialog();
                break;


        }
    }
}
