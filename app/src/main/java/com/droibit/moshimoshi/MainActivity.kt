package com.droibit.moshimoshi

import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import com.droibit.moshimoshi.entity.*
import com.droibit.moshimoshi.net.WeatherService
import com.squareup.moshi.Moshi
import retrofit.MoshiConverterFactory
import retrofit.Retrofit

class MainActivity : AppCompatActivity() {

    /** {@inheritDoc}  */
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
    private fun showArticleJson() {
        val moshi = Moshi.Builder().build()
        val adapter = moshi.adapter(Article::class.java)

        val article = Article(id = "10",
                              title = "MoshiMoshi",
                              author = Author(id = "1", name = "droibit"))
        val json = adapter.toJson(article)
        val restoreArticle = adapter.fromJson(json)

        (findViewById(R.id.text_json) as TextView).text = json + "\n" + restoreArticle.toString()
    }

    // JSONのキー値をカスタマイズ
    private fun showNamedArticleJson() {
        val moshi = Moshi.Builder().build()
        val adapter = moshi.adapter(NamedArticle::class.java)

        val article = NamedArticle(id = "11",
                                  title = "Named_MoshiMoshi",
                                  author = NamedAuthor(id = "1", name = "droibit"))
        val json = adapter.toJson(article)
        val restoreArticle = adapter.fromJson(json)

        (findViewById(R.id.text_custom_json) as TextView).text = json + "\n" + restoreArticle.toString()
    }

    private fun showArticleListJson() {
        val moshi = Moshi.Builder().build()
        val adapter = moshi.adapter(Articles::class.java)

        val articles = Articles.of(
                Article("12", "Moshi1", Author("1", "droibit")),
                Article("13", "Moshi2", Author("2", "droibit2")),
                Article("14", "Moshi3", Author("3", "droibit3"))
        )
        val json = adapter.toJson(articles)
        val restoreArticles = adapter.fromJson(json)

        (findViewById(R.id.text_list_json) as TextView).text = json + "\n" + restoreArticles.toString()
    }

    private fun showAdapterJson() {
        val moshi = Moshi.Builder()
                         .add(ArticleJsonAdapter())
                         .build()

        val articleJson = ArticleJson(
                articleId = "15",
                articleTitle = "Adapter_Moshi",
                authorId = "1",
                authorName = "droibit"
        )
        val articleJsonString = moshi.adapter(ArticleJson::class.java).toJson(articleJson)

        val adapter = moshi.adapter(Article::class.java)
        // ArticleJsonオブジェクトのJSONからArticleオブジェクトを生成
        val article = adapter.fromJson(articleJsonString)
        // ArticleオブジェクトからArticleJsonオブジェクトのJSONを生成
        val convertedArticleJsonString = adapter.toJson(article)

        (findViewById(R.id.text_adapter_json) as TextView).text = convertedArticleJsonString + "\n" + article.toString()
    }

    private fun showNotExistFieldJson() {
        val moshi = Moshi.Builder().build()
        val adapter = moshi.adapter(Article::class.java)

        val json = "{\"author\":{\"name\":\"droibit\"},\"id\":\"10\",\"title\":\"MoshiMoshi\"}";
        val restoreArticle = adapter.fromJson(json)

        (findViewById(R.id.text_not_exist_json) as TextView).text = json + "\n" + restoreArticle.toString()

        if (restoreArticle.author.id == null) {
            Toast.makeText(this, "article id == null", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showWeatherJson() {
        val moshi = Moshi.Builder().build()
        val retrofit = Retrofit.Builder()
                .baseUrl("http://weather.livedoor.com")
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .build()
        val service = retrofit.create(WeatherService::class.java)

        object: AsyncTask<Unit, Unit, Weather>() {
            override fun doInBackground(vararg params: Unit?): Weather? {
                // 東京の天気
                try {
                    val response = service.weather("130010").execute()
                    return response?.body()
                } catch (e: Exception) {
                    Log.e(BuildConfig.BUILD_TYPE, "Network Error: ", e)
                }
                return null
            }

            override fun onPostExecute(result: Weather?) {
                (findViewById(R.id.text_weather_json) as TextView).text = result?.toString()
            }
        }.execute()
    }
}
