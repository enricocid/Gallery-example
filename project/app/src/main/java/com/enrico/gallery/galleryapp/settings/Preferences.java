package com.enrico.gallery.galleryapp.settings;

import android.app.Activity;
import android.content.Context;
import android.preference.PreferenceManager;
import android.support.v7.view.ContextThemeWrapper;

import com.enrico.gallery.galleryapp.R;

public class Preferences {

    private static int resolveTheme(Context context) {

        int light = R.style.Base_Theme;
        int dark = R.style.Base_Theme_Dark;
        int red = R.style.Base_Theme_Red;
        int nightRed = R.style.Base_Theme_Dark_Red;
        int pink = R.style.Base_Theme_Pink;
        int nightPink = R.style.Base_Theme_Dark_Pink;
        int purple = R.style.Base_Theme_Purple;
        int nightPurple = R.style.Base_Theme_Dark_Purple;
        int deepPurple = R.style.Base_Theme_DeepPurple;
        int nightDeepPurple = R.style.Base_Theme_Dark_DeepPurple;
        int indigo = R.style.Base_Theme_Indigo;
        int nightIndigo = R.style.Base_Theme_Dark_Indigo;
        int blue = R.style.Base_Theme_Blue;
        int nightBlue = R.style.Base_Theme_Dark_Blue;
        int lightBlue = R.style.Base_Theme_LightBlue;
        int nightLightBlue = R.style.Base_Theme_Dark_LightBlue;
        int cyan = R.style.Base_Theme_Cyan;
        int nightCyan = R.style.Base_Theme_Dark_Cyan;
        int teal = R.style.Base_Theme_Teal;
        int nightTeal = R.style.Base_Theme_Dark_Teal;
        int green = R.style.Base_Theme_Green;
        int nightGreen = R.style.Base_Theme_Dark_Green;
        int lightGreen = R.style.Base_Theme_LightGreen;
        int nightLightGreen = R.style.Base_Theme_Dark_LightGreen;
        int lime = R.style.Base_Theme_Lime;
        int nightLime = R.style.Base_Theme_Dark_Lime;
        int yellow = R.style.Base_Theme_Yellow;
        int nightYellow = R.style.Base_Theme_Dark_Yellow;
        int amber = R.style.Base_Theme_Amber;
        int nightAmber = R.style.Base_Theme_Dark_Amber;
        int orange = R.style.Base_Theme_Orange;
        int nightOrange = R.style.Base_Theme_Dark_Orange;
        int deepOrange = R.style.Base_Theme_DeepOrange;
        int nightDeepOrange = R.style.Base_Theme_Dark_DeepOrange;
        int brown = R.style.Base_Theme_Brown;
        int nightBrown = R.style.Base_Theme_Dark_Brown;
        int blueGrey = R.style.Base_Theme_BlueGrey;
        int nightBlueGrey = R.style.Base_Theme_Dark_BlueGrey;

        String choice = PreferenceManager.getDefaultSharedPreferences(context)
                .getString(context.getString(R.string.pref_theme), String.valueOf(0));

        switch (Integer.parseInt(choice)) {
            default:
            case 0:
                return light;
            case 1:
                return dark;
            case 2:
                return red;
            case 3:
                return nightRed;
            case 4:
                return pink;
            case 5:
                return nightPink;
            case 6:
                return purple;
            case 7:
                return nightPurple;
            case 8:
                return deepPurple;
            case 9:
                return nightDeepPurple;
            case 10:
                return indigo;
            case 11:
                return nightIndigo;
            case 12:
                return blue;
            case 13:
                return nightBlue;
            case 14:
                return lightBlue;
            case 15:
                return nightLightBlue;
            case 16:
                return cyan;
            case 17:
                return nightCyan;
            case 18:
                return teal;
            case 19:
                return nightTeal;
            case 20:
                return green;
            case 21:
                return nightGreen;
            case 22:
                return lightGreen;
            case 23:
                return nightLightGreen;
            case 24:
                return lime;
            case 25:
                return nightLime;
            case 26:
                return yellow;
            case 27:
                return nightYellow;
            case 28:
                return amber;
            case 29:
                return nightAmber;
            case 30:
                return orange;
            case 31:
                return nightOrange;
            case 32:
                return deepOrange;
            case 33:
                return nightDeepOrange;
            case 34:
                return brown;
            case 35:
                return nightBrown;
            case 36:
                return blueGrey;
            case 37:
                return nightBlueGrey;
        }
    }

    public static int resolveGrid(Activity activity) {

        int two = 2;
        int three = 3;
        int four = 4;

        String choice = PreferenceManager.getDefaultSharedPreferences(activity)
                .getString(activity.getString(R.string.pref_grid), String.valueOf(40));
        switch (Integer.parseInt(choice)) {
            case 38:
                return two;
            default:
            case 39:
                return three;
            case 40:
                return four;

        }
    }

    public static void applyTheme(ContextThemeWrapper contextThemeWrapper, Context context) {
        int theme = Preferences.resolveTheme(context);
        contextThemeWrapper.setTheme(theme);
    }
}
