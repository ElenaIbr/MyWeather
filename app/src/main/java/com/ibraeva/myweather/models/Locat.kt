package com.ibraeva.myweather.models

data class Locat(
    val lat: Double,
    val lon: Double,
    val timezone: String = "",
    val timezone_offset: Int,
    val daily: List<Daily>
)




