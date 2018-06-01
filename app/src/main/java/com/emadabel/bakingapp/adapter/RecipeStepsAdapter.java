package com.emadabel.bakingapp.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.emadabel.bakingapp.R;
import com.emadabel.bakingapp.model.Step;

import java.util.List;

public class RecipeStepsAdapter extends RecyclerView.Adapter<RecipeStepsAdapter.RecipeStepsViewHolder> {

    private MasterListOnClickHandler mClickHandler;

    private List<Step> mSteps;

    public RecipeStepsAdapter(List<Step> recipeStepList, MasterListOnClickHandler clickHandler) {
        this.mClickHandler = clickHandler;
        this.mSteps = recipeStepList;
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
        String title = mSteps.get(position).getShortDescription();

        holder.mRecipeStepTitleTextView.setText(title);
    }

    @Override
    public int getItemCount() {
        if (mSteps == null) return 0;
        return mSteps.size();
    }

    public interface MasterListOnClickHandler {
        void onClick(int position);
    }

    public class RecipeStepsViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        final TextView mRecipeStepTitleTextView;

        public RecipeStepsViewHolder(View itemView) {
            super(itemView);
            mRecipeStepTitleTextView = itemView.findViewById(R.id.recipe_step_title_tv);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            mClickHandler.onClick(adapterPosition);
        }
    }
}
