package com.enrico.gallery.galleryapp.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.view.ContextThemeWrapper;

import com.enrico.gallery.galleryapp.R;

public class ThemeUtils {

    public static void applyTheme(Activity activity, ContextThemeWrapper contextThemeWrapper) {

        int currentTheme = getTheme(activity);
        contextThemeWrapper.setTheme(currentTheme);

    }

    public static void switchTheme(Activity activity, ContextThemeWrapper contextThemeWrapper) {

        int currentTheme = getTheme(activity);

        switch (currentTheme) {

            case R.style.Base_Theme:
                contextThemeWrapper.setTheme(R.style.Base_Theme_Dark);
                setTheme(activity, R.style.Base_Theme_Dark);

                recreate(activity);

                break;

            case R.style.Base_Theme_Dark:
                contextThemeWrapper.setTheme(R.style.Base_Theme);
                setTheme(activity, R.style.Base_Theme);

                recreate(activity);

                break;
        }
    }

    private static void setTheme(Activity activity, int theme) {

        SharedPreferences preferenceTheme;
        preferenceTheme = activity.getSharedPreferences("theme", Context.MODE_PRIVATE);

        preferenceTheme.edit()
                .clear()
                .apply();

        preferenceTheme.edit()
                .putInt("currentTheme", theme)
                .apply();
    }

    private static int getTheme(Activity activity) {

        SharedPreferences preferenceTheme = activity.getSharedPreferences("theme", Context.MODE_PRIVATE);

        return preferenceTheme.getInt("currentTheme", R.style.Base_Theme);
    }

    private static void recreate(Activity activity) {

        activity.recreate();

    }
}
