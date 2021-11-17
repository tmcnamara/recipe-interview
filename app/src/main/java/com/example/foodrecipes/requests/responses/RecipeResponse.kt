package com.example.foodrecipes.requests.responses

import com.example.foodrecipes.models.Recipe
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RecipeResponse {

    @SerializedName("recipe")
    @Expose()
    var recipe: Recipe? = null

    override fun toString(): String {
        return "RecipeResponse(recipe=$recipe)"
    }

}