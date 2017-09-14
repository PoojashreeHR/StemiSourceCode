package com.stemi.stemiapp.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.stemi.stemiapp.R;
import com.stemi.stemiapp.model.RegisteredUserDetails;

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
      SimpleDateFormat f = new SimpleDateFormat("dd-MM-yyyy h:mm a");
      try {
         Date d = f.parse(s);
         milliseconds = d.getTime();
         Log.e("TAG", "onClick: InMillisec Format" + milliseconds);
      } catch (ParseException e) {
         e.printStackTrace();
      }
      return milliseconds;
   }
}