package itamar.stern.news.models

data class NewsResponse (
    val pagination: Pagination,
    val data : List<News>
)