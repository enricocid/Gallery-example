package com.enrico.gallery.galleryapp;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;

public class MediaObserver extends ContentObserver {

    private Activity activity;

    private MediaObserver(Handler mHandler, Activity activity) {

        super(mHandler);
        this.activity = activity;
    }

    static void removeContentObserver(Activity activity) {

        ContentResolver contentResolver = activity.getContentResolver();

        contentResolver.unregisterContentObserver(new MediaObserver(new Handler(), activity));

    }

    static void initContentObserver(Activity activity) {

        Uri externalImagesUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        Uri internalImagesUri = MediaStore.Images.Media.INTERNAL_CONTENT_URI;
        Uri externalVideosUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        Uri internalVideosUri = MediaStore.Video.Media.INTERNAL_CONTENT_URI;

        ContentResolver contentResolver = activity.getContentResolver();

        contentResolver.
                registerContentObserver(
                        externalImagesUri,
                        true,
                        new MediaObserver(new Handler(), activity));

        contentResolver.
                registerContentObserver(
                        internalImagesUri,
                        true,
                        new MediaObserver(new Handler(), activity));

        contentResolver.
                registerContentObserver(
                        externalVideosUri,
                        true,
                        new MediaObserver(new Handler(), activity));

        contentResolver.
                registerContentObserver(
                        internalVideosUri,
                        true,
                        new MediaObserver(new Handler(), activity));
    }

    @Override
    public void onChange(boolean selfChange, Uri uri) {

        restart(activity);

    }

    public static void restart(Activity activity) {
        activity.finish();
        activity.startActivity(MainActivity.starterIntent);
        activity.overridePendingTransition(0, 0);
    }
}
