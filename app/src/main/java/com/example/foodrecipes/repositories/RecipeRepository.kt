package com.example.foodrecipes.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.foodrecipes.models.Recipe
import com.example.foodrecipes.requests.RecipeApiClient

class RecipeRepository constructor (
    private val recipeApiClient: RecipeApiClient = RecipeApiClient.instance
){

    companion object {
        val instance : RecipeRepository = RecipeRepository()
    }

    private var mQuery: String = ""
    private var mPageNumber: Int = -1

    fun getRecipes(): LiveData<List<Recipe>> = recipeApiClient.getRecipes()

    fun getRecipe(): LiveData<Recipe> = recipeApiClient.getRecipe()

    fun isRecipeRequestTimedOut(): LiveData<Boolean> = recipeApiClient.isRecipeRequestTimedOut()

    fun searchRecipesApi(query: String, pageNumber: Int) {
        var pn = pageNumber
        if (pn == 0){
            pn = 1
        }
        mQuery = query
        mPageNumber = pageNumber
        recipeApiClient.searchRecipesApi(query, pn)
    }

    fun getRecipeApi(recipeId: String){
        return recipeApiClient.getRecipesApi(recipeId)
    }

    fun cancelRequest(){
        recipeApiClient.cancelRequest()
    }

}