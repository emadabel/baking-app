package com.emadabel.bakingapp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.emadabel.bakingapp.R;
import com.emadabel.bakingapp.model.Ingredient;

import java.util.List;

public class WidgetListAdapter extends ArrayAdapter<Ingredient> {
    private final Context context;
    private final List<Ingredient> ingredientList;

    public WidgetListAdapter(@NonNull Context context, List<Ingredient> ingredients) {
        super(context, -1, ingredients);
        this.context = context;
        this.ingredientList = ingredients;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.widget_list, parent, false);
        TextView nameTextView = (TextView) rowView.findViewById(R.id.ingredient_name_tv);
        TextView detailTextView = (TextView) rowView.findViewById(R.id.ingredient_detail_tv);
        nameTextView.setText(ingredientList.get(position).getIngredient());
        detailTextView.setText(context.getString(R.string.ingredient_detail,
                ingredientList.get(position).getQuantity(), ingredientList.get(position).getMeasure()));

        return rowView;
    }
}
