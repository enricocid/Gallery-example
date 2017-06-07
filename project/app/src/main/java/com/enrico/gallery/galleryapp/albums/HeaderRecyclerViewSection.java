package com.enrico.gallery.galleryapp.albums;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;

import com.bumptech.glide.Glide;
import com.enrico.gallery.galleryapp.MediaActivity;
import com.enrico.gallery.galleryapp.R;
import com.enrico.gallery.galleryapp.utils.DeleteFileUtils;

import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;
import io.github.luizgrp.sectionedrecyclerviewadapter.StatelessSection;

public class HeaderRecyclerViewSection extends StatelessSection {

    private int gridNumber;
    private boolean expanded = true;
    private String title;
    private String[] mUrls;
    private Activity activity;
    private SectionedRecyclerViewAdapter sectionedRecyclerViewAdapter;

    private String[] VIDEO_EXTENSIONS = {"mp4", "avi", "mpg", "mkv", "webm", "flv",
            "wmv", "mov", "qt", "m4p", "m4v", "mpeg", "mp2",
            "m2v", "3gp", "3g2", "f4v", "f4p", "f4a", "f4b"};

    HeaderRecyclerViewSection(Activity activity, String title, String[] mUrls, SectionedRecyclerViewAdapter sectionedRecyclerViewAdapter, int gridNumber) {
        super(R.layout.header_layout, R.layout.media_item);
        this.title = title;
        this.mUrls = mUrls;
        this.activity = activity;
        this.sectionedRecyclerViewAdapter = sectionedRecyclerViewAdapter;
        this.gridNumber = gridNumber;
    }

    public static boolean stringContainsItemFromList(String inputStr, String[] items) {

        for (String item : items) {
            if (inputStr.contains(item)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public int getContentItemsTotal() {
        return expanded ? mUrls.length : 0;
    }

    @Override
    public RecyclerView.ViewHolder getItemViewHolder(View view) {
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindItemViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final ItemViewHolder iHolder = (ItemViewHolder) holder;

        final String url = mUrls[position];

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if (stringContainsItemFromList(url, VIDEO_EXTENSIONS)) {

                    iHolder.cameraView.setVisibility(View.VISIBLE);

                }

                if (url.contains(".gif")) {
                    iHolder.gifView.setVisibility(View.VISIBLE);
                }

                Glide.with(activity)
                        .load(url)
                        .crossFade()
                        .placeholder(R.mipmap.ic_mood)
                        .into(iHolder.picView);

                iHolder.rootView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Bundle bundle = new Bundle();

                        bundle.putStringArray("urls", mUrls);
                        Intent intent = new Intent(activity,
                                MediaActivity.class);

                        intent.putExtras(bundle);
                        intent.putExtra("pos", position);

                        intent.putExtra("count", getContentItemsTotal());

                        activity.startActivity(intent);
                    }
                });

                iHolder.rootView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {

                        DeleteFileUtils.youSureToDelete(activity, mUrls[position]);
                        return false;
                    }
                });
            }
        });

    }

    @Override
    public RecyclerView.ViewHolder getHeaderViewHolder(View view) {
        return new HeaderViewHolder(view);
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder) {
        final HeaderViewHolder hHolder = (HeaderViewHolder) holder;
        hHolder.headerTitle.setText(title);

        switch (gridNumber) {
            case 2:
                hHolder.headerTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                break;
            case 3:
                hHolder.headerTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                break;
            case 4:
                hHolder.headerTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                break;
        }

        hHolder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expanded = !expanded;
                hHolder.imgArrow.setImageResource(
                        expanded ? R.drawable.ic_arrow_up : R.drawable.ic_arrow_down
                );

                sectionedRecyclerViewAdapter.notifyDataSetChanged();
            }
        });
    }
}