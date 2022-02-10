package itamar.stern.news.ui.welcome_screen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import itamar.stern.news.adapters.WelcomeAdapter
import itamar.stern.news.databinding.ActivityWelcomeBinding
import itamar.stern.news.models.Category
import itamar.stern.news.NewsApplication
import itamar.stern.news.ui.main.MainActivity
import android.widget.ArrayAdapter
import android.widget.Toast


class WelcomeActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    private val languages = arrayOf("en", "de", "es")
    private lateinit var binding: ActivityWelcomeBinding
    private lateinit var viewModel: WelcomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[WelcomeViewModel::class.java]
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
    override fun onResume() {
        super.onResume()
        NewsApplication.LANGUAGE = NewsApplication.prefs.getString("language", "en").toString()
        viewModel.welcomeNews.postValue(mutableListOf())
        viewModel.loadNews({}){
            binding.progressBarWelcome.visibility = View.INVISIBLE
        }


        //OnClick: color the layout, go to favorites:
        binding.favoritesLayout.setOnClickListener{
            it.setBackgroundResource(itamar.stern.news.R.drawable.welcome_layouts_shape_clicked)
            NewsApplication.whereToGoFromWelcome = Category.FAVORITES.second
            binding.imageViewFavoritsIcon.setImageResource(itamar.stern.news.R.drawable.favoritesiconclicked)
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
        binding.recyclerViewWelcome.layoutManager = LinearLayoutManager(this)
        viewModel.welcomeNews.observe(this){
            binding.recyclerViewWelcome.adapter = WelcomeAdapter(it){ category ->
                //onClick on news:
                NewsApplication.whereToGoFromWelcome = Category.CATEGORIES[category]!!
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }

        //Set the spinner:
        val dropdown = binding.spinner
        //create a list of items for the spinner.

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, languages)
        dropdown.adapter = adapter
        binding.spinner.setSelection(languages.indexOf(NewsApplication.LANGUAGE), false)
        dropdown.onItemSelectedListener = this
    }
    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        NewsApplication.LANGUAGE = languages[position].substring(0, 2)
        NewsApplication.prefs.edit().putString("language", NewsApplication.LANGUAGE).apply()
        Toast.makeText(this, "Reopen the app to refresh the settings", Toast.LENGTH_SHORT).show()
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {}



}