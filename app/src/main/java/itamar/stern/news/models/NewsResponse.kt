package itamar.stern.news.models
//Classes for the response from the api
data class NewsResponse (
    val pagination: Pagination,
    //The news
    val data : List<News>
)

data class Pagination (
    val limit: Int,
    val offset: Int,
    val count: Int,
    val total: Int
)