package com.example.foodrecipes.models

import android.os.Parcel
import android.os.Parcelable

data class Recipe(val title: String,
             val publisher: String,
             val publisher_url: String,
             val ingredients: Array<String>,
             val recipe_id: String,
             val image_url: String,
             val social_rank: Float) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.createStringArray() ?: arrayOf<String>(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readFloat()
    )

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeString(title)
        dest?.writeString(publisher)
        dest?.writeString(publisher_url)
        dest?.writeStringArray(ingredients)
        dest?.writeString(recipe_id)
        dest?.writeString(image_url)
        dest?.writeFloat(social_rank)
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun toString(): String {
        return "Recipe(title='$title', publisher='$publisher', publisher_url='$publisher_url', ingredients=${ingredients.contentToString()}, recipe_id='$recipe_id', image_url='$image_url', social_rank=$social_rank)"
    }

    companion object CREATOR : Parcelable.Creator<Recipe> {
        override fun createFromParcel(parcel: Parcel): Recipe {
            return Recipe(parcel)
        }

        override fun newArray(size: Int): Array<Recipe?> {
            return arrayOfNulls(size)
        }
    }


}