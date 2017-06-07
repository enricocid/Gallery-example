package com.enrico.gallery.galleryapp;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.graphics.drawable.AnimatedVectorDrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.enrico.gallery.galleryapp.albums.Albums;
import com.enrico.gallery.galleryapp.albums.AlbumsUtils;
import com.enrico.gallery.galleryapp.utils.PermissionUtils;
import com.enrico.gallery.galleryapp.utils.SDCardUtils;
import com.enrico.gallery.galleryapp.utils.ThemeUtils;

import java.util.List;

import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;

public class MainActivity extends AppCompatActivity {

    static Intent starterIntent;
    FloatingActionButton fabPhotos, fabVideos, fabPlus;
    Intent intentVideo = new Intent(MediaStore.INTENT_ACTION_VIDEO_CAMERA);
    Intent intentCamera = new Intent(MediaStore.INTENT_ACTION_STILL_IMAGE_CAMERA);
    ContextThemeWrapper contextThemeWrapper;
    List<Albums> albumsList;
    View clickedFab;

    RecyclerView recyclerView;
    SectionedRecyclerViewAdapter sectionedRecyclerViewAdapter;
    Toolbar toolbar;
    Spinner spinner;

    private AnimatedVectorDrawableCompat plusToMinus, minusToPlus;
    private boolean isShowingPlus = true;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.change_theme:

                ThemeUtils.switchTheme(this, contextThemeWrapper);
                break;
        }

        return false;
    }

    @Override
    public void onResume() {
        super.onResume();

        MediaObserver.initContentObserver(MainActivity.this);

    }

    @Override
    public void onPause() {
        super.onPause();

        MediaObserver.removeContentObserver(MainActivity.this);
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        starterIntent = getIntent();

        contextThemeWrapper = new ContextThemeWrapper(getBaseContext(), this.getTheme());

        ThemeUtils.applyTheme(MainActivity.this, contextThemeWrapper);

        setContentView(R.layout.main_activity);

        initViews();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    PermissionUtils.askReadWriteAccess(MainActivity.this);

                }
            });

        } else {

            new LoadAlbumsAsync().execute();

        }

    }

    private void initViews() {

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        spinner = (Spinner) findViewById(R.id.grid_options);

        final ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.grid, android.R.layout.simple_spinner_dropdown_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.post(new Runnable() {
            @Override
            public void run() {

                spinner.setSelection(GridUtils.getSpinnerPosition(MainActivity.this), false);

                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                        int number = Integer.valueOf(parent.getItemAtPosition(position).toString().substring(0, 1));
                        GridUtils.setGridNumber(MainActivity.this, number, position);

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }

        });

        spinner.setAdapter(spinnerAdapter);

        setSupportActionBar(toolbar);

        fabPhotos = (FloatingActionButton) findViewById(R.id.fab_photos);
        fabVideos = (FloatingActionButton) findViewById(R.id.fab_videos);

        fabPlus = (FloatingActionButton) findViewById(R.id.fab_plus);

        plusToMinus = AnimatedVectorDrawableCompat.create(MainActivity.this, R.drawable.avd_pathmorph_plusminus_plus_to_minus);
        minusToPlus = AnimatedVectorDrawableCompat.create(MainActivity.this, R.drawable.avd_pathmorph_plusminus_minus_to_plus);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        sectionedRecyclerViewAdapter = new SectionedRecyclerViewAdapter();

        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.setAdapter(sectionedRecyclerViewAdapter);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                if (dy > 0 || dy < 0 && fabPhotos.isShown()) {
                    hideFabPlus(true);

                } else if (dy < 0) {

                    hideFabPlus(false);
                }
            }
        });

        initClicks();
    }


    private void clickFab(View v) {

        v.performClick();
    }

    private void hideFabPlus(final boolean hide) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (hide) {

                    if (fabPhotos.isShown() && fabVideos.isShown()) {
                        fabPlus.performClick();
                    }
                    fabPlus.hide();
                    fabPhotos.hide();
                    fabVideos.hide();


                } else {
                    fabPlus.show();
                }
            }
        });
    }

    private void toggleFab() {

        AnimatedVectorDrawableCompat currentDrawable = isShowingPlus ? plusToMinus : minusToPlus;
        fabPlus.setImageDrawable(currentDrawable);
        currentDrawable.start();

        if (isShowingPlus) {
            fabPhotos.show(new FloatingActionButton.OnVisibilityChangedListener() {
                @Override
                public void onShown(FloatingActionButton fab) {
                    fabVideos.show();
                    isShowingPlus = !isShowingPlus;
                }

            });
        } else {
            fabVideos.hide(new FloatingActionButton.OnVisibilityChangedListener() {
                @Override
                public void onHidden(FloatingActionButton fab) {
                    fabPhotos.hide();
                    isShowingPlus = true;
                }

            });
        }

    }

    private void initClicks() {

        fabPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                toggleFab();

            }
        });

        intentCamera.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS | Intent.FLAG_ACTIVITY_NO_ANIMATION);

        fabPhotos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                clickedFab = v;

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                    PermissionUtils.askCameraAccess(MainActivity.this, intentCamera);
                } else {
                    startActivity(intentCamera);
                }
            }

        });

        intentVideo.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS | Intent.FLAG_ACTIVITY_NO_ANIMATION);

        fabVideos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                clickedFab = v;

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    PermissionUtils.askCameraAccess(MainActivity.this, intentVideo);
                } else {
                    startActivity(intentVideo);

                }
            }
        });

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case 0: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {


                    new LoadAlbumsAsync().execute();

                } else {

                    PermissionUtils.rationaleDialog(MainActivity.this, getString(R.string.rationale_read), getString(R.string.writeReadContent), 0, null);
                }
            }

            break;

            case 1: {
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {

                    PermissionUtils.rationaleDialog(MainActivity.this, getString(R.string.rationale_camera), getString(R.string.cameraContent), 1, null);

                } else {

                    clickFab(clickedFab);

                }
            }
            break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 2:
                if (resultCode == Activity.RESULT_OK) {

                    SDCardUtils.saveSDCardUri(this, String.valueOf(data.getData()));

                    Toast.makeText(this, getString(R.string.success), Toast.LENGTH_SHORT)
                            .show();

                }
                break;
        }
    }

    private class LoadAlbumsAsync extends AsyncTask<Void, Void, Void> {

        protected void onPreExecute() {

        }

        @Override
        protected Void doInBackground(Void... params) {

            albumsList = AlbumsUtils.getAllAlbums(MainActivity.this);

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            super.onPostExecute(result);

            AlbumsUtils.setupAlbums(MainActivity.this, recyclerView, albumsList, sectionedRecyclerViewAdapter);

        }
    }
}

