package com.ibraeva.myweather.utilits

import android.app.Activity
import com.ibraeva.myweather.MainActivity
import com.ibraeva.myweather.R
import java.text.SimpleDateFormat
import java.util.*

lateinit var appActivity: MainActivity

fun getImage(id: Int): Int {

    val code = id.toString().first()
    var image = 0

    when (code) {
        '2' -> image = R.drawable.thunder
        '3' -> image = R.drawable.drizzle
        '5' -> image = R.drawable.rainy
        '6' -> image = R.drawable.snow
        '7' -> image = R.drawable.fog
    }

    when(id) {
        800 -> image = R.drawable.clear
        801 -> image = R.drawable.cloudy_day
        802 -> image = R.drawable.cloudy_day
        803 -> image = R.drawable.cloudy
        804 -> image = R.drawable.cloudy
    }

    return image
}
fun toCelsius(kelvin: Double): String {
    return if (kelvin < 0) "-" + (kelvin - 273.15).toInt().toString() + "℃"
    else (kelvin - 273.15).toInt().toString() + "℃"
}

fun getDate(dt: Int): String {
    val sdf = SimpleDateFormat("EEEE, MMMM d", Locale.ENGLISH)
    val date = Date(dt.toLong() * 1000)
    return sdf.format(date)
}


