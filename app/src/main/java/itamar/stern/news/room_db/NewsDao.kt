package itamar.stern.news.room_db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import itamar.stern.news.models.News

@Dao
interface NewsDao {
    @Insert
    fun addFavorites(news: News)

    @Query("SELECT * FROM FavoritesNews")
    fun getFavorites(): LiveData<List<News>>

    @Query("SELECT * FROM FavoritesNews WHERE published_at=:published_at")
    fun getFavoriteNewsByPublishedAt(published_at: String):List<News>

    @Query("DELETE FROM FavoritesNews WHERE published_at=:published_at")
    fun removeFavoriteByPublishedAt(published_at: String)
}