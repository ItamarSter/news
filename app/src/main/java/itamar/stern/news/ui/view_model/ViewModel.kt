package itamar.stern.news.ui.view_model

import android.app.Application
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.ScrollView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import itamar.stern.news.R
import itamar.stern.news.models.News
import itamar.stern.news.ui.NewsApplication
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
            NewsApplication.repository.fetchNews("en", category, offset = offset)
            launch(Dispatchers.Main) {

                when (category) {
                    "general" -> {
                        allGeneralNews.value?.addAll(generalNews.value!!)
                        allGeneralNews.value = allGeneralNews.value
                    }
                    "business" -> {
                        allBusinessNews.value?.addAll(businessNews.value!!)
                        allBusinessNews.value = allBusinessNews.value
                    }
                    "entertainment" -> {
                        allEntertainmentNews.value?.addAll(entertainmentNews.value!!)
                        allEntertainmentNews.value = allEntertainmentNews.value
                    }
                    "health" -> {
                        allHealthNews.value?.addAll(healthNews.value!!)
                        allHealthNews.value = allHealthNews.value
                    }
                    "science" -> {
                        allScienceNews.value?.addAll(scienceNews.value!!)
                        allScienceNews.value = allScienceNews.value
                    }
                    "sports" -> {
                        allSportsNews.value?.addAll(sportsNews.value!!)
                        allSportsNews.value = allSportsNews.value
                    }
                    "technology" -> {
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
        dialog.findViewById<TextView>(R.id.textViewDescription).text = news.description
        dialog.findViewById<TextView>(R.id.textViewSource2).text = news.source
        dialog.findViewById<Button>(R.id.buttonBrowse).setOnClickListener {
            browseNews(news.url)
        }
        dialog.findViewById<Button>(R.id.buttonFavorites).setOnClickListener {
            clickOnFavorites(news, it as MaterialButton)
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

    fun browseNews(url: String) {
        ContextCompat.startActivity(
            getApplication(),
            Intent(Intent.ACTION_VIEW, Uri.parse(url)),
            null
        )
    }

    fun clickOnFavorites(news: News, button: MaterialButton) {
        //Check if we already marked this news as favorite. yes -> remove it. no -> mark it as favorite.
        //Replace the star icon with star border and vice versa
        with(NewsApplication.roomDB.newsDao()) {
            if (isFavorite(news.published_at)) {
                removeFavorite(news.published_at)
                button.setIconResource(R.drawable.ic_baseline_star_border_24)
            } else {
                addFavorites(news)
                button.setIconResource(R.drawable.ic_baseline_star_24)
            }
        }
    }

    fun isFavorite(published_at: String): Boolean {
        if (NewsApplication.roomDB.newsDao().getFavoriteNewsByPublishedAt(published_at).isEmpty()) {
            return false
        }
        return true
    }

    fun listenToScrollAndLoadMoreNews(recyclerView: RecyclerView, category: String, callbackStartDownloading: () -> Unit, callbackFinishedDownloading: (Int) -> Unit){
        val whichCategory = when(category){
            "general" -> 0
            "business" -> 1
            "entertainment" -> 2
            "health" -> 3
            "science" -> 4
            "sports" -> 5
            "technology" -> 6
            else -> {-1}
        }
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (!recyclerView.canScrollVertically(1) && !isLoadingNewsNow[whichCategory]) {
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