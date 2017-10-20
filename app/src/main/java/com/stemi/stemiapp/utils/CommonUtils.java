package com.stemi.stemiapp.utils;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Environment;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.TypefaceSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.stemi.stemiapp.R;
import com.stemi.stemiapp.activity.TrackActivity;
import com.stemi.stemiapp.databases.DBForTrackActivities;
import com.stemi.stemiapp.fragments.TrackFragment;
import com.stemi.stemiapp.model.MessageEvent;
import com.stemi.stemiapp.model.RegisteredUserDetails;
import com.stemi.stemiapp.model.UserAcceptedEvent;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Pooja on 18-07-2017.
 */

public class CommonUtils {
   public static RegisteredUserDetails registeredUserDetails;
   static ProgressDialog dialog;
   static  long milliseconds;
   static DBForTrackActivities dbForTrackActivities = new DBForTrackActivities();;

   public static void showLoadingProgress(Context context) {
      dialog = new ProgressDialog(context, R.style.MyTheme);
      dialog.setIndeterminate(true);
      dialog.setCancelable(false);
      dialog.show();

   }


   /**
    * hide Loading Progress(Transparent Theme)
    *
    * @return null
    */
   public static void hideLoadingProgress() {
      if (dialog != null) {
         if (dialog.isShowing())
            dialog.dismiss();
      }
   }


   /**
    * get the fileoutput stream for specific folders,
    * based on given type.
    */
   public static File getOutputMediaFile(Context context, int type) {
      File mFile;
      File printFolder = null;
      String state = Environment.getExternalStorageState();
      mFile = context.getExternalFilesDir(String.valueOf(Context.MODE_PRIVATE));

      if (type == 100) {
         printFolder = new File(mFile +
                 File.separator + context.getResources().getString(R.string.app_name) +
                 File.separator + "ProfileImages");
      }

      // Create the storage directory if it does not exist
      if (!printFolder.exists()) {
         if (!printFolder.mkdirs()) {
            return null;
         }
      }
      //createNoMediaFile(printFolder);
      return printFolder;
   }


   public static long parseToMilliseconds(String s) {
      SimpleDateFormat f = new SimpleDateFormat("dd-MMM-yyyy h:mm a");
      try {
         Date d = f.parse(s);
         milliseconds = d.getTime();
         Log.e("TAG", "onClick: InMillisec Format" + milliseconds);
      } catch (ParseException e) {
         e.printStackTrace();
      }
      return milliseconds;
   }



   public static void buidDialog(final Context mContext, final int value){
      AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
      //builder.setTitle("");
      builder.setMessage("Old Entry Exist !! Do you want to save this?");
      //Yes Button
      builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
         @Override
         public void onClick(DialogInterface dialog, int which) {
            dbForTrackActivities.updateUserTrack(TrackActivity.userEventDetails,value);
            Log.i("Code2care ", "Yes button Clicked!");
            EventBus.getDefault().post(new MessageEvent("Hello!"));
            EventBus.getDefault().post(new UserAcceptedEvent(""+value));
            ((TrackActivity) mContext).setActionBarTitle("Track");

         }
      });

      //No Button
      builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
         @Override
         public void onClick(DialogInterface dialog, int which) {
            Log.i("Code2care ","No button Clicked!");
            dialog.dismiss();
            //((TrackActivity) mContext).showFragment(new TrackFragment());
            EventBus.getDefault().post(new MessageEvent("Hello!"));
            ((TrackActivity) mContext).setActionBarTitle("Track");

         }
      });

      AlertDialog alertDialog = builder.create();
      alertDialog.show();
      Button nbutton = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
      nbutton.setTextColor(mContext.getResources().getColor(R.color.appBackground));
      Button pbutton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
      pbutton.setTextColor(mContext.getResources().getColor(R.color.appBackground));
   }

   /**
    * Set Fonts and size, dynamically
    */

   public static void setRobotoLightFonts(Context context, TextView v) {
      Typeface typeface = Typeface.createFromAsset(context.getAssets(), "fonts/font_roboto_light.ttf");
      v.setTypeface(typeface);
      // CommonUtils.passViewsToChangeTextSize(context, v, textSize);
   }

   public static void setRobotoMediumFonts(Context context, TextView v) {
      Typeface typeface = Typeface.createFromAsset(context.getAssets(), "fonts/font_roboto_medium.ttf");
      v.setTypeface(typeface);
      // CommonUtils.passViewsToChangeTextSize(context, v, textSize);
   }

   public static void setRobotoRegularFonts(Context context, TextView v) {
      Typeface typeface = Typeface.createFromAsset(context.getAssets(), "fonts/font_roboto_regular.ttf");
      v.setTypeface(typeface);
      //    CommonUtils.passViewsToChangeTextSize(context, v, textSize);
   }

   public static void setRobotoBoldFonts(Context context, TextView v) {
      Typeface typeface = Typeface.createFromAsset(context.getAssets(), "fonts/roboto_bold.ttf");
      v.setTypeface(typeface);
      // CommonUtils.passViewsToChangeTextSize(context, v, textSize);
   }

   public static void setRalewaySemiBold(Context context, TextView v) {
      Typeface typeface = Typeface.createFromAsset(context.getAssets(), "fonts/raleway_semibold.ttf");
      v.setTypeface(typeface);
      // CommonUtils.passViewsToChangeTextSize(context, v, textSize);
   }



}