package com.dohro7.mobiledtrv2.utility;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class BitmapDecoder {


    public static String convertBitmapToString(String filePath) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 2;
        Bitmap bitmap;
        try {
            bitmap = BitmapFactory.decodeStream(new FileInputStream(filePath), null, options);
            bitmap = Bitmap.createScaledBitmap(bitmap, 120, 120, false);
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream); //compress to which format you want.
            byte[] byte_arr = stream.toByteArray();
            return Base64.encodeToString(byte_arr, Base64.DEFAULT);
        } catch (FileNotFoundException e) {
            return "";
        }
    }
}
