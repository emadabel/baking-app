package com.emadabel.bakingapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.emadabel.bakingapp.adapter.RecipesAdapter;
import com.emadabel.bakingapp.api.RecipeAPI;
import com.emadabel.bakingapp.model.Recipe;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private static final String RECIPE_LIST_KEY = "recipe_list_key";
    private static final String BASE_URL = "https://d17h27t6h515a5.cloudfront.net/";

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
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        recipesAdapter = new RecipesAdapter();
        recipesAdapter.setOnViewHolderClickListener(new RecipesAdapter.OnViewHolderClickListener() {
            @Override
            public void onClick(Recipe recipe) {
                Toast.makeText(MainActivity.this, recipe.getName(), Toast.LENGTH_SHORT).show();
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        mRecipeListRecyclerView.setLayoutManager(layoutManager);
        mRecipeListRecyclerView.setHasFixedSize(true);
        mRecipeListRecyclerView.setAdapter(recipesAdapter);

        if (savedInstanceState != null) {
            recipeList = savedInstanceState.getParcelableArrayList(RECIPE_LIST_KEY);
        }

        start();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (recipeList != null) {
            outState.putParcelableArrayList(RECIPE_LIST_KEY, recipeList);
        }
    }

    public void start() {
        if (recipeList != null) {
            recipesAdapter.setRecipesData(recipeList);
            return;
        } else {
            mLoadingIndicatorPb.setVisibility(View.VISIBLE);
            mRecipeListRecyclerView.setVisibility(View.INVISIBLE);
        }

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        RecipeAPI recipeAPI = retrofit.create(RecipeAPI.class);

        Call<List<Recipe>> call = recipeAPI.getRecipes();
        call.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                if (response.isSuccessful()) {
                    List<Recipe> recipes = response.body();
                    recipeList = new ArrayList<>(recipes);
                    recipesAdapter.setRecipesData(recipeList);
                    showRecipesData();
                } else {
                    showErrorMessage();
                    Log.e(this.getClass().getSimpleName(), response.errorBody().toString());
                }
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                showErrorMessage();
                t.printStackTrace();
            }
        });
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
}
