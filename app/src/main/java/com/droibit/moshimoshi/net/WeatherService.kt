package com.droibit.moshimoshi.net

import com.droibit.moshimoshi.entity.Weather
import retrofit.Call
import retrofit.http.GET
import retrofit.http.Path
import retrofit.http.Query

/**
 * @author kumagai
 */
interface WeatherService {

    @GET("/forecast/webservice/json/v1")
    fun weather(@Query("city") city: String): Call<Weather>
}