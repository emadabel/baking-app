package com.emadabel.bakingapp.api;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.Log;

import com.emadabel.bakingapp.IdlingResource.SimpleIdlingResource;
import com.emadabel.bakingapp.model.Recipe;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RecipesDownloader {

    private static final String BASE_URL = "https://d17h27t6h515a5.cloudfront.net/";

    public static void downloadRecipes(Context context, final DelayerCallback callback,
                                       @Nullable final SimpleIdlingResource idlingResource) {

        if (idlingResource != null) {
            idlingResource.setIdleState(false);
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
                    ArrayList<Recipe> recipes = new ArrayList<>(response.body());
                    if (callback != null) {
                        callback.onSuccess(recipes);
                        if (idlingResource != null) {
                            idlingResource.setIdleState(true);
                        }
                    }
                } else {
                    if (callback != null) {
                        callback.onFailure();
                        if (idlingResource != null) {
                            idlingResource.setIdleState(true);
                        }
                    }
                    Log.e(this.getClass().getSimpleName(), response.errorBody().toString());
                }
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                if (callback != null) {
                    callback.onFailure();
                    if (idlingResource != null) {
                        idlingResource.setIdleState(true);
                    }
                }
                t.printStackTrace();
            }
        });
    }

    public interface DelayerCallback {
        void onSuccess(ArrayList<Recipe> recipes);

        void onFailure();
    }
}
