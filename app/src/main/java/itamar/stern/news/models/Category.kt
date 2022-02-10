package itamar.stern.news.models

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
    }
}