package itamar.stern.news.ui.welcome_screen

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import itamar.stern.news.R
import itamar.stern.news.databinding.ActivityWelcomeBinding
import itamar.stern.news.models.News
import itamar.stern.news.ui.NewsApplication
import itamar.stern.news.ui.main.MainActivity

class WelcomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWelcomeBinding
    private lateinit var viewModel: WelcomeViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this)[WelcomeViewModel::class.java]


        binding.favoritesLayout.setOnClickListener{
            it.setBackgroundResource(R.drawable.welcome_layouts_shape_clicked)
            NewsApplication.whereToGoFromWelcome = NewsApplication.FAVORITES
            binding.imageViewFavoritsIcon.setImageResource(R.drawable.favoritesiconclicked)
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }


}