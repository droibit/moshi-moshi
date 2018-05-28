package com.droibit.moshimoshi.net

import com.droibit.moshimoshi.entity.Weather
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {

  @GET("/forecast/webservice/json/v1")
  fun weather(@Query("city") city: String): Call<Weather>
}