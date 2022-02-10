package itamar.stern.news.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "FavoritesNews")
data class News (
    val author:String?,
    val title: String,
    val description: String,
    val url: String,
    val source: String,
    val image: String?,
    val category: String,
    val language: String,
    val country: String,
    @PrimaryKey
    val published_at: String
    )