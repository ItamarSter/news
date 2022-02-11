package itamar.stern.news.models

import itamar.stern.news.R

class Category {
    companion object {
        val GENERAL = Pair("general", 0)
        val BUSINESS = Pair("business", 1)
        val ENTERTAINMENT = Pair("entertainment", 2)
        val HEALTH = Pair("health", 3)
        val SCIENCE = Pair("science", 4)
        val SPORTS = Pair("sports", 5)
        val TECHNOLOGY = Pair("technology", 6)
        val FAVORITES = Pair("favorites", 7)

        val CATEGORIES = hashMapOf(
            Pair(GENERAL.first, 0),
            Pair(BUSINESS.first, 1),
            Pair(ENTERTAINMENT.first, 2),
            Pair(HEALTH.first, 3),
            Pair(SCIENCE.first, 4),
            Pair(SPORTS.first, 5),
            Pair(TECHNOLOGY.first, 6),
            Pair(FAVORITES.first, 7)
        )

        val tabsColors = arrayOf(
            R.drawable.layer_bg_selected_tab_general,
            R.drawable.layer_bg_selected_tab_business,
            R.drawable.layer_bg_selected_tab_entertainment,
            R.drawable.layer_bg_selected_tab_health,
            R.drawable.layer_bg_selected_tab_science,
            R.drawable.layer_bg_selected_tab_sports,
            R.drawable.layer_bg_selected_tab_technology,
            R.drawable.layer_bg_selected_tab_favorites
        )
        val tabsBacks = arrayOf(
            R.color.tab_general,
            R.color.tab_business,
            R.color.tab_entertainment,
            R.color.tab_health,
            R.color.tab_science,
            R.color.tab_sports,
            R.color.tab_technology,
            R.color.tab_favorites
        )
    }
}