package itamar.stern.news.ui.welcome_screen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import itamar.stern.news.R
import itamar.stern.news.adapters.WelcomeAdapter
import itamar.stern.news.databinding.ActivityWelcomeBinding
import itamar.stern.news.models.Category
import itamar.stern.news.NewsApplication
import itamar.stern.news.ui.main.MainActivity

class WelcomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWelcomeBinding
    private lateinit var viewModel: WelcomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[WelcomeViewModel::class.java]
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.loadNews({}){
            binding.progressBarWelcome.visibility = View.INVISIBLE
        }

        //OnClick: color the layout, go to favorites:
        binding.favoritesLayout.setOnClickListener{
            it.setBackgroundResource(R.drawable.welcome_layouts_shape_clicked)
            NewsApplication.whereToGoFromWelcome = Category.FAVORITES.second
            binding.imageViewFavoritsIcon.setImageResource(R.drawable.favoritesiconclicked)
            startActivity(Intent(this, MainActivity::class.java))
        }
        binding.recyclerViewWelcome.layoutManager = LinearLayoutManager(this)
        viewModel.welcomeNews.observe(this){
            binding.recyclerViewWelcome.adapter = WelcomeAdapter(it){ category ->
                //onClick on news:
                NewsApplication.whereToGoFromWelcome = Category.CATEGORIES[category]!!
                startActivity(Intent(this, MainActivity::class.java))
            }
        }
    }


}