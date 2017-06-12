package com.enrico.gallery.galleryapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.widget.Toast;

import com.enrico.gallery.galleryapp.settings.Preferences;
import com.enrico.gallery.galleryapp.utils.SDCardUtils;

public class MediaActivity extends AppCompatActivity {

    int pos;

    String[] mUrls;

    ContextThemeWrapper contextThemeWrapper;
    ViewPager pager;
    PagerAdapter mPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        mUrls = getIntent().getExtras().getStringArray("urls");

        pos = getIntent().getExtras().getInt("pos");

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                contextThemeWrapper = new ContextThemeWrapper(getBaseContext(), MediaActivity.this.getTheme());

                Preferences.applyTheme(contextThemeWrapper, getBaseContext());

                ImmersiveMode.On(MediaActivity.this);

            }
        });

        setContentView(R.layout.media_activity);

        mPagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
        pager = (ViewPager) findViewById(R.id.pager);

        pager.setAdapter(mPagerAdapter);
        pager.setCurrentItem(pos);
        pager.setClipToPadding(false);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            ImmersiveMode.On(this);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        MediaObserver.initContentObserver(MediaActivity.this);

    }

    @Override
    public void onPause() {
        super.onPause();

        MediaObserver.removeContentObserver(MediaActivity.this);
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

    private class MyPagerAdapter extends FragmentStatePagerAdapter {

        MyPagerAdapter(FragmentManager fm) {

            super(fm);

        }

        @Override
        public Fragment getItem(int position) {

            MediaPagerFragment frag = new MediaPagerFragment();

            Bundle bundle = new Bundle();

            bundle.putStringArray("urls", mUrls);
            bundle.putInt("pos", position);
            frag.setArguments(bundle);

            return frag;
        }

        @Override
        public int getCount() {
            return mUrls.length;
        }

        @Override
        public Parcelable saveState() {
            Bundle bundle = (Bundle) super.saveState();
            bundle.putParcelableArray("states", null);
            return bundle;
        }
    }
}


