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

class RecipeApiClient {

    private val coroutineScope = CoroutineScope(Dispatchers.IO)
    private var searchJob: Job = Job()

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