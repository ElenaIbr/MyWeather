package com.ibraeva.myweather.models

data class Daily (
    val dt : Int,
    val sunrise : Int,
    val sunset : Int,
    val moonrise : Double,
    val moonset : Int,
    val moon_phase : Double,
    val temp : Temp,
    val feels_like : Feels_like,
    val pressure : Int,
    val humidity : Int,
    val dew_point : Double,
    val wind_speed : Double,
    val wind_deg : Int,
    val wind_gust : Double,
    val weather : List<Weather>,
    val clouds : Int,
    val pop : Double,
    val rain : Double = 0.0,
    val uvi : Double
)
