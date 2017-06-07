package com.enrico.gallery.galleryapp.utils;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;

import com.enrico.gallery.galleryapp.R;

import java.io.File;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import static com.enrico.gallery.galleryapp.albums.HeaderRecyclerViewSection.stringContainsItemFromList;

class InfoUtils {

    static String fileName(String url) {

        File file = new File(url);

        return file.getName();
    }

    static String filePath(String url) {

        return url.replace(fileName(url), "");
    }

    static String lastModifiedDate(String url) {

        File file = new File(url);

        Date date = new Date(file.lastModified());

        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.FULL, Locale.getDefault());

        return dateFormat.format(date);
    }

    static double fileSize(String url) {

        File file = new File(url);

        return file.length();
    }

    static String fileResolution(String url, int which) {

        String[] VIDEO_EXTENSIONS = {"mp4", "avi", "mpg", "mkv", "webm", "flv",
                "wmv", "mov", "qt", "m4p", "m4v", "mpeg", "mp2",
                "m2v", "3gp", "3g2", "f4v", "f4p", "f4a", "f4b"};

        String resolution = "";

        if (stringContainsItemFromList(url, VIDEO_EXTENSIONS)) {
            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            retriever.setDataSource(url);

            int videoWidth = Integer.valueOf(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH));
            int videoHeight = Integer.valueOf(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT));

            switch (which) {
                case 0:
                    resolution = String.valueOf(videoWidth);
                    break;
                case 1:
                    resolution = String.valueOf(videoHeight);
                    break;
            }

            retriever.release();

        } else {

            Uri uri = Uri.parse(url);

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(new File(uri.getPath()).getAbsolutePath(), options);
            int imageHeight = options.outHeight;
            int imageWidth = options.outWidth;

            switch (which) {
                case 0:
                    resolution = String.valueOf(imageWidth);
                    break;
                case 1:
                    resolution = String.valueOf(imageHeight);
                    break;
            }

        }

        return resolution;
    }

    static String fileType(Activity activity, String url) {

        String[] VIDEO_EXTENSIONS = {"mp4", "avi", "mpg", "mkv", "webm", "flv",
                "wmv", "mov", "qt", "m4p", "m4v", "mpeg", "mp2",
                "m2v", "3gp", "3g2", "f4v", "f4p", "f4a", "f4b"};

        String fileType;

        if (stringContainsItemFromList(url, VIDEO_EXTENSIONS)) {

            String extensionType = activity.getString(R.string.videoType);

            String extension = url.substring(url.lastIndexOf("."));

            fileType = extensionType + extension;

        } else {

            String extensionType = activity.getString(R.string.imageType);

            String extension = url.substring(url.lastIndexOf("."));

            fileType = extensionType + extension;
        }

        return fileType;
    }
}
