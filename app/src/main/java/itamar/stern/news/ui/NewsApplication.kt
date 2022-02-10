package itamar.stern.news.ui

import android.app.Application
import android.graphics.Bitmap
import itamar.stern.news.api.NewsApi
import itamar.stern.news.repository.NewsRepository
import itamar.stern.news.room_db.RoomDB

class NewsApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        lateinit var instance: NewsApplication

        val repository: NewsRepository by lazy {
            NewsRepository(NewsApi.create())
        }

        val roomDB: RoomDB by lazy {
            RoomDB.create(instance)
        }

        val bitmapImages = mutableMapOf<String, Bitmap>()

        var whereToGoFromWelcome = -1

        const val LANGUAGE = "en"

    }
}