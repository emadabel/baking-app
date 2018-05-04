package com.emadabel.bakingapp.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.emadabel.bakingapp.R;
import com.emadabel.bakingapp.model.Recipe;
import com.emadabel.bakingapp.utils.Utilities;

import java.util.List;

/**
 * Created by Emad on 04/05/2018.
 */

public class RecipesAdapter extends RecyclerView.Adapter<RecipesAdapter.RecipesViewHolder> {

    private OnViewHolderClickListener mViewHolderClickListener;
    private List<Recipe> mRecipeList;

    public RecipesAdapter() {
    }

    @NonNull
    @Override
    public RecipesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View view = inflater.inflate(R.layout.recipe_list, parent, false);
        return new RecipesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipesViewHolder holder, int position) {
        String name = mRecipeList.get(position).getName();
        String servings = Integer.toString(mRecipeList.get(position).getServings());
        int posterId = Utilities.getPosterResources(name);

        holder.mRecipeTitleTextView.setText(name);
        holder.mServingsTextView.setText(servings);
        holder.mRecipePosterImageView.setImageResource(posterId);
    }

    public void setRecipesData(List<Recipe> recipeList) {
        mRecipeList = recipeList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (mRecipeList == null) return 0;
        return mRecipeList.size();
    }

    public void setOnViewHolderClickListener(@Nullable OnViewHolderClickListener listener) {
        this.mViewHolderClickListener = listener;
    }

    public interface OnViewHolderClickListener {
        void onClick(Recipe recipe);
    }

    public class RecipesViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        final ImageView mRecipePosterImageView;
        final TextView mRecipeTitleTextView;
        final TextView mServingsTextView;

        RecipesViewHolder(View itemView) {
            super(itemView);
            mRecipePosterImageView = itemView.findViewById(R.id.recipe_poster_iv);
            mRecipeTitleTextView = itemView.findViewById(R.id.recipe_title_tv);
            mServingsTextView = itemView.findViewById(R.id.servings_tv);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            Recipe recipe = mRecipeList.get(adapterPosition);
            mViewHolderClickListener.onClick(recipe);
        }
    }
}
