package com.enrico.gallery.galleryapp;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class GridUtils {

    private static void saveSpinnerPosition(Activity activity, int position) {
        SharedPreferences preferencePos;
        preferencePos = activity.getSharedPreferences("spinner", Context.MODE_PRIVATE);


        preferencePos.edit()
                .clear()
                .apply();

        preferencePos.edit()
                .putInt("selectedPosition", position)
                .apply();
    }

    static void setGridNumber(Activity activity, int number, int position) {

        SharedPreferences preferenceGrid;
        preferenceGrid = activity.getSharedPreferences("grid", Context.MODE_PRIVATE);


        preferenceGrid.edit()
                .clear()
                .apply();

        preferenceGrid.edit()
                .putInt("selectedGrid", number)
                .apply();

        saveSpinnerPosition(activity, position);

        activity.finish();
        activity.startActivity(MainActivity.starterIntent);
        activity.overridePendingTransition(0, 0);

    }

    public static int getGridNumber(Activity activity) {

        SharedPreferences preferenceGrid = activity.getSharedPreferences("grid", Context.MODE_PRIVATE);

        return preferenceGrid.getInt("selectedGrid", 4);
    }

    static int getSpinnerPosition(Activity activity) {

        SharedPreferences preferenceGrid = activity.getSharedPreferences("spinner", Context.MODE_PRIVATE);

        return preferenceGrid.getInt("selectedPosition", 2);
    }
}
