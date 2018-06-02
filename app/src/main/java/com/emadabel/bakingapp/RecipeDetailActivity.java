package com.emadabel.bakingapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.emadabel.bakingapp.adapter.IngredientAdapter;
import com.emadabel.bakingapp.model.Recipe;
import com.emadabel.bakingapp.widget.UpdatingWidgetService;
import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeDetailActivity extends AppCompatActivity {

    public static final String RECIPE_DETAIL = "recipe_detail";

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

        mRecipe = getIntent().getParcelableExtra(RECIPE_DETAIL);

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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_widget_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }

        if (id == R.id.action_add_to_widget) {
            Gson gson = new Gson();
            SharedPreferences prefs = PreferenceManager
                    .getDefaultSharedPreferences(RecipeDetailActivity.this);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString(getString(R.string.pref_widget_recipe), gson.toJson(mRecipe));
            editor.apply();

            UpdatingWidgetService.startActionUpdateWidgets(this);

            Toast.makeText(this, getString(R.string.add_widget_message), Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
