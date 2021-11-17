package com.example.foodrecipes.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.foodrecipes.models.Recipe
import com.example.foodrecipes.repositories.RecipeRepository

class RecipeListViewModel constructor(
    private val recipeRepository: RecipeRepository = RecipeRepository.instance,
    var isViewingRecipes : Boolean = false,
    var isPerformingQuery: Boolean = false): ViewModel() {

    fun getRecipes(): LiveData<List<Recipe>> = recipeRepository.getRecipes()

    fun searchRecipesApi(query: String, pageNumber: Int) {
        isViewingRecipes = true
        recipeRepository.searchRecipesApi(query, pageNumber)
    }

    fun onBackPressed(): Boolean {
        if (isPerformingQuery) {
            recipeRepository.cancelRequest()
        }
        if (isViewingRecipes) {
            isViewingRecipes = false
            return false
        }
        return true
    }

}