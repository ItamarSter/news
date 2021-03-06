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

        //Singleton repository
        val repository: NewsRepository by lazy {
            NewsRepository(NewsApi.create())
        }
        //Save language in prefs
        val prefs: SharedPreferences by lazy {
            instance.getSharedPreferences("prefs", Context.MODE_PRIVATE)
        }
        //Favorites saved in room
        val roomDB: RoomDB by lazy {
            RoomDB.create(instance)
        }
        //Errors sent to firebase database
        val fireDBRef: DatabaseReference by lazy {
            FirebaseDatabase.getInstance().reference
        }
        //Save downloaded images for reuse
        val bitmapImages = mutableMapOf<String, Bitmap>()
        //Helper to navigate from welcome page
        var whereToGoFromWelcome = -1

        var LANGUAGE = "en"
        //Google sign-in
        private val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestId()
            .requestProfile()
            .build()

        val mGoogleSignInClient: GoogleSignInClient by lazy {
            GoogleSignIn.getClient(instance, gso)
        }
        //Current google account details
        val account get() = GoogleSignIn.getLastSignedInAccount(instance)
    }
}