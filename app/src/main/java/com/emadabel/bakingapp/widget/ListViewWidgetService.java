package com.emadabel.bakingapp.widget;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.emadabel.bakingapp.R;
import com.emadabel.bakingapp.model.Ingredient;
import com.emadabel.bakingapp.utils.Utilities;

import java.util.List;

/*
code sourece:
https://medium.com/@puruchauhan/android-widget-for-starters-5db14f23009b
https://github.com/tashariko/widget_sample/tree/step_5-widget_list_view
 */
public class ListViewWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new AppWidgetListView(this.getApplicationContext(), Utilities.getDataFromSharedPrefs(getApplicationContext()));
    }
}

class AppWidgetListView implements RemoteViewsService.RemoteViewsFactory {

    private List<Ingredient> ingredientList;
    private Context mContext;

    public AppWidgetListView(Context context, List<Ingredient> ingredients) {
        this.ingredientList = ingredients;
        this.mContext = context;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return ingredientList.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.widget_list_item);

        views.setTextViewText(R.id.ingredient_title_tv, ingredientList.get(position).getIngredient());
        views.setTextViewText(R.id.ingredient_subTitle_tv, mContext.getString(R.string.ingredient_detail,
                ingredientList.get(position).getQuantity(), ingredientList.get(position).getMeasure()));

        return views;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}