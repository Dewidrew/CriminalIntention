package com.augmentis.ayp.crimin.model;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;

/**
 * Created by Hattapong on 8/4/2016.
 */
public class PictureUtils {
    public static Bitmap getScaledBit(String path, int destWidth, int destHeight) {
        //Read the dimesion of the image
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        //return null and put meta data (information about the bitmap
        BitmapFactory.decodeFile(path, options);

        float srcWidth = options.outWidth;
        float srcHeight = options.outHeight;

        int inSampleSize = 1;

        if (srcHeight > destHeight || srcWidth > srcHeight) {
            if (srcWidth > srcHeight) {
                inSampleSize = Math.round(srcHeight / destHeight);
            } else {
                inSampleSize = Math.round(srcWidth / destWidth);
            }
        }
        options = new BitmapFactory.Options();
        options.inSampleSize = inSampleSize;

        return BitmapFactory.decodeFile(path, options);

    }

    public static Bitmap getScaledBitmap(String path, Activity activity) {
        Point size = new Point();

        activity.getWindowManager().getDefaultDisplay().getSize(size);

        return getScaledBit(path, size.x, size.y);
    }
}
