package com.emadabel.bakingapp;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.emadabel.bakingapp.adapter.RecipeStepsAdapter;
import com.emadabel.bakingapp.model.Recipe;

public class RecipeDetailFragment extends Fragment {

    private static final String RECIPE_DETAIL = "recipe_detail";
    OnListClickListener mCallback;

    private Recipe mRecipe;

    public RecipeDetailFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (savedInstanceState != null) {
            mRecipe = savedInstanceState.getParcelable(RECIPE_DETAIL);
        }

        final View rootView = inflater.inflate(R.layout.fragment_recipe_detail, container, false);

        RecyclerView recyclerView = rootView.findViewById(R.id.recipe_details_rv);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

        RecipeStepsAdapter adapter = new RecipeStepsAdapter(mRecipe.getSteps(),
                new RecipeStepsAdapter.MasterListOnClickHandler() {
                    @Override
                    public void onClick(int position) {
                        mCallback.onTopicSelected(position);
                    }
                });

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        return rootView;
    }

    public void setMasterList(Recipe recipe) {
        this.mRecipe = recipe;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mCallback = (OnListClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnListClickListener");
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(RECIPE_DETAIL, mRecipe);
    }

    public interface OnListClickListener {
        void onTopicSelected(int position);
    }
}
