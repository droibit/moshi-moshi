package com.droibit.moshimoshi

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import com.droibit.moshimoshi.adapter.UriAdapter
import com.droibit.moshimoshi.entity.Article
import com.droibit.moshimoshi.entity.ArticleJsonAdapter
import com.droibit.moshimoshi.entity.Articles
import com.droibit.moshimoshi.entity.Author
import com.droibit.moshimoshi.entity.CodegenArticle
import com.droibit.moshimoshi.entity.NamedArticle
import com.droibit.moshimoshi.entity.NamedAuthor
import com.droibit.moshimoshi.entity.Weather
import com.droibit.moshimoshi.net.WeatherService
import com.squareup.moshi.JsonDataException
import com.squareup.moshi.Moshi
import kotlinx.android.synthetic.main.activity_main.codegenJson
import kotlinx.android.synthetic.main.activity_main.textAdapterJson
import kotlinx.android.synthetic.main.activity_main.textCustomJson
import kotlinx.android.synthetic.main.activity_main.textJson
import kotlinx.android.synthetic.main.activity_main.textListJson
import kotlinx.android.synthetic.main.activity_main.textNotExistJson
import kotlinx.android.synthetic.main.activity_main.textWeatherJson
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class MainActivity : AppCompatActivity() {

  private val moshi = Moshi.Builder()
      .build()

  private val service: WeatherService by lazy {
    val moshi = moshi.newBuilder()
        .add(UriAdapter.FACTORY)
        .build()
    Retrofit.Builder()
        .baseUrl("http://weather.livedoor.com")
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()
        .create(WeatherService::class.java)
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    showArticleJson()
    showNamedArticleJson()
    showArticleListJson()
    showArticleJsonWithAdapter()
    showNotExistFieldJson()
    showCodegenArticleJson()
    showWeatherJson()
  }

  @SuppressLint("SetTextI18n")
  private fun showArticleJson() {
    val adapter = moshi.adapter(Article::class.java)
        .indent("  ")

    val article = Article(
        id = "10",
        title = "MoshiMoshi",
        author = Author(id = "1", name = "droibit")
    )
    val json = adapter.toJson(article)
    val restoreArticle: Article = adapter.fromJson(json)!!
    check(article == restoreArticle)

    textJson.text = "$json\n$restoreArticle"
  }

  @SuppressLint("SetTextI18n")
  private fun showNamedArticleJson() {
    val adapter = moshi.adapter(NamedArticle::class.java)
        .indent("  ")
    val article = NamedArticle(
        id = "11",
        title = "Named_MoshiMoshi",
        author = NamedAuthor(id = "1", name = "droibit")
    )
    val json = adapter.toJson(article)
    val restoreArticle: NamedArticle = adapter.fromJson(json)!!
    check(article == restoreArticle)

    textCustomJson.text = "$json\n$restoreArticle"
  }

  @SuppressLint("SetTextI18n")
  private fun showArticleListJson() {
    val adapter = moshi.adapter(Articles::class.java)
        .indent("  ")

    val articles = Articles.of(
        Article("12", "Moshi1", Author("1", "droibit")),
        Article("13", "Moshi2", Author("2", "droibit2")),
        Article("14", "Moshi3", Author("3", "droibit3"))
    )
    val json = adapter.toJson(articles)
    val restoreArticles: Articles = adapter.fromJson(json)!!
    check(articles == restoreArticles)

    textListJson.text = "$json\n$restoreArticles"
  }

  @SuppressLint("SetTextI18n")
  private fun showArticleJsonWithAdapter() {
    val moshi = moshi.newBuilder()
        .add(ArticleJsonAdapter())
        .build()

    val articleJsonString =
      "{\"articleId\":\"15\",\"articleTitle\":\"Adapter_Moshi\",\"authorId\":\"1\",\"authorName\":\"droibit\"}"

    val adapter = moshi.adapter(Article::class.java)
        .indent("  ")
    val article: Article = adapter.fromJson(articleJsonString)!!
    val convertedArticleJsonString = adapter.toJson(article)

    textAdapterJson.text = "$convertedArticleJsonString\n$article"
  }

  @SuppressLint("SetTextI18n")
  private fun showNotExistFieldJson() {
    val adapter = moshi.adapter(Article::class.java)
        .indent("  ")

    val json = "{\"author\":{\"name\":\"droibit\"},\"title\":\"MoshiMoshi\"}"
    val restoreArticle: Article = adapter.fromJson(json)!!

    textNotExistJson.text = "${JSONObject(json).toString(2)}\n$restoreArticle"
    Log.e(BuildConfig.BUILD_TYPE, "article id == null")
  }

  @SuppressLint("SetTextI18n")
  private fun showCodegenArticleJson() {
    val adapter = moshi.adapter(CodegenArticle::class.java)
        .indent("  ")

    try {
      val invalidJson = "{\"author\":{\"name\":\"droibit\"},\"title\":\"MoshiMoshi\", \"nested\": {\"value\": \"nested\"}}"
      adapter.fromJson(invalidJson)!!
    } catch (e: JsonDataException) {
      Log.e(BuildConfig.BUILD_TYPE, e.toString())
    }

    val validJson =
      "{\"title\":\"MoshiMoshi\",\"id\":\"10\",\"author\":{\"id\":\"2\",\"name\":\"droibit\"}, \"nested\": {\"value\": \"nested\"}}"
    val restoreArticle: CodegenArticle = adapter.fromJson(validJson)!!
    codegenJson.text = "${JSONObject(validJson).toString(2)}\n$restoreArticle"
  }

  private fun showWeatherJson() {
    service.weather("130010")
        .enqueue(object : Callback<Weather> {
          override fun onFailure(
            call: Call<Weather>,
            t: Throwable
          ) {
            Log.e(BuildConfig.BUILD_TYPE, "", t)
            textWeatherJson.text = t.toString()
          }

          override fun onResponse(
            call: Call<Weather>,
            response: Response<Weather>
          ) {
            textWeatherJson.text = response.body()!!.toString()
          }
        })
  }
}
