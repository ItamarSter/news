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
import androidx.lifecycle.MutableLiveData
import itamar.stern.news.utils.noInternet


class WelcomeActivity : AppCompatActivity() {

    private val languages = arrayOf("en", "de", "es")
    private lateinit var binding: ActivityWelcomeBinding
    private lateinit var viewModel: WelcomeViewModel
    //Flag to prevent double refreshing news when pressed back to this page (spinner selection and onResume, both cause to refresh)
    private var isRefreshingNow = false

    companion object {
        //Flag to tell the user that the api 500 request are over.
        val noMoreRequests = MutableLiveData(false)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[WelcomeViewModel::class.java]
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        noMoreRequests.observe(this){
            if(it){
                Toast.makeText(this, "The free api used here is limited to 500 requests. You have to talk to the app developer to fix this.", Toast.LENGTH_LONG).show()
            }
        }
        
    }
    override fun onResume() {
        super.onResume()
        NewsApplication.LANGUAGE = NewsApplication.prefs.getString("language", "en").toString()

        if(!isRefreshingNow) {
            refreshNews()
        }

        //OnClick: go to favorites:
        binding.animationArrowFavorites.setOnClickListener{
            NewsApplication.whereToGoFromWelcome = Category.FAVORITES.second
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

        //Set the languages spinner:
        val dropdown = binding.spinner
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, languages)
        dropdown.adapter = adapter
        binding.spinner.setSelection(languages.indexOf(NewsApplication.LANGUAGE), false)

        dropdown.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                NewsApplication.LANGUAGE = languages[position].substring(0, 2)
                NewsApplication.prefs.edit().putString("language", NewsApplication.LANGUAGE).apply()
                if(!isRefreshingNow){
                    refreshNews()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        binding.buttonRefresh.setOnClickListener {
            refreshNews()
        }
    }

    private fun refreshNews() {
        viewModel.welcomeNews.postValue(mutableListOf())
        if(noInternet(this)){
            binding.buttonRefresh.visibility = View.VISIBLE
            return
        } else {
            binding.buttonRefresh.visibility = View.INVISIBLE
        }
        viewModel.loadNews({
            isRefreshingNow = true
            binding.progressBarWelcome.visibility = View.VISIBLE
        }) {
            isRefreshingNow = false
            binding.progressBarWelcome.visibility = View.INVISIBLE
        }
    }
}