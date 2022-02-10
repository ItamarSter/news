package itamar.stern.news.api


import itamar.stern.news.BuildConfig
import itamar.stern.news.models.NewsResponse
import itamar.stern.news.ui.NewsApplication
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
const val BASE_URL = "http://api.mediastack.com/"
const val API_KEY = BuildConfig.NEWS_API_KEY



interface NewsApi {
    @GET("v1/news")
    suspend fun fetchMovies(
        @Query("access_key") api_key: String = API_KEY,
        @Query("languages") language: String = NewsApplication.LANGUAGE,
        @Query("categories") category: String = "general",
        @Query("limit") limit: String = "100",
        @Query("offset") offset: String = "0",
        @Query("sort") sort: String = "published_desc"
    ): NewsResponse

    companion object {
        fun create(): NewsApi {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(NewsApi::class.java)
        }
    }
}