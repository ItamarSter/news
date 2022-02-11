package itamar.stern.news.room_db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import itamar.stern.news.models.News

const val DB_VERSION = 1
const val DB_NAME = "NewsDatabase"

@Database(entities = [News::class], version = DB_VERSION)
abstract class RoomDB : RoomDatabase() {
    companion object {
        fun create(context: Context): RoomDB {
            return Room.databaseBuilder(context, RoomDB::class.java, DB_NAME)
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build()
        }
    }
    abstract fun newsDao(): NewsDao

}