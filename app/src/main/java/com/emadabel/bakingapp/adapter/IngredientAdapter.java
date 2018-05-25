package com.emadabel.bakingapp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.emadabel.bakingapp.R;
import com.emadabel.bakingapp.model.Ingredient;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.IngredientsViewHolder> {

    private Context mContext;
    private List<Ingredient> mIngredients;

    public IngredientAdapter(Context context, List<Ingredient> ingredients) {
        this.mContext = context;
        this.mIngredients = ingredients;
    }

    @NonNull
    @Override
    public IngredientsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View view = inflater.inflate(R.layout.ingredients_list, parent, false);
        return new IngredientsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientsViewHolder holder, int position) {
        String name = mIngredients.get(position).getIngredient();
        String measure = mIngredients.get(position).getMeasure();
        String quantity = mIngredients.get(position).getQuantity();

        holder.mIngredientName.setText(name);
        holder.mIngredientDetail.setText(mContext.getString(R.string.ingredient_detail, quantity, measure));
    }

    @Override
    public int getItemCount() {
        if (mIngredients == null) return 0;
        return mIngredients.size();
    }

    public class IngredientsViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.ingredient_name_tv)
        TextView mIngredientName;
        @BindView(R.id.ingredient_detail_tv)
        TextView mIngredientDetail;

        public IngredientsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
