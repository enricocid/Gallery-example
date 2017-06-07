package com.enrico.gallery.galleryapp.albums;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.enrico.gallery.galleryapp.GridUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;

public class AlbumsUtils {

    private static ArrayList<Albums> allAlbums = new ArrayList<>();

    public static ArrayList<Albums> getAllAlbums(Activity activity) {

        Uri externalImagesUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        Uri internalImagesUri = MediaStore.Images.Media.INTERNAL_CONTENT_URI;
        Uri externalVideosUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        Uri internalVideosUri = MediaStore.Video.Media.INTERNAL_CONTENT_URI;
        String imageData = MediaStore.Images.ImageColumns.DATA;
        String imagePath = MediaStore.Images.ImageColumns.DISPLAY_NAME;
        String videoData = MediaStore.Video.VideoColumns.DATA;
        String videoPath = MediaStore.Video.VideoColumns.DISPLAY_NAME;

        allAlbums.clear();

        Cursor cursor;
        int column_index_data;

        String absolutePathOfAlbums;

        String[] ImagesProjection = {imageData, imagePath};

        cursor = activity.getContentResolver().query(externalImagesUri, ImagesProjection, null,
                null, null);

        if (cursor != null) {
            column_index_data = cursor.getColumnIndexOrThrow(imageData);

            while (cursor.moveToNext()) {

                absolutePathOfAlbums = cursor.getString(column_index_data);

                Albums media = new Albums();

                media.setAlbumsUrl(absolutePathOfAlbums);

                allAlbums.add(media);

            }
        }

        cursor = activity.getContentResolver().query(internalImagesUri, ImagesProjection, null,
                null, null);

        if (cursor != null) {
            column_index_data = cursor.getColumnIndexOrThrow(imageData);

            while (cursor.moveToNext()) {

                absolutePathOfAlbums = cursor.getString(column_index_data);

                Albums media = new Albums();

                media.setAlbumsUrl(absolutePathOfAlbums);

                allAlbums.add(media);
            }

            String[] VideosProjection = {videoData, videoPath};

            cursor = activity.getContentResolver().query(externalVideosUri, VideosProjection, null,
                    null, null);

            if (cursor != null) {
                column_index_data = cursor.getColumnIndexOrThrow(videoData);

                while (cursor.moveToNext()) {

                    absolutePathOfAlbums = cursor.getString(column_index_data);

                    Albums media = new Albums();

                    media.setAlbumsUrl(absolutePathOfAlbums);

                    allAlbums.add(media);

                }
            }

            cursor = activity.getContentResolver().query(internalVideosUri, VideosProjection, null,
                    null, null);

            if (cursor != null) {
                column_index_data = cursor.getColumnIndexOrThrow(videoData);

                while (cursor.moveToNext()) {

                    absolutePathOfAlbums = cursor.getString(column_index_data);

                    Albums media = new Albums();

                    media.setAlbumsUrl(absolutePathOfAlbums);

                    allAlbums.add(media);
                }
                cursor.close();

            }
        }
        return allAlbums;
    }

    private static String getFolderName(String path) {

        File folderName = new File(path);

        return folderName.getName();

    }

    public static void setupAlbums(final Activity activity, RecyclerView recyclerView, List<Albums> albumsList, final SectionedRecyclerViewAdapter sectionedRecyclerViewAdapter) {

        int mediaSize = albumsList.size();

        String[] albumsPath = new String[mediaSize];

        Set<String> paths = new HashSet<>();

        for (int i = 0; i < albumsList.size(); i++) {

            albumsPath[i] = albumsList.get(i).getAlbumsPath();

            paths.add(albumsPath[i]);
        }

        String[] veryPaths = paths.toArray(new String[paths.size()]);

        for (String path : veryPaths) {

            String[] mediaUrls = MediaFromAlbums.listMedia(path);

            String currentFolder = getFolderName(path);

            if (mediaUrls.length != 0) {

                final int gridNumber = GridUtils.getGridNumber(activity);
                GridLayoutManager glm = new GridLayoutManager(activity, gridNumber);
                glm.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                    @Override
                    public int getSpanSize(int position) {
                        switch (sectionedRecyclerViewAdapter.getSectionItemViewType(position)) {
                            case SectionedRecyclerViewAdapter.VIEW_TYPE_HEADER:
                                return gridNumber;
                            default:
                                return 1;
                        }
                    }
                });

                recyclerView.setLayoutManager(glm);

                HeaderRecyclerViewSection headerRecyclerViewSection = new HeaderRecyclerViewSection(activity, currentFolder, mediaUrls, sectionedRecyclerViewAdapter, gridNumber);

                sectionedRecyclerViewAdapter.addSection(headerRecyclerViewSection);

            }

        }

    }
}




