package com.emadabel.bakingapp;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.emadabel.bakingapp.model.Ingredient;
import com.emadabel.bakingapp.model.Recipe;
import com.emadabel.bakingapp.model.Step;

import java.util.List;

public class DetailsActivity extends AppCompatActivity implements RecipeDetailFragment.OnListClickListener {

    static final String RECIPE_DETAIL = "recipe_detail";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        if (savedInstanceState == null) {

            Bundle b = getIntent().getExtras();
            Recipe recipe = new Recipe();
            if (b != null)
            recipe = b.getParcelable(RECIPE_DETAIL);

            /*Step step = recipe.getSteps().get(0);

            StepDetailFragment stepDetailFragment = new StepDetailFragment();
            stepDetailFragment.setVideoUrl(step.getVideoURL());
            FragmentManager fragmentManager = getSupportFragmentManager();

            fragmentManager.beginTransaction()
                    .add(R.id.step_detail_container, stepDetailFragment)
                    .commit();*/

            RecipeDetailFragment recipeDetailFragment = new RecipeDetailFragment();
            recipeDetailFragment.setMasterList(recipe);
            FragmentManager fragmentManager = getSupportFragmentManager();

            fragmentManager.beginTransaction()
                    .add(R.id.recipe_detail_container, recipeDetailFragment)
                    .commit();
        }
    }

    @Override
    public void onTopicSelected(List<Ingredient> ingredients, Step step) {
        if (ingredients == null) {
            Toast.makeText(this, "Step clicked", Toast.LENGTH_SHORT).show();
        } else if (step == null) {
            Toast.makeText(this, "Ingredients clicked", Toast.LENGTH_SHORT).show();
        }
    }
}
