package itamar.stern.news.repository

import androidx.lifecycle.MutableLiveData
import itamar.stern.news.api.NewsApi
import itamar.stern.news.models.Category
import itamar.stern.news.models.News
import itamar.stern.news.utils.sendErrorsToFirebase

class NewsRepository(
    private val newsApi: NewsApi
) {
    val generalNewsList = MutableLiveData<List<News>>(mutableListOf())
    val businessNewsList = MutableLiveData<List<News>>(mutableListOf())
    val entertainmentNewsList = MutableLiveData<List<News>>(mutableListOf())
    val healthNewsList = MutableLiveData<List<News>>(mutableListOf())
    val scienceNewsList = MutableLiveData<List<News>>(mutableListOf())
    val sportsNewsList = MutableLiveData<List<News>>(mutableListOf())
    val technologyNewsList = MutableLiveData<List<News>>(mutableListOf())

    val welcomeNewsList = MutableLiveData<MutableList<News>>(mutableListOf())

    suspend fun fetchNews(category: String, offset: String = "0", limit: String = "100"){
        try{
            val news = newsApi.fetchMovies(category = category, offset = offset, limit = limit)
            when(category){
                Category.GENERAL.first -> {
                    generalNewsList.postValue(news.data)
                }
                Category.BUSINESS.first -> {
                    businessNewsList.postValue(news.data)
                }
                Category.ENTERTAINMENT.first -> {
                    entertainmentNewsList.postValue(news.data)
                }
                Category.HEALTH.first -> {
                    healthNewsList.postValue(news.data)
                }
                Category.SCIENCE.first -> {
                    scienceNewsList.postValue(news.data)
                }
                Category.SPORTS.first -> {
                    sportsNewsList.postValue(news.data)
                }
                Category.TECHNOLOGY.first -> {
                    technologyNewsList.postValue(news.data)
                }
            }
        } catch (e: Exception){
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
            sendErrorsToFirebase(e)
        }

        callbackDone()
    }

}