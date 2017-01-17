package com.example.a21corp.vinca;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by ymuslu on 09-01-2017.
 */

public class ImageExporter {

    private static final int WRITE_EXTERNAL_STORAGE_STATE = 255;

    public void viewToJPG(Activity act, View view, String title) {
        File dir = new File(Environment.getExternalStoragePublicDirectory
                (Environment.DIRECTORY_PICTURES),"VINCA");
        viewToJPG(act, view, title, dir);
    }

    public void viewToJPG(Activity act, View view, String title, File dir) {
        //Time taken to draw image
        Long start = SystemClock.currentThreadTimeMillis();
        if (!checkPermissionForWriting(act)) {
            Log.d("ImageExporter", "No permission to save in Gallery!");
            return; //TODO Billedet skal gemmes hvis vi får tilladelse til at gemme i galleriet
        }
        File file = saveBitMap(act, view, title, dir);
        if (file != null) {
            Log.i("TAG", "Drawing saved to the gallery!");
        } else {
            Log.i("TAG", "Oops! Image could not be saved.");
        }
        Long total = SystemClock.currentThreadTimeMillis() - start;
        Log.d("ImageExporter", "Saving image took " + total + " ms");
    }

    private File saveBitMap(Activity act, View view, String title, File dir){
        if (!dir.exists()) {
            boolean isDirectoryCreated = dir.mkdirs();
            if(!isDirectoryCreated)
                Log.i("TAG", "Can't create directory to save the image");
            return null;
        }
        String filename = dir.getPath() + File.separator  + title + ".png";
        File pictureFile = new File(filename);
        Bitmap bitmap =getBitmapFromView(view);
        if (bitmap == null) {
            Log.d("TAG", "Bitmap == Null");
            return null;
        }
        try {
            pictureFile.createNewFile();
            FileOutputStream oStream = new FileOutputStream(pictureFile, false);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, oStream);
            oStream.flush();
            oStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            Log.i("TAG", "There was an issue saving the image.");
        }
        scanGallery(act, pictureFile.getAbsolutePath());
        return pictureFile;
    }

    //create bitmap from view and returns it
    private Bitmap getBitmapFromView(View view) {
        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight()
                , Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.draw(canvas);
        return bitmap;
    }

    // used for scanning gallery
    private void scanGallery(Activity act, String path) {
        try {
            MediaScannerConnection.scanFile(act, new String[] { path }
                    , null, new MediaScannerConnection.OnScanCompletedListener() {
                        public void onScanCompleted(String path, Uri uri) {
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean checkPermissionForWriting(Activity act) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            int permissionCheck =
                    act.checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                if (act.shouldShowRequestPermissionRationale
                        (android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                } else {
                    act.requestPermissions
                            (new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}
                                    , WRITE_EXTERNAL_STORAGE_STATE);
                }
                if (act.checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED) {
                    return true;
                }
                return false;
            }
        }
        return true;
    }
}
