package com.emadabel.bakingapp.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.emadabel.bakingapp.R;
import com.emadabel.bakingapp.model.Ingredient;
import com.emadabel.bakingapp.model.Step;

import java.util.ArrayList;
import java.util.List;

public class MasterListAdapter extends RecyclerView.Adapter<MasterListAdapter.RecipeStepsViewHolder> {

    private MasterListOnClickHandler mClickHandler;

    private List<String> masterList;
    private List<Step> mSteps;
    private List<Ingredient> mIngredients;

    public MasterListAdapter(List<Step> recipeStepList, List<Ingredient> ingredients, MasterListOnClickHandler clickHandler) {
        this.mClickHandler = clickHandler;
        this.mSteps = recipeStepList;
        this.mIngredients = ingredients;
        masterList = new ArrayList<>(recipeStepList.size() + 1);
        masterList.add("Recipe Ingredients");
        for (int i = 0; i < recipeStepList.size(); i++) {
            masterList.add(recipeStepList.get(i).getShortDescription());
        }
    }

    @NonNull
    @Override
    public RecipeStepsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View view = inflater.inflate(R.layout.recipe_steps_list, parent, false);
        return new RecipeStepsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeStepsViewHolder holder, int position) {
        String title = masterList.get(position);

        holder.mRecipeStepDescriptionTextView.setText(title);
    }

    @Override
    public int getItemCount() {
        if (masterList == null)return 0;
        return masterList.size();
    }

    public interface MasterListOnClickHandler {
        void onClick(Step step);
        void onClick(List<Ingredient> ingredients);
    }

    public class RecipeStepsViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        final TextView mRecipeStepDescriptionTextView;

        public RecipeStepsViewHolder(View itemView) {
            super(itemView);
            mRecipeStepDescriptionTextView = itemView.findViewById(R.id.recipe_step_description_tv);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            if (adapterPosition == 0) {
                mClickHandler.onClick(mIngredients);
            } else {
                mClickHandler.onClick(mSteps.get(adapterPosition - 1));
            }
        }
    }
}
