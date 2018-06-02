package com.emadabel.bakingapp.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.emadabel.bakingapp.R;
import com.emadabel.bakingapp.model.Ingredient;
import com.emadabel.bakingapp.model.Recipe;
import com.google.gson.Gson;

import java.util.List;

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

    public static List<Ingredient> getDataFromSharedPrefs(Context context) {
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(context);
        String recipeKey = context.getString(R.string.pref_widget_recipe);
        String recipeJson = prefs.getString(recipeKey, null);

        Recipe recipe = new Gson().fromJson(recipeJson, Recipe.class);

        return recipe.getIngredients();
    }
}
