package com.ibraeva.myweather

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.tabs.TabLayout
import com.google.gson.Gson
import com.ibraeva.myweather.models.DayShortInfo
import com.ibraeva.myweather.models.Locat
import com.ibraeva.myweather.utilits.appActivity
import com.ibraeva.myweather.utilits.getDate
import com.ibraeva.myweather.utilits.getImage
import com.ibraeva.myweather.utilits.toCelsius
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.net.HttpURLConnection
import java.net.URL
import kotlin.math.ceil

const val ARG_OBJECT = "object"

class NumberFragment : Fragment() {

    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private var adapterWeek: WeekAdapter? = null

    var recycle: RecyclerView? = null

    var itemList = MutableList(7) { DayShortInfo() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        return inflater.inflate(R.layout.fragment_number, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {


        arguments?.takeIf { it.containsKey(ARG_OBJECT) }?.apply {
            //val textView: TextView = view.findViewById(R.id.textView)
            //textView.text = getInt(ARG_OBJECT).toString()

            //val todayWeather: ConstraintLayout = view.findViewById(R.id.today_weather)



            if (getInt(ARG_OBJECT) == 1) {

                //todayWeather.visibility = View.VISIBLE
                //recycle?.visibility = View.GONE
            } else {

                //Log.d("R", "2")
                //todayWeather.visibility = View.GONE
                //recycle?.visibility = View.VISIBLE


            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        // Inflate the layout for this fragment

        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(appActivity)

        checkPermissions()


        recycle = appActivity.findViewById(R.id.recycle)
        recycle?.layoutManager = LinearLayoutManager(appActivity)
        adapterWeek = WeekAdapter(itemList, appActivity)
        recycle?.adapter = adapterWeek

        /*fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(appActivity)

        checkPermissions()*/

        //val recycle: RecyclerView = view.findViewById(R.id.recycle)




    }

    private fun checkPermissions() {
        if (ContextCompat.checkSelfPermission(
                appActivity,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                appActivity,
                arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                1
            )
        } else {
            getLocations()
        }
    }

    @SuppressLint("MissingPermission")
    private fun getLocations() {
        fusedLocationProviderClient.lastLocation.addOnSuccessListener {
            if (it == null) {
                Toast.makeText(appActivity, "Can`t get location", Toast.LENGTH_SHORT).show()
            } else {
                createRequest(it.latitude.toString(), it.longitude.toString())
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == 1) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(
                        appActivity,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    Toast.makeText(appActivity, "Permission granted", Toast.LENGTH_SHORT).show()
                    getLocations()
                } else {
                    Toast.makeText(appActivity, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun createRequest(latitude: String, longitude: String) {
        val o = Observable.create<String> {
            val urlConnection =
                URL("https://api.openweathermap.org/data/2.5/onecall?lat=${latitude}&lon=${longitude}&exclude=current,minutely,hourly&appid=86ba71f16b2a471617575a7e849aaba7").openConnection() as HttpURLConnection
            try {
                urlConnection.connect()
                if (urlConnection.responseCode != HttpURLConnection.HTTP_OK) {
                    it.onError(RuntimeException(urlConnection.responseMessage))
                } else {
                    val str = urlConnection.inputStream.bufferedReader().readText()
                    it.onNext(str)
                }
            } finally {
                urlConnection.disconnect()
            }

        }.map { result ->
            Gson().fromJson(result, Locat::class.java)
        }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())

        val result = o.subscribe(
            {
                appActivity.findViewById<TextView>(R.id.city_name).text = it.timezone
                appActivity.findViewById<TextView>(R.id.date).text = getDate(it.daily[0].dt)
                appActivity.findViewById<TextView>(R.id.currentTemp).text =
                    toCelsius(ceil(it.daily[0].temp.day))
                appActivity.findViewById<ImageView>(R.id.main_img)
                    .setImageResource(getImage(it.daily[0].weather[0].id))
                appActivity.findViewById<TextView>(R.id.description).text =
                    it.daily[0].weather[0].description

                appActivity.findViewById<TextView>(R.id.tempMorn).text =
                    "Morning\n" + toCelsius(it.daily[0].temp.morn)
                appActivity.findViewById<TextView>(R.id.tempDay).text =
                    "Day\n" + toCelsius(it.daily[0].temp.day)
                appActivity.findViewById<TextView>(R.id.tempEve).text =
                    "Evening\n" + toCelsius(it.daily[0].temp.eve)
                appActivity.findViewById<TextView>(R.id.tempNight).text =
                    "Night\n" + toCelsius(it.daily[0].temp.night)

                appActivity.findViewById<TextView>(R.id.wind).text =
                    it.daily[0].wind_speed.toString() + " m/sec"
                appActivity.findViewById<TextView>(R.id.pressure).text =
                    it.daily[0].pressure.toString() + " hPa"
                appActivity.findViewById<TextView>(R.id.humidity).text =
                    it.daily[0].humidity.toString() + " %"
                appActivity.findViewById<TextView>(R.id.cloudiness).text =
                    it.daily[0].clouds.toString() + " %"


                for (i in 0 until itemList.size) {
                    itemList[i].dt = it.daily[i].dt
                    itemList[i].temp = it.daily[i].temp.day
                    itemList[i].id = it.daily[i].weather[0].id
                    itemList[i].des = it.daily[i].weather[0].description
                }

            },
            {
                Log.d("RESULT", it.message.toString())
            })
    }


}