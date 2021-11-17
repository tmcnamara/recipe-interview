package com.example.foodrecipes

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.foodrecipes.glide.GlideApp
import com.example.foodrecipes.models.Recipe
import com.example.foodrecipes.viewmodels.RecipeViewModel
import kotlinx.android.synthetic.main.activity_recipe.*

class RecipeActivity : BaseActivity() {

    companion object {
        private const val TAG = "RecipeActivity"
    }

    private val recipeViewModel : RecipeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_recipe)

        showProgressBar(true)
        subscribeObservers()
        getIncomingIntent()
    }

    fun getIncomingIntent() {
        if (intent.hasExtra("recipe")) {
            val recipe : Recipe = intent.getParcelableExtra("recipe")
            Log.d(TAG, "getIncomingIntent: ${recipe.title}")
            recipeViewModel.getRecipeById(recipe.recipe_id)
        }
    }

    fun subscribeObservers(){
        recipeViewModel.getRecipe().observe(this, Observer{ recipe ->
            recipe?.let {
                if (it.recipe_id == recipeViewModel.recipeId) {
                    setRecipeProperties(it)
                    recipeViewModel.didRetrieveRecipe = true
                }
            }
        })

        recipeViewModel.isRecipeRequestTimedOut().observe(this, Observer{ timeout ->
            if (timeout && !recipeViewModel.didRetrieveRecipe) {
                displayErrorScreen("Error retrieving data.")
                Log.d(TAG, "TIMED OUT!!!");
            }
        })

    }

    private fun displayErrorScreen(errorMessage: String) {

        recipe_title.text = "Error Receiving Recipe...."
        recipe_social_score.text = ""

        var textView = TextView(this)
        if (errorMessage.isNotEmpty()) {
            textView.text = errorMessage
        } else {
            textView.text = "Error"
        }
        textView.textSize = 15f
        textView.layoutParams = LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
        ingredients_container.addView(textView)

        val requestOptions = RequestOptions().placeholder(R.drawable.ic_launcher_background)

        GlideApp.with(this).setDefaultRequestOptions(requestOptions)
            .load(R.drawable.ic_launcher_background)
            .into(recipe_image)
        showParent()
        showProgressBar(false)
    }

    fun setRecipeProperties(recipe: Recipe) {
        recipe?.let{
            val requestOptions = RequestOptions().placeholder(R.drawable.ic_launcher_background)

            GlideApp.with(this).setDefaultRequestOptions(requestOptions)
                .load(it.image_url)
                .into(recipe_image)

            //TODO #4 - Populate the page seen in activity_recipe.xml
            // Layout can be seen here: https://drive.google.com/file/d/1HQTV1OEqBPBOMUod9Xx085yZckARLpwO/view?usp=sharing
            // Populate the following:
            // - social score
            // - recipe title
            // - all ingredients as seen in layout

            showParent()
            showProgressBar(false)
        }
    }

    fun showParent() {
        parent_scroll_bar.visibility = View.VISIBLE
    }



}