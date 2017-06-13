package com.enrico.gallery.galleryapp.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Toast;

import com.enrico.gallery.galleryapp.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class SaveTools {

    private static boolean dir_exists(String dir_path) {
        boolean ret = false;
        File dir = new File(dir_path);
        if (dir.exists() && dir.isDirectory())
            ret = true;
        return ret;
    }

    private static File saveImage(Bitmap bmp, final String fileName, final Activity activity) throws IOException {

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();

        bmp.compress(Bitmap.CompressFormat.PNG, 100, bytes);

        String dir_path = Environment.getExternalStorageDirectory() + File.separator + "GalleryCropShit";

        if (!dir_exists(dir_path)) {
            File directory = new File(dir_path);
            boolean makeDir = directory.mkdirs();
        }

        File f;


        f = new File(dir_path + File.separator

                + fileName);

        FileOutputStream fo = new FileOutputStream(f);
        fo.write(bytes.toByteArray());
        fo.close();

        notifyChange(f, activity);

        return f;
    }

    private static void notifyChange(File f, Activity activity) {

        Intent mediaScannerIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri fileContentUri = Uri.fromFile(f);
        mediaScannerIntent.setData(fileContentUri);
        activity.sendBroadcast(mediaScannerIntent);
    }

    private static void setLastInt(Activity activity, int lastInt) {

        SharedPreferences preferenceName;
        preferenceName = activity.getSharedPreferences("int", Context.MODE_PRIVATE);


        preferenceName.edit()
                .clear()
                .apply();

        preferenceName.edit()
                .putInt("lastInt", lastInt)
                .apply();
    }

    private static int getLastInt(Activity activity) {

        SharedPreferences preferenceName = activity.getSharedPreferences("int", Context.MODE_PRIVATE);

        return preferenceName.getInt("lastInt", 0);
    }

    public static void saveCrop(Activity activity, Uri uri, String url) {

        new saveCropImage(activity, uri, url).execute();
    }

    private static class saveCropImage extends AsyncTask<Void, Void, Void> {

        Activity activity;
        Uri uri;
        String url;
        Bitmap bitmap;
        ProgressDialog progressDialog;
        int count;
        String name;
        String fileNameWithoutExt;
        String fileName;

        private saveCropImage(Activity activity, Uri uri, String url) {
            this.activity = activity;
            this.uri = uri;
            this.url = url;
        }


        protected void onPreExecute() {
            count = getLastInt(activity) + 1;
            progressDialog = new ProgressDialog(activity);
            progressDialog.setMessage(activity.getString(R.string.saving_crop));
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {

            name = url.substring(url.lastIndexOf('/') + 1, url.length());

            fileNameWithoutExt = name.substring(0, name.lastIndexOf('.'));

            fileName = fileNameWithoutExt + "_crop_" + count + ".png";

            try {

                bitmap = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), uri);

                SaveTools.saveImage(bitmap, fileName, activity);

            } catch (IOException ex) {
                ex.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            progressDialog.dismiss();

            Toast.makeText(activity, fileName + activity.getString(R.string.saved), Toast.LENGTH_SHORT)
                    .show();

            setLastInt(activity, count);
        }
    }
}
