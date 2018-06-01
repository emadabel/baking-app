package com.emadabel.bakingapp;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.emadabel.bakingapp.model.Recipe;
import com.emadabel.bakingapp.model.Step;

public class StepDetailActivity extends AppCompatActivity implements
        StepDetailFragment.OnStepChangedListener, RecipeDetailFragment.OnListClickListener {

    private boolean mTwoPane;
    private Recipe mRecipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_detail);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        if (getIntent().getExtras() != null)
            mRecipe = getIntent().getExtras().getParcelable(RecipeDetailActivity.RECIPE_DETAIL);

        Step step = mRecipe.getSteps().get(0);
        getSupportActionBar().setTitle(step.getShortDescription());

        FragmentManager fragmentManager = getSupportFragmentManager();

        if (savedInstanceState == null) {
            StepDetailFragment stepDetailFragment = new StepDetailFragment();
            stepDetailFragment.setStepList(mRecipe.getSteps());
            stepDetailFragment.setListIndex(0);

            fragmentManager.beginTransaction()
                    .add(R.id.step_detail_container, stepDetailFragment)
                    .commit();
        }

        if (findViewById(R.id.recipe_detail_container) != null) {
            mTwoPane = true;

            RecipeDetailFragment recipeDetailFragment = new RecipeDetailFragment();
            recipeDetailFragment.setMasterList(mRecipe);

            fragmentManager.beginTransaction()
                    .replace(R.id.recipe_detail_container, recipeDetailFragment)
                    .commit();
        } else {
            mTwoPane = false;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home)
            onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void updateActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    @Override
    public void onTopicSelected(int position) {
        if (mTwoPane) {
            StepDetailFragment stepDetailFragment = new StepDetailFragment();
            stepDetailFragment.setStepList(mRecipe.getSteps());
            stepDetailFragment.setListIndex(position);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.step_detail_container, stepDetailFragment)
                    .commit();
        }
    }
}
