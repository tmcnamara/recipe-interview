package com.example.foodrecipes.requests.responses

import com.example.foodrecipes.models.Recipe
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RecipeSearchResponse {

    @SerializedName("count")
    @Expose()
    var count: Int = 0

    @SerializedName("recipes")
    @Expose()
    var recipes: List<Recipe> = listOf()

    override fun toString(): String {
        return "RecipeSearchResponse(count=$count, recipes=$recipes)"
    }


}