package com.enrico.gallery.galleryapp.settings;

import android.app.Activity;
import android.content.Context;
import android.preference.PreferenceManager;
import android.support.v7.view.ContextThemeWrapper;

import com.enrico.gallery.galleryapp.R;

public class Preferences {

    public static int resolveGrid(Activity activity) {

        int two = 2;
        int three = 3;
        int four = 4;

        String choice = PreferenceManager.getDefaultSharedPreferences(activity)
                .getString(activity.getString(R.string.pref_grid), String.valueOf(4));
        switch (Integer.parseInt(choice)) {
            case 2:
                return two;
            default:
            case 3:
                return three;
            case 4:
                return four;

        }
    }

    private static int resolveTheme(Context context) {

        int light = R.style.Base_Theme;

        int dark = R.style.Base_Theme_Dark;

        String choice = PreferenceManager.getDefaultSharedPreferences(context)
                .getString(context.getString(R.string.pref_theme), String.valueOf(0));
        switch (Integer.parseInt(choice)) {
            default:
            case 0:
                return light;
            case 1:
                return dark;
        }
    }

    public static void applyTheme(ContextThemeWrapper contextThemeWrapper, Context context) {
        int theme = Preferences.resolveTheme(context);
        contextThemeWrapper.setTheme(theme);

    }
}
