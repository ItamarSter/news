package itamar.stern.news

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
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

        val fireDBRef: DatabaseReference by lazy {
            FirebaseDatabase.getInstance().reference
        }

        val bitmapImages = mutableMapOf<String, Bitmap>()

        var whereToGoFromWelcome = -1

        var LANGUAGE = "en"

        private val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestId()
            .requestProfile()
            .build()

        val mGoogleSignInClient: GoogleSignInClient by lazy {
            GoogleSignIn.getClient(instance, gso)
        }

        val account get() = GoogleSignIn.getLastSignedInAccount(instance)
    }
}