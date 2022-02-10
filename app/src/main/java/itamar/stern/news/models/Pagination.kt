package itamar.stern.news.models

data class Pagination (
    val limit: Int,
    val offset: Int,
    val count: Int,
    val total: Int
)