package com.example.foodrecipes.util

class Constants {

    companion object{
        val BASE_URL = "https://recipesapi.herokuapp.com"
        val API_KEY = ""

        val NETWORK_TIMEOUT = 3000L

        val DEFAULT_SEARCH_CATEGORIES =
            arrayOf(
                "Barbeque",
                "Breakfast",
                "Chicken",
                "Beef",
                "Brunch",
                "Dinner",
                "Wine",
                "Italian"
            )

        val DEFAULT_SEARCH_CATEGORY_IMAGES =
            arrayOf(
                "barbeque",
                "breakfast",
                "chicken",
                "beef",
                "brunch",
                "dinner",
                "wine",
                "italian"
            )
    }


}