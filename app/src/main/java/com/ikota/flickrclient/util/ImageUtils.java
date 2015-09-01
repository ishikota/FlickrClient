package com.ikota.flickrclient.util;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.util.Pair;
import android.widget.Toast;

import com.ikota.flickrclient.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@SuppressWarnings("unused")
public class ImageUtils {
    // this String is used as directory name which would be stored captured image.
    private static final String SAVE_DIR_NAME = "Flickr Client";

    public static Drawable tintImage(Context context, Drawable src, int res_color) {
        int tint = context.getResources().getColor(res_color);
        PorterDuff.Mode mode = PorterDuff.Mode.SRC_ATOP;
        src.setColorFilter(tint, mode);
        return src;
    }

    /**
     * Save passed picture to specified directory.
     * If failed then show toast.
     * @param context : context
     * @param data : byte array of picture to save.
     */
    public static File savePicture(Context context, byte[] data, boolean if_register_to_gallery) {
        // if failed in saving picture then flg set to true
        boolean flg = true;
        File pictureFile = getOutputMediaFile();
        try {
            if(data == null || pictureFile == null) throw new IllegalArgumentException("nullpo");
            FileOutputStream fos = new FileOutputStream(pictureFile);
            fos.write(data);
            fos.close();
            if(if_register_to_gallery) registerToMediaScanner(context, pictureFile);
        } catch (FileNotFoundException e) {
            Log.d("Camera Util", "File not found: " + e.getMessage());
            flg = false;
        } catch (IOException e) {
            Log.d("Camera Util", "Error accessing file: " + e.getMessage());
            flg = false;
        }

        if(context!=null) {
            String message;
            if (flg) message = context.getResources().getString(R.string.save_success);
            else message = context.getResources().getString(R.string.save_failed);
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        }
        return pictureFile;
    }

    public static Pair<Boolean, File> savePicture(Context context, Bitmap bmp, boolean if_register_to_gallery) {
        boolean flg = true;
        FileOutputStream fos = null;
        File pictureFile = getOutputMediaFile();
        try {
            if(bmp == null || pictureFile == null) throw new IllegalArgumentException("nullpo");
            fos = new FileOutputStream(pictureFile);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.close();
            if(if_register_to_gallery) registerToMediaScanner(context, pictureFile);
        } catch (IllegalArgumentException e) {
            Log.d("savePicture", "passed bitmap is null");
            flg = false;
        } catch (FileNotFoundException e) {
            Log.d("Camera Util", "File not found: " + e.getMessage());
            flg = false;
        } catch (IOException e) {
            Log.d("Camera Util", "Error accessing file: " + e.getMessage());
            flg = false;
        } finally {
            if(fos!=null) try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new Pair<>(flg, pictureFile);
    }

    /** Create a file Uri for saving an image */
    public static Uri getOutputMediaFileUri(){
        File file = getOutputMediaFile();
        if(file!=null) return Uri.fromFile(getOutputMediaFile());
        else return null;
    }

    /*
        Create a File for saving an image or video

        ** CAUTION
        You need to add below permission in AndroidManifest.xml to use this method.
         <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
        **

     */
    public static File getOutputMediaFile(){
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), SAVE_DIR_NAME);
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Log.d("getOutputMediaFile", "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH).format(new Date());
        File mediaFile;

        mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                "IMG_"+ timeStamp + ".jpg");

        return mediaFile;
    }

    public static void registerToMediaScanner(Context context, File pictureFile) {
        String[] paths = {pictureFile.getPath()};
        String[] mimeTypes = {"image/*"};
        MediaScannerConnection.scanFile(context, paths, mimeTypes, null);
    }

    public static void shareImage(final String img_url, final Activity context) {
        final Target IMG_LOAD_HANDLER = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                Resources r = context.getResources();
                File dist = ImageUtils.savePicture(context, bitmap, true).second;
                if (dist != null) {
                    Uri uri = Uri.fromFile(dist);
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("image/jpg");
                    intent.putExtra(Intent.EXTRA_STREAM, uri);
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                    try {
                        context.startActivity(Intent.createChooser(intent, r.getString(R.string.share_intent_title)));
                    } catch (ActivityNotFoundException e) {
                        Toast.makeText(context,r.getString(R.string.share_failed),Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context,r.getString(R.string.share_failed),Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
                Toast.makeText(context,
                        context.getResources().getString(R.string.share_failed),
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
                // do nothing
            }
        };
        Picasso.with(context)
                .load(img_url)
                .into(IMG_LOAD_HANDLER);
    }
}
