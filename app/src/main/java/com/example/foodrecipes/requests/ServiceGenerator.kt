package com.example.foodrecipes.requests

import com.example.foodrecipes.util.Constants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ServiceGenerator {

    companion object {
        private val retrofitBuilder: Retrofit.Builder = Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())

        private val retrofit = retrofitBuilder.build()

        val recipeApi: RecipeApi = retrofit.create(RecipeApi::class.java)
    }
}