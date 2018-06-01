package com.emadabel.bakingapp.utils;

import com.emadabel.bakingapp.R;

public class Utilities {

    private static final String[] RECIPES_NAME = {
            "Nutella Pie",
            "Brownies",
            "Yellow Cake",
            "Cheesecake"
    };

    public static int getPosterResources(String name) {
        if (name.equalsIgnoreCase(RECIPES_NAME[0])) {
            return R.drawable.nutella_pie;
        } else if (name.equalsIgnoreCase(RECIPES_NAME[1])) {
            return R.drawable.brownies;
        } else if (name.equalsIgnoreCase(RECIPES_NAME[2])) {
            return R.drawable.yellow_cake;
        } else if (name.equalsIgnoreCase(RECIPES_NAME[3])) {
            return R.drawable.cheesecake;
        } else {
            return 0;
        }
    }
}
