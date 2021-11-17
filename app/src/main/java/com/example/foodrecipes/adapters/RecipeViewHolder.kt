package com.example.foodrecipes.adapters

import android.view.View
import android.view.View.OnClickListener
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.layout_recipe_list_item.view.*

class RecipeViewHolder(itemView: View, private val onReciperListener: OnRecipeListener) : RecyclerView.ViewHolder(itemView), OnClickListener {

    init {
        itemView.setOnClickListener(this)
    }

    val title = itemView.recipe_title
    val publisher = itemView.recipe_publisher
    val socialScore = itemView.recipe_social_score
    val image = itemView.recipe_image

    override fun onClick(v: View?) {
        onReciperListener.onRecipeClick(adapterPosition)
    }


}