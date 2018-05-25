package com.emadabel.bakingapp;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.emadabel.bakingapp.adapter.RecipesAdapter;
import com.emadabel.bakingapp.api.RecipesDownloader;
import com.emadabel.bakingapp.model.Recipe;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ConfigurableWidgetConfigureActivity extends AppCompatActivity implements RecipesDownloader.DelayerCallback {

    private static final String RECIPE_LIST_KEY = "recipe_list_key";

    int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;

    private AppWidgetManager widgetManager;
    private RemoteViews views;

    @BindView(R.id.recipe_list_rv)
    RecyclerView mRecipeListRecyclerView;

    @BindView(R.id.loading_indicator_pb)
    ProgressBar mLoadingIndicatorPb;

    @BindView(R.id.error_message_tv)
    TextView mErrorMessageTv;

    RecipesAdapter recipesAdapter;
    ArrayList<Recipe> recipeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_widget_configure);
        ButterKnife.bind(this);

        recipesAdapter = new RecipesAdapter();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        mRecipeListRecyclerView.setLayoutManager(layoutManager);
        mRecipeListRecyclerView.setHasFixedSize(true);
        mRecipeListRecyclerView.setAdapter(recipesAdapter);

        widgetManager = AppWidgetManager.getInstance(this);
        views = new RemoteViews(this.getPackageName(), R.layout.recipe_widget_provider);
        // Find the widget id from the intent.
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }
        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
            return;
        }

        recipesAdapter.setOnViewHolderClickListener(new RecipesAdapter.OnViewHolderClickListener() {
            @Override
            public void onClick(Recipe recipe) {
                Bundle b = new Bundle();
                b.putParcelable(RecipeDetailActivity.RECIPE_DETAIL, recipe);
                Intent intent = new Intent(ConfigurableWidgetConfigureActivity.this, RecipeDetailActivity.class);
                intent.putExtras(b);

                PendingIntent pendingIntent = PendingIntent.getActivity(ConfigurableWidgetConfigureActivity.this, 0, intent, 0);

                views.setOnClickPendingIntent(R.id.appwidget_title_tv, pendingIntent);

                widgetManager.updateAppWidget(mAppWidgetId, views);
                Intent resultValue = new Intent();
                // Set the results as expected from a 'configure activity'.
                resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
                setResult(RESULT_OK, resultValue);
                finish();
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (recipeList != null) {
            outState.putParcelableArrayList(RECIPE_LIST_KEY, recipeList);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (recipeList != null) {
            recipesAdapter.setRecipesData(recipeList);
            return;
        } else {
            mLoadingIndicatorPb.setVisibility(View.VISIBLE);
            mRecipeListRecyclerView.setVisibility(View.INVISIBLE);
        }
        RecipesDownloader.downloadRecipes(this, ConfigurableWidgetConfigureActivity.this, null);
    }

    private void showRecipesData() {
        mRecipeListRecyclerView.setVisibility(View.VISIBLE);
        mErrorMessageTv.setVisibility(View.INVISIBLE);
        mLoadingIndicatorPb.setVisibility(View.INVISIBLE);
    }

    private void showErrorMessage() {
        mLoadingIndicatorPb.setVisibility(View.INVISIBLE);
        mRecipeListRecyclerView.setVisibility(View.INVISIBLE);
        mErrorMessageTv.setVisibility(View.VISIBLE);
    }

    @Override
    public void onSuccess(ArrayList<Recipe> recipes) {
        recipeList = recipes;
        recipesAdapter.setRecipesData(recipeList);
        showRecipesData();
    }

    @Override
    public void onFailure() {
        showErrorMessage();
    }
}
