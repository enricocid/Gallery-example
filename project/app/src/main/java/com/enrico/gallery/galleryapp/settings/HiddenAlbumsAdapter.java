package com.enrico.gallery.galleryapp.settings;

import android.app.Activity;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.enrico.gallery.galleryapp.R;

import java.util.ArrayList;

class HiddenAlbumsAdapter extends RecyclerView.Adapter<HiddenAlbumsAdapter.SimpleViewHolder> {

    private ArrayList<String> hiddenFolders;
    private Activity activity;

    HiddenAlbumsAdapter(Activity activity, ArrayList<String> hiddenFolders) {

        this.hiddenFolders = hiddenFolders;
        this.activity = activity;

    }

    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.hidden_item, parent, false);
        return new SimpleViewHolder(activity, itemView, hiddenFolders);
    }

    @Override
    public void onBindViewHolder(SimpleViewHolder holder, int position) {

        holder.textView.setText(hiddenFolders.get(position));

    }

    @Override
    public int getItemCount() {

        return hiddenFolders.size();
    }

    static class SimpleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        static ArrayList<String> visibleFolders;
        private TextView textView;
        private ArrayList<String> hiddenFolders;

        private Activity activity;

        private int originalBgColor;

        SimpleViewHolder(Activity activity, View itemView, ArrayList<String> hiddenFolders) {
            super(itemView);

            textView = (TextView) itemView.findViewById(R.id.hidden_folder);

            itemView.setOnClickListener(this);

            this.activity = activity;
            this.hiddenFolders = hiddenFolders;

            this.originalBgColor = itemView.getSolidColor();

            visibleFolders = new ArrayList<>();

        }

        @Override
        public void onClick(View v) {

            if (!itemView.isSelected()) {

                visibleFolders.add(hiddenFolders.get(getAdapterPosition()));

                v.setSelected(true);

                v.setBackgroundColor(ContextCompat.getColor(activity, R.color.material_green_a400));

            } else {

                visibleFolders.remove(hiddenFolders.get(getAdapterPosition()));

                v.setSelected(false);

                v.setBackgroundColor(originalBgColor);
            }
        }
    }
}