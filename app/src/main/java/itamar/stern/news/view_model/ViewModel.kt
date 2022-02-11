package itamar.stern.news.view_model

import android.app.Application
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
import android.text.Html
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import itamar.stern.news.R
import itamar.stern.news.models.Category
import itamar.stern.news.models.News
import itamar.stern.news.NewsApplication
import itamar.stern.news.network.NetworkStatusChecker
import itamar.stern.news.ui.main.MainActivity
import itamar.stern.news.utils.noInternet
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ViewModel(application: Application) : AndroidViewModel(application) {
    val favorites = NewsApplication.roomDB.newsDao().getFavorites()

    //the last loaded 100 news:
    private val generalNews = NewsApplication.repository.generalNewsList
    private val businessNews = NewsApplication.repository.businessNewsList
    private val entertainmentNews = NewsApplication.repository.entertainmentNewsList
    private val healthNews = NewsApplication.repository.healthNewsList
    private val scienceNews = NewsApplication.repository.scienceNewsList
    private val sportsNews = NewsApplication.repository.sportsNewsList
    private val technologyNews = NewsApplication.repository.technologyNewsList

    //all the loaded news:
    val allGeneralNews = MutableLiveData<MutableList<News>>(mutableListOf())
    val allBusinessNews = MutableLiveData<MutableList<News>>(mutableListOf())
    val allEntertainmentNews = MutableLiveData<MutableList<News>>(mutableListOf())
    val allHealthNews = MutableLiveData<MutableList<News>>(mutableListOf())
    val allScienceNews = MutableLiveData<MutableList<News>>(mutableListOf())
    val allSportsNews = MutableLiveData<MutableList<News>>(mutableListOf())
    val allTechnologyNews = MutableLiveData<MutableList<News>>(mutableListOf())

    //Flags to prevent double news loading:
    private val isLoadingNewsNow = mutableListOf(false, false, false, false, false, false, false)
    private val numberOfNewsFetched = mutableListOf(100, 100, 100, 100, 100, 100, 100)


    fun loadNews(
        offset: String,
        category: String,
        callbackStartDownloading: () -> Unit,
        callbackFinishedDownloading: () -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            launch(Dispatchers.Main) {
                callbackStartDownloading()
            }
            NewsApplication.repository.fetchNews(category, offset = offset)
            launch(Dispatchers.Main) {

                when (category) {
                    Category.GENERAL.first -> {
                        allGeneralNews.value?.addAll(generalNews.value!!)
                        allGeneralNews.value = allGeneralNews.value
                    }
                    Category.BUSINESS.first -> {
                        allBusinessNews.value?.addAll(businessNews.value!!)
                        allBusinessNews.value = allBusinessNews.value
                    }
                    Category.ENTERTAINMENT.first -> {
                        allEntertainmentNews.value?.addAll(entertainmentNews.value!!)
                        allEntertainmentNews.value = allEntertainmentNews.value
                    }
                    Category.HEALTH.first -> {
                        allHealthNews.value?.addAll(healthNews.value!!)
                        allHealthNews.value = allHealthNews.value
                    }
                    Category.SCIENCE.first -> {
                        allScienceNews.value?.addAll(scienceNews.value!!)
                        allScienceNews.value = allScienceNews.value
                    }
                    Category.SPORTS.first -> {
                        allSportsNews.value?.addAll(sportsNews.value!!)
                        allSportsNews.value = allSportsNews.value
                    }
                    Category.TECHNOLOGY.first -> {
                        allTechnologyNews.value?.addAll(technologyNews.value!!)
                        allTechnologyNews.value = allTechnologyNews.value
                    }
                }
                callbackFinishedDownloading()
            }
        }
    }


    fun openNewsDialog(
        context: Context,
        news: News,
        recyclerViewHeight: Int,
        recyclerViewWidth: Int
    ) {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.extended_new_item)
        dialog.window?.setLayout(recyclerViewWidth / 7 * 6, recyclerViewHeight / 4 * 3)
        dialog.findViewById<TextView>(R.id.textViewTitle2).text = news.title
        dialog.findViewById<TextView>(R.id.textViewDescription).text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(news.description, 0)
        } else Html.fromHtml(news.description)
        dialog.findViewById<TextView>(R.id.textViewSource2).text = news.source
        dialog.findViewById<Button>(R.id.buttonBrowse).setOnClickListener {
            browseNews(news.url)
        }
        dialog.findViewById<Button>(R.id.buttonFavorites).setOnClickListener {
            //Check if user logged in. just logged in can mark favorites:
            if(NewsApplication.account != null){
                clickOnFavorites(news, it as MaterialButton)
            } else {
                dialog.dismiss()
                MainActivity.goToLogin.postValue(true)
            }
        }

        (dialog.findViewById<Button>(R.id.buttonFavorites) as MaterialButton).setIconResource(
            if (isFavorite(
                    news.published_at
                )
            ) R.drawable.ic_baseline_star_24 else R.drawable.ic_baseline_star_border_24
        )
        if (news.image != null) {
            val imageView = dialog.findViewById<ImageView>(R.id.imageView2)
            imageView.setImageBitmap(NewsApplication.bitmapImages[news.image])
            imageView.layoutParams.height = recyclerViewWidth / 2
        }
        dialog.show()
    }

    private fun browseNews(url: String) {
        ContextCompat.startActivity(
            getApplication(),
            Intent(Intent.ACTION_VIEW, Uri.parse(url)),
            null
        )
    }

    private fun clickOnFavorites(news: News, button: MaterialButton) {
        //Check if we already marked this news as favorite. yes -> remove it. no -> mark it as favorite.
        //Replace the star icon with star border and vice versa
        with(NewsApplication.roomDB.newsDao()) {
            if (isFavorite(news.published_at)) {
                removeFavoriteByPublishedAt(news.published_at)
                button.setIconResource(R.drawable.ic_baseline_star_border_24)
            } else {
                addFavorites(news)
                button.setIconResource(R.drawable.ic_baseline_star_24)
            }
        }
    }

    private fun isFavorite(published_at: String): Boolean {
        if (NewsApplication.roomDB.newsDao().getFavoriteNewsByPublishedAt(published_at).isEmpty()) {
            return false
        }
        return true
    }

    fun listenToScrollAndLoadMoreNews(recyclerView: RecyclerView, category: String, callbackStartDownloading: () -> Unit, callbackFinishedDownloading: (Int) -> Unit){
        val whichCategory = when(category){

            Category.GENERAL.first -> 0
            Category.BUSINESS.first -> 1
            Category.ENTERTAINMENT.first -> 2
            Category.HEALTH.first -> 3
            Category.SCIENCE.first -> 4
            Category.SPORTS.first -> 5
            Category.TECHNOLOGY.first -> 6
            else -> {-1}
        }
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (!recyclerView.canScrollVertically(1) && !isLoadingNewsNow[whichCategory]) {
                    if (noInternet(getApplication<Application>())){
                        return
                    }
                    loadNews("${numberOfNewsFetched[whichCategory]}", category,{
                        //Set isLoadingNewsNow true to prevent reloading of the same news (if the user scrolls again to bottom before the download is completed)
                        isLoadingNewsNow[whichCategory] = true
                        callbackStartDownloading()
                    }){
                        isLoadingNewsNow[whichCategory] = false
                        numberOfNewsFetched[whichCategory] += 100

                        callbackFinishedDownloading(numberOfNewsFetched[whichCategory])
                    }
                }
            }
        })
    }
}