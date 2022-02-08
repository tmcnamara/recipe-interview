package com.example.foodrecipes.requests

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.foodrecipes.models.Recipe
import com.example.foodrecipes.requests.responses.RecipeResponse
import com.example.foodrecipes.requests.responses.RecipeSearchResponse
import com.example.foodrecipes.util.Constants
import com.example.foodrecipes.util.Constants.Companion.NETWORK_TIMEOUT
import kotlinx.coroutines.*
import retrofit2.Call
import java.io.IOException
import java.util.*

class RecipeApiClient {

    private val coroutineScope = CoroutineScope(Dispatchers.IO)
    private var searchJob: Job = Job()
    private var detailsJob: Job = Job()
    companion object {
        val TAG : String = "RecipeApiClient"
        val instance : RecipeApiClient = RecipeApiClient()
    }

    private val recipes : MutableLiveData<List<Recipe>> by lazy {
        MutableLiveData<List<Recipe>>()
    }

    private val recipeRequestTimeout : MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    private val recipe : MutableLiveData<Recipe> by lazy {
        MutableLiveData<Recipe>()
    }

    fun getRecipes(): LiveData<List<Recipe>> = recipes

    fun getRecipe(): LiveData<Recipe> = recipe

    fun isRecipeRequestTimedOut(): LiveData<Boolean> = recipeRequestTimeout

    fun searchRecipesApi(query: String, pageNumber: Int) {
        searchJob.cancel()

        try {
            searchJob = coroutineScope.launch {
                withTimeout(NETWORK_TIMEOUT) {
                   try {
                       val response = getRecipes(query, pageNumber).execute()

                       if (response.code() == 200) {
                           //TODO #1 - Handle the getRecipes response by publishing the recipes back into the LiveData.
                           // Note that the api has paging so if you beyond page 1, add the recipes to the existing list prior to the publish.
                                if(pageNumber >1){
                                    val  updatedList:MutableList<Recipe> = mutableListOf()
                                   val eList =  recipes.value
                                    eList?.let { updatedList.addAll(it) }
                                    response.body()?.let {
                                        updatedList.addAll(it.recipes)
                                    }
                                    recipes.postValue(updatedList)
                                }else {
                                    response.body()?.let { recipes.postValue(it.recipes) }
                                }
                       } else {
                           val error = response.errorBody().toString()
                           Log.e(TAG, "Error on search api: $error")
                           recipes.postValue(null)
                       }
                   } catch (e:Exception){
                       Log.e(TAG, "Exception on search api",e)
                       recipes.postValue(null)
                   }
                }
            }
        }catch (e: TimeoutCancellationException){
            Log.e(TAG, "Timeout exception on search api", e)
            recipes.postValue(null)
        }catch (e: IOException){
            Log.e(TAG, "IO exception on search api", e)
            recipes.postValue(null)
        }
    }

    fun getRecipesApi(recipeId: String) {
        recipeRequestTimeout.postValue(false)
        searchJob.cancel()

        try {

            //TODO #2 - Follow the same pattern you see searchRecipesApi to implement the getRecipe call here.

           detailsJob = coroutineScope.launch {
                withTimeout(NETWORK_TIMEOUT) {
                    try {
                        val response = getRecipe(recipeId).execute()

                        if (response.code() == 200) {
                                response.body()?.let { recipe.postValue(it.recipe) }
                        } else {
                            val error = response.errorBody().toString()
                            Log.e(TAG, "Error on search api: $error")
                            recipe.postValue(null)
                        }
                    } catch (e:Exception){
                        Log.e(TAG, "Exception on search api",e)
                        recipe.postValue(null)
                    }
                }
            }

        }catch (e: TimeoutCancellationException){
            Log.e(TAG, "Timeout exception on search api", e)
            recipeRequestTimeout.postValue(true)
        }catch (e: IOException){
            Log.e(TAG, "IO exception on search api", e)
            recipeRequestTimeout.postValue(true)
        }
    }

    fun getRecipes(query: String, pageNumber: Int) : Call<RecipeSearchResponse> {
        return ServiceGenerator.recipeApi.searchRecipe(Constants.API_KEY, query, pageNumber.toString())
    }

    fun getRecipe(recipeId: String) : Call<RecipeResponse> {
        return ServiceGenerator.recipeApi.getRecipe(Constants.API_KEY,recipeId)
    }

    fun cancelRequest() {
        searchJob?.let {
            it.cancel("cancelling request")
        }
    }
}