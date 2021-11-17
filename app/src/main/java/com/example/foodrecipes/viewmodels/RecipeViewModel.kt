package com.example.foodrecipes.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.foodrecipes.models.Recipe
import com.example.foodrecipes.repositories.RecipeRepository

class RecipeViewModel constructor(
    private val recipeRepository: RecipeRepository = RecipeRepository.instance,
    var didRetrieveRecipe : Boolean = false) : ViewModel() {

    var recipeId: String = ""

    fun getRecipe() : LiveData<Recipe> = recipeRepository.getRecipe()

    fun getRecipeById(recipeId: String){
        this.recipeId = recipeId
        return recipeRepository.getRecipeApi(recipeId)
    }

    fun isRecipeRequestTimedOut() = recipeRepository.isRecipeRequestTimedOut()



}