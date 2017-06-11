package com.enrico.gallery.galleryapp.albums;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.enrico.gallery.galleryapp.R;

class HeaderViewHolder extends RecyclerView.ViewHolder {

    TextView headerTitle;
    ImageView imgArrow;
    ImageButton imgVisibilty;

    HeaderViewHolder(View itemView) {
        super(itemView);
        headerTitle = (TextView) itemView.findViewById(R.id.header_id);
        imgArrow = (ImageView) itemView.findViewById(R.id.imgArrow);
        imgVisibilty = (ImageButton) itemView.findViewById(R.id.imgHide);
    }
}