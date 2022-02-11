package itamar.stern.news.view_model

import android.app.Application
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.text.Html
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
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
import itamar.stern.news.ui.main.MainActivity
import itamar.stern.news.utils.noInternet
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ViewModel(application: Application) : AndroidViewModel(application) {
    val favorites = NewsApplication.roomDB.newsDao().getFavorites()

//the last loaded 100 news:
    private val newsListsMap = hashMapOf(
        Pair(Category.GENERAL.first, NewsApplication.repository.newsListsMap[Category.GENERAL.first]),
        Pair(Category.BUSINESS.first, NewsApplication.repository.newsListsMap[Category.BUSINESS.first]),
        Pair(Category.ENTERTAINMENT.first, NewsApplication.repository.newsListsMap[Category.ENTERTAINMENT.first]),
        Pair(Category.HEALTH.first, NewsApplication.repository.newsListsMap[Category.HEALTH.first]),
        Pair(Category.SCIENCE.first, NewsApplication.repository.newsListsMap[Category.SCIENCE.first]),
        Pair(Category.SPORTS.first, NewsApplication.repository.newsListsMap[Category.SPORTS.first]),
        Pair(Category.TECHNOLOGY.first, NewsApplication.repository.newsListsMap[Category.TECHNOLOGY.first]),
    )

    //all the loaded news:
    val allLoadedNewsLists = hashMapOf(
        Pair(Category.GENERAL.first, MutableLiveData<MutableList<News>>(mutableListOf())),
        Pair(Category.BUSINESS.first, MutableLiveData<MutableList<News>>(mutableListOf())),
        Pair(Category.ENTERTAINMENT.first, MutableLiveData<MutableList<News>>(mutableListOf())),
        Pair(Category.HEALTH.first, MutableLiveData<MutableList<News>>(mutableListOf())),
        Pair(Category.SCIENCE.first, MutableLiveData<MutableList<News>>(mutableListOf())),
        Pair(Category.SPORTS.first, MutableLiveData<MutableList<News>>(mutableListOf())),
        Pair(Category.TECHNOLOGY.first, MutableLiveData<MutableList<News>>(mutableListOf())),
    )

    //Flags to prevent double news loading:
    private val isLoadingNewsNow = mutableListOf(false, false, false, false, false, false, false)
    private val numberOfNewsFetched = mutableListOf(100, 100, 100, 100, 100, 100, 100)

    //Using coroutines to asynchronously fetch data from the internet
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
                allLoadedNewsLists[category]?.value?.addAll(newsListsMap[category]?.value!!)
                allLoadedNewsLists[category]?.value = allLoadedNewsLists[category]?.value
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
        //Using room database for save the favorites on the device.
        //It's faster to show, mark and remove favorites from room than from an internet server.
        dialog.findViewById<Button>(R.id.buttonFavorites).setOnClickListener {
            //Check if user logged in. Just logged in can mark favorites:
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
        //Check if the api gave us image url:
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
            Intent(Intent.ACTION_VIEW, Uri.parse(url)).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK),
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
        val whichCategory = Category.CATEGORIES[category] ?: -1

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