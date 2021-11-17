package com.example.foodrecipes.adapters


import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.request.RequestOptions
import com.example.foodrecipes.R
import com.example.foodrecipes.glide.GlideApp
import com.example.foodrecipes.models.Recipe
import com.example.foodrecipes.util.Constants
import kotlinx.android.synthetic.main.layout_category_list_item.view.*

class RecipeRecyclerAdapter(private val  onRecipeListener: OnRecipeListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        val RECIPE_TYPE = 1
        val LOADING_TYPE = 2
        val CATEGORY_TYPE = 3
    }

    private var recipes: List<Recipe> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            RECIPE_TYPE -> RecipeViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.layout_recipe_list_item, parent, false), onRecipeListener)
            LOADING_TYPE -> LoadingViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.layout_loading_list_item, parent, false))
            CATEGORY_TYPE -> CategoryViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.layout_category_list_item, parent, false), onRecipeListener)
            else -> RecipeViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.layout_recipe_list_item, parent, false), onRecipeListener)
        }
    }

    override fun getItemCount(): Int {
        return recipes.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val itemViewType = getItemViewType(position)
        if (itemViewType == RECIPE_TYPE) {
            val requestOptions = RequestOptions().placeholder(R.drawable.ic_launcher_background)
            GlideApp.with((holder as RecipeViewHolder).itemView.context)
                .setDefaultRequestOptions(requestOptions)
                .load(recipes.get(position).image_url)
                .into(holder.image)

            //TODO #3 populate the title, publisher, socialScore on the RecipeViewHolder. Display score as a rounded whole number.

        } else if (itemViewType == CATEGORY_TYPE) {
            val requestOptions = RequestOptions().placeholder(R.drawable.ic_launcher_background)

            val path = Uri.parse("android.resource://com.example.foodrecipes/drawable/"+recipes.get(position).image_url)
            GlideApp.with((holder as CategoryViewHolder).itemView.context)
                .setDefaultRequestOptions(requestOptions)
                .load(path)
                .into(holder.itemView.category_image)

            holder.itemView.category_title.text = recipes.get(position).title

        }
    }

    override fun getItemViewType(position: Int): Int {
        if (recipes.get(position).social_rank == -1f) {
            return CATEGORY_TYPE
        } else if (recipes.get(position).title == "LOADING...") {
            return LOADING_TYPE
        } else if (position == recipes.size - 1 && position != 0 && recipes.get(position).title != "EXHAUSTED..." && isLoading()) {
            return LOADING_TYPE
        } else {
            return RECIPE_TYPE
        }
    }

    fun displayLoading(){
        if (!isLoading()) {
            val recipe = Recipe("LOADING...", "", "", arrayOf(), "", "", 0f)
            val loadingList = listOf(recipe)
            recipes = loadingList
            notifyDataSetChanged()
        }
    }

    fun displaySearchCategories(){
        val categories = mutableListOf<Recipe>()
        for (i in Constants.DEFAULT_SEARCH_CATEGORIES.indices) {
            categories += Recipe(Constants.DEFAULT_SEARCH_CATEGORIES[i],"","",arrayOf(),"",Constants.DEFAULT_SEARCH_CATEGORY_IMAGES[i],-1f)
        }

        recipes = categories
        notifyDataSetChanged()
    }

    private fun isLoading() : Boolean {
        if (!recipes.isNullOrEmpty()){
            return recipes.get(recipes.size - 1).title == "LOADING..."
        }
        return false
    }

    fun setRecipes(recipes: List<Recipe>) {
        this.recipes = recipes
        notifyDataSetChanged()
    }

    fun getSelectedRecipe(position: Int) : Recipe? {
        if (!recipes.isNullOrEmpty()){
            return recipes.get(position)
        }
        return null
    }
}