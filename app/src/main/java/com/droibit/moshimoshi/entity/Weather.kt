package com.droibit.moshimoshi.entity

/**
 * livedoor[お天気Webサービス](http://weather.livedoor.com/weather_hacks/webservice)
 *
 * @author kumagai
 *
 * @property location 予報を発表した地域
 * @property title タイトル
 * @property link 天気予報のURL
 * @property publicTime 天気予報発表時刻
 * @property description 天気概況文
 * @property forecasts 都府県天気予報の予報日毎の配列
 * @property copyright コピーライト
 */
data class Weather(
        val location: Location,
        val title: String,
        val link: String,
        val publicTime: String,
        val description: Description,
        val forecasts: List<Forecast>,
        val copyright: Copyright
)

/**
 * @property area 地方名（九州地方）
 * @property pref 都道府県名（福岡県）
 * @property city 一時細分区名（八幡）
 */
data class Location(
        val area: String,
        val pref: String,
        val city: String
)

/**
 * @property text 天気概況文
 * @property publicTime 発表時刻
 */
data class Description(
        val text: String,
        val publicTime: String
)

/**
 * @property date 予報日
 * @property dateLabel 予報日(今日、明日、明後日のいずれか)
 * @property telop 天気（晴れ、曇り、雨など）
 * @property image 天気アイコン
 * @property min 最高気温
 * @property max 最低気温
 */
data class Forecast(
        val date: String,
        val dateLabel: String,
        val telop: String,
        val image: Image,
        val min: Temperature,
        val max: Temperature
)

/**
 * @property title 天気（晴れ、曇り、雨など）
 * @property link 天気情報のURL
 * @property url 天気アイコンのURL
 * @property width 天気アイコンの幅
 * @property height 天気アイコンの高さ
 */
data class Image(
        val title: String,
        val link: String,
        val url: String,
        val width: Int,
        val height: Int
)

/**
 * @property celsius 摂氏
 * @property fahrenheit 華氏
 */
data class Temperature(
        val celsius: String,
        val fahrenheit: String
)

/**
 * @property link 天気情報のURL
 * @property name 市区町村名
 */
data class PinpointLocation(
        val link: String,
        val name: String
)

/**
 * @property provider livedoor 天気情報で使用している気象データの配信元
 * @property link livedoor 天気情報のURL
 * @property title コピーライトの文言
 * @property image livedoor 天気情報へのURL、アイコンなど
 */
data class Copyright(
        val provider: List<Provider>,
        val link: String,
        val title: String,
        val image: Image
)

/**
 * @property link 気象データの配信元のURL
 * @property name 気象データの配信元の名前
 */
data class Provider(
        val link: String,
        val name: String
)