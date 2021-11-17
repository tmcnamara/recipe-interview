package com.example.foodrecipes.util

import android.util.Log
import com.example.foodrecipes.models.Recipe

class Testing {

    companion object {
        fun printRecipes(list: List<Recipe>, tag: String){
            for (recipe in list) {
                Log.d(tag, recipe.title);
            }
        }
    }
}