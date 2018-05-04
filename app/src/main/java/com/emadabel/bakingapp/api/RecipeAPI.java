package com.emadabel.bakingapp.api;

import com.emadabel.bakingapp.model.Recipe;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Emad on 04/05/2018.
 */

public interface RecipeAPI {

    @GET("/topher/2017/May/59121517_baking/baking.json")
    Call<List<Recipe>> getRecipes();
}
