package itamar.stern.news.ui

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
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

        val prefs: SharedPreferences by lazy {
            instance.getSharedPreferences("prefs", Context.MODE_PRIVATE)
        }

        val roomDB: RoomDB by lazy {
            RoomDB.create(instance)
        }

        val bitmapImages = mutableMapOf<String, Bitmap>()

        var whereToGoFromWelcome = -1

        const val LANGUAGE = "en"

    }
}