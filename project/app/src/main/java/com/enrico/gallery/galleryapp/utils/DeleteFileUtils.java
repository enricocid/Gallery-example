package com.enrico.gallery.galleryapp.utils;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.provider.DocumentFile;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.enrico.gallery.galleryapp.R;

import java.io.File;
import java.io.IOException;

import static com.enrico.gallery.galleryapp.utils.PermissionUtils.rationaleDialog;

public class DeleteFileUtils {

    private static void deleteFileFromMediaStore(final ContentResolver contentResolver, File file) {

        String canonicalPath;

        try {
            canonicalPath = file.getCanonicalPath();
        } catch (IOException e) {
            canonicalPath = file.getAbsolutePath();
        }
        final Uri uri = MediaStore.Files.getContentUri("external");
        final int result = contentResolver.delete(uri,
                MediaStore.Files.FileColumns.DATA + "=?", new String[]{canonicalPath});
        if (result == 0) {
            final String absolutePath = file.getAbsolutePath();
            if (!absolutePath.equals(canonicalPath)) {
                contentResolver.delete(uri,
                        MediaStore.Files.FileColumns.DATA + "=?", new String[]{absolutePath});
            }
        }
    }

    private static void deleteFileSAF(String url, Activity activity) {

        if (SDCardUtils.getSDCardUri(activity).isEmpty()) {

            PermissionUtils.askSDCardAccess(activity);

        } else {

            File file = new File(url);

            DocumentFile documentFile = DocumentFile.fromTreeUri(activity, Uri.parse(SDCardUtils.getSDCardUri(activity)));

            String[] parts = (file.getPath()).split("\\/");

            for (int i = 3; i < parts.length; i++) {
                if (documentFile != null) {
                    documentFile = documentFile.findFile(parts[i]);
                }
            }

            if (documentFile == null) {

                Toast.makeText(activity, activity.getString(R.string.notFound), Toast.LENGTH_SHORT)
                        .show();

                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);

                rationaleDialog(activity, activity.getString(R.string.sdcard), activity.getString(R.string.sdcardContent), 2, intent);

            } else {

                if (documentFile.delete()) {

                    deleteFileFromMediaStore(activity.getContentResolver(), file);

                }
            }
        }
    }

    private static void deleteFile(final String url, final Activity activity) {

        final File file = new File(url);

        if (file.delete()) {

            deleteFileFromMediaStore(activity.getContentResolver(), file);

        } else {

            deleteFileSAF(url, activity);
        }
    }

    public static void youSureToDelete(final Activity activity, final String url) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                activity);

        alertDialogBuilder.setTitle(activity.getString(R.string.uSure));

        alertDialogBuilder
                .setMessage(activity.getString(R.string.uSureContent) + url + activity.getString(R.string.questionMark))
                .setCancelable(false)
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                dialog.dismiss();

                            }
                        }
                )
                .setPositiveButton(activity.getString(R.string.delete), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        deleteFile(url, activity);
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();

        alertDialog.show();

    }
}


