package com.enrico.gallery.galleryapp.utils;

import android.app.Activity;
import android.app.WallpaperManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.enrico.gallery.galleryapp.R;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.File;
import java.io.IOException;
import java.util.Locale;

import static com.enrico.gallery.galleryapp.albums.HeaderRecyclerViewSection.stringContainsItemFromList;

public class BottomSheetMediaActions {

    public static void show(final Activity activity, final LinearLayout bottomSheet, final String url, final Fragment ifAny) {

        toggleBottomSheet(bottomSheet);

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                getInfo(activity, bottomSheet, ifAny, url);

            }
        });

    }

    private static void toggleBottomSheet(View bottomSheet) {

        BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);

        int state = bottomSheetBehavior.getState();

        switch (state) {
            case BottomSheetBehavior.STATE_COLLAPSED:
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                break;
            case BottomSheetBehavior.STATE_HIDDEN:
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                break;
            case BottomSheetBehavior.STATE_EXPANDED:
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                break;

        }
    }

    private static void set(final Activity activity, final String url, LinearLayout bottomView, final Fragment ifAny, String name, String path, String date, double fileSize, final String type, String fileWidth, String fileHeight) {

        String[] VIDEO_EXTENSIONS = {"mp4", "avi", "mpg", "mkv", "webm", "flv", "gif",
                "wmv", "mov", "qt", "m4p", "m4v", "mpeg", "mp2",
                "m2v", "3gp", "3g2", "f4v", "f4p", "f4a", "f4b"};

        ImageButton share = (ImageButton) bottomView.findViewById(R.id.share);
        ImageButton crop = (ImageButton) bottomView.findViewById(R.id.crop);
        ImageButton delete = (ImageButton) bottomView.findViewById(R.id.delete);
        final TextView setAsWallpaper = (TextView) bottomView.findViewById(R.id.setAs);

        if (!stringContainsItemFromList(url, VIDEO_EXTENSIONS)) {
            setAsWallpaper.setVisibility(View.VISIBLE);

            crop.setVisibility(View.VISIBLE);
            crop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (ifAny != null) {
                        CropImage.activity(Uri.fromFile(new File(url)))
                                .setActivityMenuIconColor(Color.BLACK)
                                .start(activity, ifAny);
                    } else {
                        CropImage.activity(Uri.fromFile(new File(url)))
                                .setActivityMenuIconColor(Color.BLACK)
                                .start(activity);

                    }


                }
            });

            setAsWallpaper.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Glide
                            .with(activity)
                            .load(url)
                            .asBitmap()
                            .into(new SimpleTarget<Bitmap>() {
                                @Override
                                public void onResourceReady(final Bitmap resource, GlideAnimation glideAnimation) {
                                    final WallpaperManager wallpaperManager = WallpaperManager.getInstance(activity);

                                    activity.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {

                                                wallpaperManager.setBitmap(resource);
                                            } catch (IOException ex) {
                                                ex.printStackTrace();
                                            }
                                        }
                                    });
                                }
                            });
                }
            });
        }

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareUtils.shareFile(activity, url);
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteFileUtils.youSureToDelete(activity, url);
            }
        });

        TextView fileName = (TextView) bottomView.findViewById(R.id.filename);
        TextView size = (TextView) bottomView.findViewById(R.id.size);
        TextView width = (TextView) bottomView.findViewById(R.id.width);
        TextView height = (TextView) bottomView.findViewById(R.id.height);
        TextView filePath = (TextView) bottomView.findViewById(R.id.path);
        TextView fileType = (TextView) bottomView.findViewById(R.id.type);
        TextView fileLastMod = (TextView) bottomView.findViewById(R.id.lastMod);

        fileName.setText(name);
        filePath.setText(path);
        setSize(activity, size, fileSize);
        fileType.setText(type);
        width.setText(fileWidth);
        height.setText(fileHeight);
        fileLastMod.setText(date);
    }

    private static void setSize(Activity activity, TextView textSize, double bytes) {

        double kilobytes = (bytes / 1024);
        double megabytes = (kilobytes / 1024);
        double gigabytes = (megabytes / 1024);

        String resultB = Math.round(bytes) + activity.getString(R.string.B);

        String resultKB = String.format(Locale.getDefault(), "%.2f", kilobytes) + activity.getString(R.string.KB);

        String resultMB = String.format(Locale.getDefault(), "%.2f", megabytes) + activity.getString(R.string.MB);

        String resultGB = String.format(Locale.getDefault(), "%.2f", gigabytes) + activity.getString(R.string.GB);

        if (kilobytes < 1000.000) {
            textSize.setText(resultKB + activity.getString(R.string.parenthesisL) + resultB + activity.getString(R.string.parenthesisR));
        } else if (megabytes >= 1000.000) {
            textSize.setText(resultGB + activity.getString(R.string.parenthesisL) + resultB + activity.getString(R.string.parenthesisR));
        } else {
            textSize.setText(resultMB + activity.getString(R.string.parenthesisL) + resultB + activity.getString(R.string.parenthesisR));
        }
    }

    private static void getInfo(final Activity activity, LinearLayout bottomView, Fragment ifAny, final String url) {

        String fileName = InfoUtils.fileName(url);
        String filePath = InfoUtils.filePath(url);
        String lastModifiedDate = InfoUtils.lastModifiedDate(url);
        double fileSize = InfoUtils.fileSize(url);
        String fileWidth = InfoUtils.fileResolution(url, 0);
        String fileHeight = InfoUtils.fileResolution(url, 1);
        String fileType = InfoUtils.fileType(activity, url);

        BottomSheetMediaActions.set(activity, url, bottomView, ifAny, fileName, filePath, lastModifiedDate, fileSize, fileType, fileWidth, fileHeight);

    }
}
