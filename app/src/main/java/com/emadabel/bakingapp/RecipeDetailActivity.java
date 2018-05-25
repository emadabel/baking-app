package com.emadabel.bakingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.emadabel.bakingapp.adapter.IngredientAdapter;
import com.emadabel.bakingapp.model.Recipe;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeDetailActivity extends AppCompatActivity {

    static final String RECIPE_DETAIL = "recipe_detail";

    @BindView(R.id.ingredients_rv)
    RecyclerView mIngredientsRecyclerView;

    @BindView(R.id.goto_steps_button)
    Button mGotoStepsButton;

    private Recipe mRecipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
        ButterKnife.bind(this);

        if (getIntent().getExtras() != null)
            mRecipe = getIntent().getExtras().getParcelable(RECIPE_DETAIL);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            if (mRecipe != null) getSupportActionBar().setTitle(mRecipe.getName());
        }

        IngredientAdapter ingredientAdapter = new IngredientAdapter(this, mRecipe.getIngredients());

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        mIngredientsRecyclerView.setLayoutManager(layoutManager);
        mIngredientsRecyclerView.setHasFixedSize(true);
        mIngredientsRecyclerView.setAdapter(ingredientAdapter);

        mGotoStepsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle b = new Bundle();
                b.putParcelable(RECIPE_DETAIL, mRecipe);
                Intent intent = new Intent(RecipeDetailActivity.this, StepDetailActivity.class);
                intent.putExtras(b);
                startActivity(intent);
            }
        });
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
}
