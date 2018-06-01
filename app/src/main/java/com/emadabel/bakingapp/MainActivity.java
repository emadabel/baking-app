package com.emadabel.bakingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.emadabel.bakingapp.IdlingResource.SimpleIdlingResource;
import com.emadabel.bakingapp.adapter.RecipesAdapter;
import com.emadabel.bakingapp.api.RecipesDownloader;
import com.emadabel.bakingapp.model.Recipe;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements RecipesDownloader.DelayerCallback {

    private static final String RECIPE_LIST_KEY = "recipe_list_key";

    @BindView(R.id.recipe_list_rv)
    RecyclerView mRecipeListRecyclerView;

    @BindView(R.id.loading_indicator_pb)
    ProgressBar mLoadingIndicatorPb;

    @BindView(R.id.error_message_tv)
    TextView mErrorMessageTv;

    RecipesAdapter recipesAdapter;
    ArrayList<Recipe> recipeList;

    @Nullable
    private SimpleIdlingResource mIdlingResource;

    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new SimpleIdlingResource();
        }
        return mIdlingResource;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        recipesAdapter = new RecipesAdapter();
        recipesAdapter.setOnViewHolderClickListener(new RecipesAdapter.OnViewHolderClickListener() {
            @Override
            public void onClick(Recipe recipe) {
                Intent intent = new Intent(MainActivity.this, RecipeDetailActivity.class);
                intent.putExtra(RecipeDetailActivity.RECIPE_DETAIL, recipe);
                startActivity(intent);
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        mRecipeListRecyclerView.setLayoutManager(layoutManager);
        mRecipeListRecyclerView.setHasFixedSize(true);
        mRecipeListRecyclerView.setAdapter(recipesAdapter);

        if (savedInstanceState != null) {
            recipeList = savedInstanceState.getParcelableArrayList(RECIPE_LIST_KEY);
        }

        getIdlingResource();
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
        RecipesDownloader.downloadRecipes(this, MainActivity.this, mIdlingResource);
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
