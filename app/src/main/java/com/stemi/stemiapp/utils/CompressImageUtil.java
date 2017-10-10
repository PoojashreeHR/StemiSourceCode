package com.stemi.stemiapp.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;

import com.squareup.picasso.Picasso;

import java.io.IOException;

/**
 * Created by Asif on 09-01-2017.
 */

public class CompressImageUtil {

    public Bitmap compressImage(Context context, String imageUri) {

        String filePath = imageUri;
        Bitmap scaledBitmap = null;
        int actualHeight = 0;
        int actualWidth = 0;
        BitmapFactory.Options options = new BitmapFactory.Options();

//      by setting this field as true, the actual bitmap pixels are not loaded in the memory. Just the bounds are loaded. If
//      you try to use the bitmap here, you will get null.
        options.inJustDecodeBounds = true;
        Bitmap bmp = null;

                bmp = BitmapFactory.decodeFile(filePath, options);
                actualHeight = options.outHeight;
                actualWidth = options.outWidth;

        //CommonUtils.loge("Log", " Actual Height " + actualHeight);   //3003
        //CommonUtils.loge("Log", " Actual Width " + actualWidth);     //3500
        //CommonUtils.loge("Log", " Actual URI " + imageUri);
//      max Height and width values of the compressed image is taken as 816x612


        float maxHeight = 816.0f;
        float maxWidth = 572.0f;
        float imgRatio = 0;        //1.0
        float maxRatio = 0;              //0.75
        try {
            imgRatio = actualWidth / actualHeight;
            maxRatio = maxWidth / maxHeight;
        } catch (Exception e) {
            e.printStackTrace();
            maxRatio = maxWidth / maxHeight;
        }

//      width and height voalues are set maintaining the aspect ratio of the image

        if (actualHeight > maxHeight || actualWidth > maxWidth) {

            if (imgRatio < maxRatio) {

                imgRatio = maxHeight / actualHeight;

                actualWidth = (int) (imgRatio * actualWidth);

                actualHeight = (int) maxHeight;


            } else if (imgRatio > maxRatio) {

                imgRatio = maxWidth / actualWidth;           // 612 /   3500

                actualHeight = (int) (imgRatio * actualHeight);     // 0.17485714 * 3005

                actualWidth = (int) maxWidth;                   //612
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;

            }
        }

//      setting inSampleSize value allows to load a scaled down version of the original image

        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);

//      inJustDecodeBounds set to false to load the actual bitmap
        options.inJustDecodeBounds = false;

//      this options allow android to claim the bitmap memory if it runs low on memory
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];

        try {
//          load the bitmap from its path

                bmp = BitmapFactory.decodeFile(filePath, options);


        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();

        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }

        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

//      check the rotation of the image and display it properly
        ExifInterface exif;
        try {
            exif = new ExifInterface(filePath);

            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, 0);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);

            } else if (orientation == 3) {
                matrix.postRotate(180);
            } else if (orientation == 8) {
                matrix.postRotate(270);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
                    scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix,
                    true);
        } catch (IOException e) {
            e.printStackTrace();
        }

       /* FileOutputStream out = null;
        String filename = getFilename();
        try {
            out = new FileOutputStream(filename);

//          write the compressed bitmap at the destination specified by filename.
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);
            ivCompressed.setImageBitmap(scaledBitmap);
            ivCompressed.setVisibility(View.VISIBLE);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }*/

        return scaledBitmap;

    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;

        int inSampleSize = 1;



        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);     //  3003    /   525 = 6
            final int widthRatio = Math.round((float) width / (float) reqWidth);        //  3500    /   612 = 6
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;         //  6
        }
        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;

        }
        return inSampleSize;
    }

}
