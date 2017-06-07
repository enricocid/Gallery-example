package com.enrico.gallery.galleryapp.utils;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;

import com.enrico.gallery.galleryapp.R;

public class PermissionUtils {

    public static void rationaleDialog(final Activity activity, String title, String rationale, final int requestCode, final Intent intent) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                activity);

        alertDialogBuilder.setTitle(title);

        alertDialogBuilder
                .setMessage(rationale)
                .setCancelable(false)

                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        switch (requestCode) {
                            case 0:
                                askReadWriteAccess(activity);
                                break;
                            case 1:
                                askCameraAccess(activity, intent);
                                //camera
                                break;

                            case 2:

                                activity.startActivityForResult(intent, 2);
                                break;
                        }

                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();

        alertDialog.show();

    }

    @TargetApi(23)
    public static void askReadWriteAccess(Activity activity) {

        if (!Settings.System.canWrite(activity)) {

            activity.requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE}, 0);

        }
    }

    @TargetApi(23)
    public static void askCameraAccess(Activity activity, Intent intent) {

        if (activity.checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            activity.startActivity(intent);

        } else {

            activity.requestPermissions(new String[]{Manifest.permission.CAMERA}, 1);
        }
    }

    static void askSDCardAccess(final Activity activity) {

        if (SDCardUtils.getSDCardUri(activity).isEmpty()) {

            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);

            rationaleDialog(activity, activity.getString(R.string.sdcard), activity.getString(R.string.sdcardContent), 2, intent);

        }
    }

}
