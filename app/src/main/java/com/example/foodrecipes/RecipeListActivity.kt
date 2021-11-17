package com.example.foodrecipes

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.foodrecipes.adapters.OnRecipeListener
import com.example.foodrecipes.adapters.RecipeRecyclerAdapter
import com.example.foodrecipes.util.Testing
import com.example.foodrecipes.util.VerticalSpacingItemDecorator
import com.example.foodrecipes.viewmodels.RecipeListViewModel
import kotlinx.android.synthetic.main.activity_recipe_list.*

class RecipeListActivity : BaseActivity(), OnRecipeListener {

    companion object {
        val TAG : String = "RecipeListActivity"
    }

    private val recipeListViewModel : RecipeListViewModel by viewModels()
    private lateinit var recyclerAdapter : RecipeRecyclerAdapter
    private lateinit var searchView: SearchView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_recipe_list)

        initRecyclerView()
        subscribeObservers()
        initSearchView()
        if (!recipeListViewModel.isViewingRecipes) {
            displaySearchCategories()
        }
        setSupportActionBar(toolBar)
    }

    private fun subscribeObservers() {
        recipeListViewModel.getRecipes().observe(this, Observer{ recipes ->
            recipes?.let {
                if (recipeListViewModel.isViewingRecipes) {
                    Testing.printRecipes(recipes, TAG)
                    recipeListViewModel.isPerformingQuery = false
                    recyclerAdapter.setRecipes(recipes)
                }
            }
        })
    }

    private fun initSearchView(){
        search_view.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                recyclerAdapter.displayLoading()
                recipeListViewModel.searchRecipesApi(query, 1)
                search_view.clearFocus()
                return false
            }

        })

    }

    private fun initRecyclerView() {
        recyclerAdapter = RecipeRecyclerAdapter(this)
        val itemDecorator = VerticalSpacingItemDecorator(30)
        recipe_list.addItemDecoration(itemDecorator)
        recipe_list.adapter = recyclerAdapter
        recipe_list.layoutManager = LinearLayoutManager(this)

        recipe_list.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (!recipe_list.canScrollVertically(1)) {
                    // search the next page
                }
            }
        })
    }

    override fun onRecipeClick(position: Int) {
        val intent = Intent(this, RecipeActivity::class.java)
        intent.putExtra("recipe", recyclerAdapter.getSelectedRecipe(position))
        startActivity(intent)
    }

    override fun onCategoryClick(category: String) {
        recyclerAdapter.displayLoading()
        recipeListViewModel.searchRecipesApi(category, 1)
        search_view.clearFocus()
    }

    private fun displaySearchCategories(){
        recipeListViewModel.isViewingRecipes = false
        recyclerAdapter.displaySearchCategories()
    }

    override fun onBackPressed(){
        if (recipeListViewModel.onBackPressed()) {
            super.onBackPressed()
        } else {
            displaySearchCategories()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.recipe_search_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_categories -> displaySearchCategories()
        }
        return super.onOptionsItemSelected(item)
    }



}