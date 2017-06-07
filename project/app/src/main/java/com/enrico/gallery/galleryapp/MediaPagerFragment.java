package com.enrico.gallery.galleryapp;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.graphics.Palette;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.afollestad.easyvideoplayer.EasyVideoCallback;
import com.afollestad.easyvideoplayer.EasyVideoPlayer;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.enrico.gallery.galleryapp.utils.BottomSheetMediaActions;
import com.enrico.gallery.galleryapp.utils.RandomMaterialColor;
import com.enrico.gallery.galleryapp.utils.SaveTools;
import com.github.chrisbanes.photoview.PhotoView;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.IOException;

import static android.app.Activity.RESULT_OK;
import static com.enrico.gallery.galleryapp.albums.HeaderRecyclerViewSection.stringContainsItemFromList;

public class MediaPagerFragment extends Fragment implements EasyVideoCallback {

    PhotoView photoView;
    String[] mUrls;
    CoordinatorLayout background;
    BottomSheetBehavior bottomSheetBehavior;

    int pos, color;
    FloatingActionButton fabPlay;
    String url;
    Fragment fragment;
    LinearLayout bottomSheet;

    private String[] VIDEO_EXTENSIONS = {"mp4", "avi", "mpg", "mkv", "webm", "flv", "gif",
            "wmv", "mov", "qt", "m4p", "m4v", "mpeg", "mp2",
            "m2v", "3gp", "3g2", "f4v", "f4p", "f4a", "f4b"};

    private EasyVideoPlayer videoView;

    @Override
    public void onPause() {
        super.onPause();

        if (stringContainsItemFromList(mUrls[pos], VIDEO_EXTENSIONS)) {

            videoView.pause();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        fragment = this;

        mUrls = (String[]) getActivity().getIntent().getExtras().get("urls");

        pos = getArguments().getInt("pos");

        url = mUrls[pos];
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater
                .inflate(R.layout.media_pager, container, false);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final View view = getView();

        if (view != null) {

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    initViews(view);

                    initMediaView(view);

                }
            });

        }
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
                Uri resultUri = result.getUri();

                Glide
                        .with(getActivity())
                        .load(resultUri)
                        .asBitmap()
                        .into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(final Bitmap resource, GlideAnimation glideAnimation) {

                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                        try {
                                            SaveTools.saveImage(resource, url, getActivity());

                                        } catch (IOException ex) {
                                            ex.printStackTrace();
                                        }

                                    }
                                });
                            }
                        });
            }
        }
    }

    private void initViews(View view) {

        bottomSheet = (LinearLayout) view.findViewById(R.id.bottom_sheet_media);

        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);

        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

        photoView = (PhotoView) view.findViewById(R.id.photoView);

    }

    private void initMediaView(View view) {

        fabPlay = (FloatingActionButton) view.findViewById(R.id.fab_play);

        photoView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                BottomSheetMediaActions.show(getActivity(), bottomSheet, url, fragment);

                if (stringContainsItemFromList(mUrls[pos], VIDEO_EXTENSIONS)) {

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

                }

                return false;
            }
        });

        background = (CoordinatorLayout) view.findViewById(R.id.background);

        if (stringContainsItemFromList(mUrls[pos], VIDEO_EXTENSIONS)) {

            videoView = (EasyVideoPlayer) view.findViewById(R.id.videoView);

            videoView.setCallback(this);

            videoView.setSource(Uri.parse(mUrls[pos]));

            fabPlay.setBackgroundTintList(ColorStateList.valueOf(RandomMaterialColor.get()));

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
        }

        Glide.with(getActivity())
                .load(mUrls[pos])
                .asBitmap()
                .into(new BitmapImageViewTarget(photoView) {
                    @Override
                    public void onResourceReady(Bitmap bitmap, GlideAnimation anim) {
                        super.onResourceReady(bitmap, anim);
                        Palette.from(bitmap)
                                .generate(new Palette.PaletteAsyncListener() {
                                    @Override
                                    public void onGenerated(Palette palette) {

                                        int def = 0x000000;

                                        color = palette.getLightMutedColor(def);

                                        if (color == 0) {
                                            color = palette.getDominantColor(def);
                                            if (color == 0) {
                                                color = Color.DKGRAY;
                                            }
                                        }

                                        background.setBackgroundColor(color);

                                    }
                                });
                    }
                });
    }
}
