package com.enrico.gallery.galleryapp.utils;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.webkit.MimeTypeMap;

import com.enrico.gallery.galleryapp.R;

import java.io.File;

class ShareUtils {

    static void shareFile(Activity activity, String url) {
        try {
            File myFile = new File(url);
            MimeTypeMap mime = MimeTypeMap.getSingleton();
            String ext = myFile.getName().substring(myFile.getName().lastIndexOf(".") + 1);
            String type = mime.getMimeTypeFromExtension(ext);
            Intent sharingIntent = new Intent("android.intent.action.SEND");
            sharingIntent.setType(type);
            sharingIntent.putExtra("android.intent.extra.STREAM", Uri.fromFile(myFile));
            activity.startActivity(Intent.createChooser(sharingIntent, activity.getString(R.string.shareWith)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
