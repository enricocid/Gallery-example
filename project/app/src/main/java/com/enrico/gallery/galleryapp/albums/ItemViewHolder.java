package com.enrico.gallery.galleryapp.albums;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.enrico.gallery.galleryapp.R;

class ItemViewHolder extends RecyclerView.ViewHolder {

    ImageView picView, cameraView, gifView;
    View rootView;

    ItemViewHolder(View itemView) {
        super(itemView);

        rootView = itemView;
        picView = (ImageView) itemView.findViewById(R.id.imageView);
        cameraView = (ImageView) itemView.findViewById(R.id.cameraView);
        gifView = (ImageView) itemView.findViewById(R.id.gifView);

    }
}
