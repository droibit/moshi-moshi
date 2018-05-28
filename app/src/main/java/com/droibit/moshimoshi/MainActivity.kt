package com.droibit.moshimoshi

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.droibit.moshimoshi.adapter.UriAdapter
import com.droibit.moshimoshi.entity.Article
import com.droibit.moshimoshi.entity.ArticleJsonAdapter
import com.droibit.moshimoshi.entity.Articles
import com.droibit.moshimoshi.entity.Author
import com.droibit.moshimoshi.entity.NamedArticle
import com.droibit.moshimoshi.entity.NamedAuthor
import com.droibit.moshimoshi.entity.Weather
import com.droibit.moshimoshi.net.WeatherService
import com.squareup.moshi.Moshi
import kotlinx.android.synthetic.main.activity_main.textAdapterJson
import kotlinx.android.synthetic.main.activity_main.textCustomJson
import kotlinx.android.synthetic.main.activity_main.textJson
import kotlinx.android.synthetic.main.activity_main.textListJson
import kotlinx.android.synthetic.main.activity_main.textNotExistJson
import kotlinx.android.synthetic.main.activity_main.textWeatherJson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class MainActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    showArticleJson()
    showNamedArticleJson()
    showArticleListJson()
    showAdapterJson()
    showNotExistFieldJson()
    showWeatherJson()
  }

  // JSONへの変換および復元
  @SuppressLint("SetTextI18n")
  private fun showArticleJson() {
    val moshi = Moshi.Builder()
        .build()
    val adapter = moshi.adapter(Article::class.java)

    val article = Article(
        id = "10",
        title = "MoshiMoshi",
        author = Author(id = "1", name = "droibit")
    )
    val json = adapter.toJson(article)
    val restoreArticle = adapter.fromJson(json)

    assert(article == restoreArticle)

    textJson.text = "$json\n$restoreArticle"
  }

  // JSONのキー値をカスタマイズ
  @SuppressLint("SetTextI18n")
  private fun showNamedArticleJson() {
    val moshi = Moshi.Builder()
        .build()
    val adapter = moshi.adapter(NamedArticle::class.java)

    val article = NamedArticle(
        id = "11",
        title = "Named_MoshiMoshi",
        author = NamedAuthor(id = "1", name = "droibit")
    )
    val json = adapter.toJson(article)
    val restoreArticle = adapter.fromJson(json)

    assert(article == restoreArticle)

    textCustomJson.text = "$json\n$restoreArticle"
  }

  @SuppressLint("SetTextI18n")
  private fun showArticleListJson() {
    val moshi = Moshi.Builder()
        .build()
    val adapter = moshi.adapter(Articles::class.java)

    val articles = Articles.of(
        Article("12", "Moshi1", Author("1", "droibit")),
        Article("13", "Moshi2", Author("2", "droibit2")),
        Article("14", "Moshi3", Author("3", "droibit3"))
    )
    val json = adapter.toJson(articles)
    val restoreArticles = adapter.fromJson(json)

    assert(articles == restoreArticles)

    textListJson.text = "$json\n$restoreArticles"
  }

  @SuppressLint("SetTextI18n")
  private fun showAdapterJson() {
    val moshi = Moshi.Builder()
        .add(ArticleJsonAdapter())
        .build()

    val articleJsonString =
      "{\"articleId\":\"15\",\"articleTitle\":\"Adapter_Moshi\",\"authorId\":\"1\",\"authorName\":\"droibit\"}";

    val adapter = moshi.adapter(Article::class.java)
    // ArticleJsonオブジェクトのJSONからArticleオブジェクトを生成
    val article = adapter.fromJson(articleJsonString)
    // ArticleオブジェクトからArticleJsonオブジェクトのJSONを生成
    val convertedArticleJsonString = adapter.toJson(article)

    assert(articleJsonString == convertedArticleJsonString)

    textAdapterJson.text = "$convertedArticleJsonString\n$article"
  }

  @SuppressLint("SetTextI18n")
  private fun showNotExistFieldJson() {
    val moshi = Moshi.Builder()
        .build()
    val adapter = moshi.adapter(Article::class.java)

    val json = "{\"author\":{\"name\":\"droibit\"},\"id\":\"10\",\"title\":\"MoshiMoshi\"}";
    val restoreArticle = adapter.fromJson(json)

    textNotExistJson.text = "$json\n$restoreArticle"

    if (restoreArticle?.author?.id == null) {
      Toast.makeText(this, "article id == null", Toast.LENGTH_SHORT)
          .show()
    }
  }

  private fun showWeatherJson() {
    val moshi = Moshi.Builder()
        .add(UriAdapter.FACTORY)
        .build()
    val retrofit = Retrofit.Builder()
        .baseUrl("http://weather.livedoor.com")
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()
    val service = retrofit.create(WeatherService::class.java)

    service.weather("130010")
        .enqueue(object : Callback<Weather> {
          override fun onFailure(
            call: Call<Weather>,
            t: Throwable
          ) {
            Log.e(BuildConfig.BUILD_TYPE, "", t)
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
