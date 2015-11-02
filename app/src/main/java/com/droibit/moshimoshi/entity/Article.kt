package com.droibit.moshimoshi.entity


import com.squareup.moshi.FromJson
import com.squareup.moshi.Json
import com.squareup.moshi.ToJson

/**
 * @author kumagai
 */
data class Article(
        val id: String,
        val title: String,
        val author: Author
)

data class NamedArticle(
        @Json(name = "articleId") val id: String,
                                  val title: String,
                                  val author: NamedAuthor
)

data class Articles(val articles: List<Article>) {
    companion object {
        fun of(vararg articles: Article) = Articles(listOf(*articles))
    }
}

data class ArticleJson(
        val articleId: String,
        val articleTitle: String,
        val authorId: String,
        val authorName: String
)

class ArticleJsonAdapter {

    @FromJson
    fun articleFromJson(json: ArticleJson): Article {
        return Article(
                id = json.articleId,
                title = json.articleTitle,
                author = Author(json.authorId, name = json.authorName)
        )
    }

    @ToJson
    fun articleToJson(article: Article): ArticleJson {
        return ArticleJson(
                articleId = article.id,
                articleTitle = article.title,
                authorId = article.author.id,
                authorName = article.author.name
        )
    }
}