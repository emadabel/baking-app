package com.emadabel.bakingapp;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;

import com.emadabel.bakingapp.model.Recipe;
import com.google.gson.Gson;

public class UpdatingWidgetService extends IntentService {

    public static final String ACTION_UPDATE_ALL_WIDGETS = "com.emadabel.bakingapp.action.update_widgets";

    public UpdatingWidgetService() {
        super("UpdatingWidgetService");
    }

    public static void startActionUpdateWidgets(Context context) {
        Intent intent = new Intent(context, UpdatingWidgetService.class);
        intent.setAction(ACTION_UPDATE_ALL_WIDGETS);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_UPDATE_ALL_WIDGETS.equals(action)) {
                SharedPreferences prefs = PreferenceManager
                        .getDefaultSharedPreferences(this);
                String recipeKey = getString(R.string.pref_widget_recipe);
                String recipeJson = prefs.getString(recipeKey, null);

                Recipe recipe = new Gson().fromJson(recipeJson, Recipe.class);

                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
                int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, RecipeWidgetProvider.class));
                RecipeWidgetProvider.updateRecipeWidgets(this, appWidgetManager, recipe, appWidgetIds);
            }
        }
    }
}
