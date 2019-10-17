package com.droibit.moshimoshi.entity

import androidx.annotation.Keep
import com.squareup.moshi.FromJson
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.squareup.moshi.ToJson

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

@Keep
data class FlatArticle(
  val articleId: String,
  val articleTitle: String,
  val authorId: String,
  val authorName: String
)

class ArticleJsonAdapter {

  @FromJson
  fun articleFromJson(json: FlatArticle): Article {
    return Article(
        id = json.articleId,
        title = json.articleTitle,
        author = Author(json.authorId, name = json.authorName)
    )
  }

  @ToJson
  fun articleToJson(article: Article): FlatArticle {
    return FlatArticle(
        articleId = article.id,
        articleTitle = article.title,
        authorId = article.author.id,
        authorName = article.author.name
    )
  }
}

@JsonClass(generateAdapter = true)
data class CodegenArticle(
  val id: String,
  val title: String,
  val author: CodegenAuthor,
  val nested: Nested
) {

  @JsonClass(generateAdapter = true)
  data class Nested(val value: String)
}