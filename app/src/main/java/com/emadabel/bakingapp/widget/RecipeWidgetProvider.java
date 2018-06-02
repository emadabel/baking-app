package com.emadabel.bakingapp.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.TaskStackBuilder;
import android.widget.RemoteViews;

import com.emadabel.bakingapp.MainActivity;
import com.emadabel.bakingapp.R;
import com.emadabel.bakingapp.RecipeDetailActivity;
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
            Intent appIntent = new Intent(context, RecipeDetailActivity.class);
            appIntent.putExtra(RecipeDetailActivity.RECIPE_DETAIL, recipe);

            PendingIntent pendingIntent =
                    TaskStackBuilder.create(context)
                            // add all of DetailsActivity's parents to the stack,
                            // followed by DetailsActivity itself
                            .addNextIntentWithParentStack(appIntent)
                            .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

            views.setTextViewText(R.id.appwidget_title_tv, recipe.getName());

            Intent intent = new Intent(context, ListViewWidgetService.class);
            views.setRemoteAdapter(R.id.appwidget_ingredients_lv, intent);

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

