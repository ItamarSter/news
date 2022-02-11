package itamar.stern.news.repository

import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import itamar.stern.news.NewsApplication
import itamar.stern.news.api.NewsApi
import itamar.stern.news.models.Category
import itamar.stern.news.models.News
import itamar.stern.news.ui.welcome_screen.WelcomeActivity
import itamar.stern.news.utils.sendErrorsToFirebase
import retrofit2.HttpException

class NewsRepository(
    private val newsApi: NewsApi
) {
    val newsListsMap = hashMapOf(
        Pair(Category.GENERAL.first, MutableLiveData<List<News>>(mutableListOf())),
        Pair(Category.BUSINESS.first, MutableLiveData<List<News>>(mutableListOf())),
        Pair(Category.ENTERTAINMENT.first, MutableLiveData<List<News>>(mutableListOf())),
        Pair(Category.HEALTH.first, MutableLiveData<List<News>>(mutableListOf())),
        Pair(Category.SCIENCE.first, MutableLiveData<List<News>>(mutableListOf())),
        Pair(Category.SPORTS.first, MutableLiveData<List<News>>(mutableListOf())),
        Pair(Category.TECHNOLOGY.first, MutableLiveData<List<News>>(mutableListOf())),
    )


    val welcomeNewsList = MutableLiveData<MutableList<News>>(mutableListOf())

    suspend fun fetchNews(category: String, offset: String = "0", limit: String = "100"){
        try{
            val news = newsApi.fetchMovies(category = category, offset = offset, limit = limit)
            newsListsMap[category]?.postValue(news.data)
        } catch (e: Exception){
            if(e is HttpException){
                WelcomeActivity.noMoreRequests.postValue(true)
            }
            sendErrorsToFirebase(e)
        }
    }

    suspend fun fetchNewsForWelcome(callbackDone:()->Unit) {
        try {
            var news = newsApi.fetchMovies(category = Category.GENERAL.first, limit = "1")
            welcomeNewsList.value?.addAll(news.data)
            news = newsApi.fetchMovies(category = Category.BUSINESS.first, limit = "1")
            welcomeNewsList.value?.addAll(news.data)
            news = newsApi.fetchMovies(category = Category.ENTERTAINMENT.first, limit = "1")
            welcomeNewsList.value?.addAll(news.data)
            news = newsApi.fetchMovies(category = Category.HEALTH.first, limit = "1")
            welcomeNewsList.value?.addAll(news.data)
            news = newsApi.fetchMovies(category = Category.SCIENCE.first, limit = "1")
            welcomeNewsList.value?.addAll(news.data)
            news = newsApi.fetchMovies(category = Category.SPORTS.first, limit = "1")
            welcomeNewsList.value?.addAll(news.data)
            news = newsApi.fetchMovies(category = Category.TECHNOLOGY.first, limit = "1")
            welcomeNewsList.value?.addAll(news.data)
        } catch (e: Exception){
            if(e is HttpException){
                WelcomeActivity.noMoreRequests.postValue(true)
            }
            sendErrorsToFirebase(e)
        }

        callbackDone()
    }

}