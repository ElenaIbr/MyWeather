package com.ibraeva.myweather

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ibraeva.myweather.models.Daily
import com.ibraeva.myweather.models.DayShortInfo
import com.ibraeva.myweather.utilits.getDate
import com.ibraeva.myweather.utilits.getImage
import com.ibraeva.myweather.utilits.toCelsius

class WeekAdapter(var arrayList: MutableList<DayShortInfo>, val context: Context) :

    RecyclerView.Adapter<WeekAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun BindItems(day: DayShortInfo) {
            itemView.findViewById<TextView>(R.id.date_img).text = getDate(day.dt)
            itemView.findViewById<TextView>(R.id.temp_img).text = toCelsius(day.temp)
            //itemView.findViewById<TextView>(R.id.description).text = day.des
            itemView.findViewById<ImageView>(R.id.item_img).setImageResource(getImage(day.id))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater =
            LayoutInflater.from(parent.context).inflate(R.layout.day_item, parent, false)
        return ViewHolder(inflater)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.BindItems(arrayList[position])
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    fun updateAdapter(listArray: MutableList<DayShortInfo>) {
        arrayList = listArray
        notifyDataSetChanged()
    }
}
