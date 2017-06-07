package com.enrico.gallery.galleryapp.utils;

import android.graphics.Color;

import java.util.Random;

public class RandomMaterialColor {

    private static int[] items = new int[]{Color.rgb(244, 67, 54),
            Color.rgb(244, 67, 54),
            Color.rgb(233, 30, 99),
            Color.rgb(156, 39, 176),
            Color.rgb(103, 58, 183),
            Color.rgb(63, 81, 181),
            Color.rgb(33, 150, 243),
            Color.rgb(3, 169, 244),
            Color.rgb(0, 188, 212),
            Color.rgb(0, 150, 136),
            Color.rgb(76, 175, 80),
            Color.rgb(139, 195, 74),
            Color.rgb(255, 193, 7),
            Color.rgb(255, 152, 0),
            Color.rgb(255, 87, 34),
            Color.rgb(121, 85, 72)

    };

    private static Random rand = new Random();

    public static int get() {
        return items[rand.nextInt(items.length)];
    }
}
