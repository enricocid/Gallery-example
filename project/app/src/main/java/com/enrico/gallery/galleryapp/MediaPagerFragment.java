package com.enrico.gallery.galleryapp;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.afollestad.easyvideoplayer.EasyVideoCallback;
import com.afollestad.easyvideoplayer.EasyVideoPlayer;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.enrico.gallery.galleryapp.utils.BottomSheetMediaActions;
import com.enrico.gallery.galleryapp.utils.SaveTools;
import com.github.chrisbanes.photoview.PhotoView;
import com.theartofdev.edmodo.cropper.CropImage;

import static android.app.Activity.RESULT_OK;
import static com.enrico.gallery.galleryapp.albums.HeaderRecyclerViewSection.stringContainsItemFromList;

public class MediaPagerFragment extends Fragment implements EasyVideoCallback {

    PhotoView photoView;
    String[] mUrls;
    BottomSheetBehavior bottomSheetBehavior;

    int pos;
    FloatingActionButton fabPlay;
    String url;
    Fragment fragment;
    LinearLayout bottomSheet;

    private String[] VIDEO_EXTENSIONS = {"mp4", "avi", "mpg", "mkv", "webm", "flv",
            "wmv", "mov", "qt", "m4p", "m4v", "mpeg", "mp2",
            "m2v", "3gp", "3g2", "f4v", "f4p", "f4a", "f4b"};

    private EasyVideoPlayer videoView;

    @Override
    public void onPause() {
        super.onPause();

        videoView.pause();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        fragment = this;

        mUrls = (String[]) getActivity().getIntent().getExtras().get("urls");

        pos = getArguments().getInt("pos");

        url = mUrls[pos];

        setRetainInstance(true);
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

                    initMediaView();

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

                SaveTools.saveCrop(getActivity(), resultUri, url);
            }
        }
    }

    private void initViews(View view) {

        bottomSheet = (LinearLayout) view.findViewById(R.id.bottom_sheet_media);

        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);

        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

        photoView = (PhotoView) view.findViewById(R.id.photoView);

        videoView = (EasyVideoPlayer) view.findViewById(R.id.videoView);

        fabPlay = (FloatingActionButton) view.findViewById(R.id.fab_play);

    }

    private void initMediaView() {

        if (stringContainsItemFromList(url, VIDEO_EXTENSIONS)) {

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

                    BottomSheetMediaActions.show(getActivity(), bottomSheet, url, fragment);

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

                    BottomSheetMediaActions.show(getActivity(), bottomSheet, url, fragment);
                    return false;
                }
            });
        }

        ColorDrawable transparentDrawable = new ColorDrawable(Color.TRANSPARENT);

        Glide.with(getActivity())
                .load(url)
                .crossFade()
                .placeholder(transparentDrawable)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(photoView);
    }
}
