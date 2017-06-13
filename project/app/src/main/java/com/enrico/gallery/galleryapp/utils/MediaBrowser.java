package com.enrico.gallery.galleryapp.utils;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.view.View;
import android.widget.LinearLayout;

import com.afollestad.easyvideoplayer.EasyVideoCallback;
import com.afollestad.easyvideoplayer.EasyVideoPlayer;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.enrico.gallery.galleryapp.ImmersiveMode;
import com.enrico.gallery.galleryapp.R;
import com.enrico.gallery.galleryapp.settings.Preferences;
import com.github.chrisbanes.photoview.PhotoView;
import com.theartofdev.edmodo.cropper.CropImage;

public class MediaBrowser extends AppCompatActivity implements EasyVideoCallback {

    PhotoView photoView;

    BottomSheetBehavior bottomSheetBehavior;

    FloatingActionButton fabPlay;

    String url;

    LinearLayout bottomSheet;

    ContextThemeWrapper contextThemeWrapper;
    String type;
    private EasyVideoPlayer videoView;

    @Override
    public void onPause() {
        super.onPause();

        videoView.pause();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                contextThemeWrapper = new ContextThemeWrapper(getBaseContext(), MediaBrowser.this.getTheme());

                Preferences.applyTheme(contextThemeWrapper, getBaseContext());

                ImmersiveMode.On(MediaBrowser.this);

            }
        });

        setContentView(R.layout.media_pager);

        Intent intent = getIntent();
        String action = intent.getAction();
        type = intent.getType();

        if (Intent.ACTION_VIEW.equals(action) && type != null) {

            if (type.startsWith("image/") || type.startsWith("video/")) {

                Uri imageUri = intent.getData();

                url = convertMediaUriToPath(imageUri);

            }

        }

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                initViews();

                initMediaView();

            }
        });

    }

    @Override
    public void onPreparing(EasyVideoPlayer player) {
        // TODO handle if needed
    }

    @Override
    public void onPrepared(EasyVideoPlayer player) {
        // TODO handle
    }

    @Override
    public void onBuffering(int percent) {
        // TODO handle if needed
    }

    @Override
    public void onError(EasyVideoPlayer player, Exception e) {
        // TODO handle
    }

    @Override
    public void onCompletion(EasyVideoPlayer player) {
        // TODO handle if needed

        fabPlay.show(new FloatingActionButton.OnVisibilityChangedListener() {
            @Override
            public void onShown(FloatingActionButton fab) {
                photoView.setVisibility(View.VISIBLE);
                videoView.setVisibility(View.GONE);

            }
        });
    }

    @Override
    public void onRetry(EasyVideoPlayer player, Uri source) {
        // TODO handle if used
    }

    @Override
    public void onSubmit(EasyVideoPlayer player, Uri source) {
        // TODO handle if used
    }

    @Override
    public void onStarted(EasyVideoPlayer player) {
        // TODO handle if needed
    }

    @Override
    public void onPaused(EasyVideoPlayer player) {
        // TODO handle if needed
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                final Uri resultUri = result.getUri();

                SaveTools.saveCrop(MediaBrowser.this, resultUri, url);

            }
        }
    }

    private void initViews() {

        bottomSheet = (LinearLayout) findViewById(R.id.bottom_sheet_media);

        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);

        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

        photoView = (PhotoView) findViewById(R.id.photoView);

        videoView = (EasyVideoPlayer) findViewById(R.id.videoView);

        fabPlay = (FloatingActionButton) findViewById(R.id.fab_play);

    }

    private void initMediaView() {

        if (url.contains(type)) {

            videoView.setCallback(this);

            videoView.setSource(Uri.parse(url));

            fabPlay.show();
            fabPlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    videoView.setVisibility(View.VISIBLE);
                    photoView.setVisibility(View.GONE);

                    if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
                        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                    }

                    fabPlay.hide(new FloatingActionButton.OnVisibilityChangedListener() {
                        @Override
                        public void onHidden(FloatingActionButton fab) {

                            videoView.start();

                        }
                    });
                }
            });

            photoView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    BottomSheetMediaActions.show(MediaBrowser.this, bottomSheet, url, null);

                    bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
                        @Override
                        public void onStateChanged(@NonNull View bottomSheet, int newState) {

                            switch (newState) {
                                case BottomSheetBehavior.STATE_EXPANDED:

                                    fabPlay.hide();
                                    break;
                                case BottomSheetBehavior.STATE_COLLAPSED:

                                    fabPlay.show();
                                    break;

                                case BottomSheetBehavior.STATE_HIDDEN:

                                    if (!videoView.isShown()) {
                                        fabPlay.show();
                                    }
                            }
                        }

                        @Override
                        public void onSlide(@NonNull View bottomSheet, float slideOffset) {

                        }
                    });

                    return false;
                }
            });
        } else {

            photoView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    BottomSheetMediaActions.show(MediaBrowser.this, bottomSheet, url, null);
                    return false;
                }
            });
        }

        ColorDrawable transparentDrawable = new ColorDrawable(Color.TRANSPARENT);

        Glide.with(this)
                .load(url)
                .crossFade()
                .placeholder(transparentDrawable)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(photoView);
    }

    protected String convertMediaUriToPath(Uri uri) {

        String filePath = "";

        Cursor cursor = this.getContentResolver().query(uri, new String[]{MediaStore.MediaColumns.DATA}, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
            filePath = cursor.getString(0);
            cursor.close();
        }

        return filePath;
    }
}
