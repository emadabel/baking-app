package com.emadabel.bakingapp;

import android.content.Context;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.emadabel.bakingapp.model.Ingredient;

import java.util.List;

/**
 * Created by lenovo on 20/05/2018.
 */

public class ListRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    Context mContext;
    List<Ingredient> ingredientList;

    public ListRemoteViewsFactory(Context context) {
        mContext = context;
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
        return 0;
    }

    @Override
    public RemoteViews getViewAt(int position) {
        return null;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 0;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
