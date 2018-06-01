package com.emadabel.bakingapp;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.emadabel.bakingapp.model.Ingredient;
import com.emadabel.bakingapp.model.Recipe;

/**
 * Implementation of App Widget functionality.
 */
public class RecipeWidgetProvider extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                Recipe recipe, int appWidgetId) {

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipe_widget_provider);

        if (recipe != null) {
            Intent intent = new Intent(context, RecipeDetailActivity.class);
            intent.putExtra(RecipeDetailActivity.RECIPE_DETAIL, recipe);

            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            views.setTextViewText(R.id.appwidget_title_tv, recipe.getName());

            StringBuilder ingredients = new StringBuilder();
            for (Ingredient ingredient : recipe.getIngredients()) {
                ingredients.append(ingredient.getIngredient())
                        .append(": ")
                        .append(ingredient.getQuantity())
                        .append(" ")
                        .append(ingredient.getMeasure())
                        .append("\n");
            }

            views.setTextViewText(R.id.appwidget_ingredients_tv, ingredients);

            views.setOnClickPendingIntent(R.id.widget_container, pendingIntent);
        } else {
            Intent intent = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

            views.setTextViewText(R.id.appwidget_title_tv, context.getString(R.string.no_favorite_widget));

            views.setOnClickPendingIntent(R.id.widget_container, pendingIntent);
        }

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    public static void updateRecipeWidgets(Context context, AppWidgetManager appWidgetManager,
                                           Recipe recipe, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, recipe, appWidgetId);
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        UpdatingWidgetService.startActionUpdateWidgets(context);
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

