package com.enrico.gallery.galleryapp.albums;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;

public class AsyncLoadGallery {

    public static void execute(Activity activity, RecyclerView recyclerView, SectionedRecyclerViewAdapter sectionedRecyclerViewAdapter) {

        new populateGallery(activity, recyclerView, sectionedRecyclerViewAdapter).execute();
    }

    private static class populateGallery extends AsyncTask<Void, Void, Void> {

        Activity activity;
        ArrayList<Albums> albumsList;

        private RecyclerView recyclerView;
        private SectionedRecyclerViewAdapter sectionedRecyclerViewAdapter;

        private String[] resultFolders;

        private populateGallery(Activity activity, RecyclerView recyclerView, SectionedRecyclerViewAdapter sectionedRecyclerViewAdapter) {
            this.activity = activity;
            this.recyclerView = recyclerView;
            this.sectionedRecyclerViewAdapter = sectionedRecyclerViewAdapter;
        }


        protected void onPreExecute() {

        }

        @Override
        protected Void doInBackground(Void... params) {

            albumsList = AlbumsUtils.getAllAlbums(activity);
            resultFolders = AlbumsUtils.initFolders(activity, albumsList);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            AlbumsUtils.setupAlbums(activity, resultFolders, recyclerView, sectionedRecyclerViewAdapter);
        }
    }
}
