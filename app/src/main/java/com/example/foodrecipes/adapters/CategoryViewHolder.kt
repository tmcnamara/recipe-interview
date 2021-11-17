package com.example.foodrecipes.adapters

import android.view.View
import android.view.View.OnClickListener
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.layout_category_list_item.view.*

class CategoryViewHolder constructor(itemView: View, private val listener: OnRecipeListener): RecyclerView.ViewHolder(itemView), OnClickListener {

    init{
           itemView.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        listener.onCategoryClick(itemView.category_title.text.toString())
    }


}