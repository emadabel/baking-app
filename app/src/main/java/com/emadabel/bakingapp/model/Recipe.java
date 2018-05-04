package com.emadabel.bakingapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by Emad on 04/05/2018.
 */

public class Recipe implements Parcelable {

    public final static Parcelable.Creator<Recipe> CREATOR = new Creator<Recipe>() {

        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        public Recipe[] newArray(int size) {
            return (new Recipe[size]);
        }
    };

    private int id;
    private String name;
    private List<Ingredient> ingredients = null;
    private List<Step> steps = null;
    private int servings;
    private String image;

    private Recipe(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        in.readTypedList(this.ingredients, Ingredient.CREATOR);
        in.readTypedList(this.steps, Step.CREATOR);
        this.servings = in.readInt();
        this.image = in.readString();
    }

    public Recipe() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public List<Step> getSteps() {
        return steps;
    }

    public void setSteps(List<Step> steps) {
        this.steps = steps;
    }

    public int getServings() {
        return servings;
    }

    public void setServings(int servings) {
        this.servings = servings;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeTypedList(ingredients);
        dest.writeTypedList(steps);
        dest.writeInt(servings);
        dest.writeString(image);
    }

    public int describeContents() {
        return 0;
    }
}
