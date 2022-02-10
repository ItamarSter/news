package itamar.stern.news.repository

import androidx.lifecycle.MutableLiveData
import itamar.stern.news.api.NewsApi
import itamar.stern.news.models.News

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

    suspend fun fetchNews(language:String, category: String, offset: String = "0", limit: String = "100"){
        try{
            val news = newsApi.fetchMovies(language = language, category = category, offset = offset, limit = limit)
            when(category){
                "general" -> {
                    generalNewsList.postValue(news.data)
                }
                "business" -> {
                    businessNewsList.postValue(news.data)
                }
                "entertainment" -> {
                    entertainmentNewsList.postValue(news.data)
                }
                "health" -> {
                    healthNewsList.postValue(news.data)
                }
                "science" -> {
                    scienceNewsList.postValue(news.data)
                }
                "sports" -> {
                    sportsNewsList.postValue(news.data)
                }
                "technology" -> {
                    technologyNewsList.postValue(news.data)
                }
            }
        } catch (e: Exception){
            println(e)
            e.printStackTrace()
            println(e.message)
            println(e.localizedMessage)
        }



    }
}