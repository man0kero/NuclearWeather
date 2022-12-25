package android.example.weatherapp.view.adapters

import android.annotation.SuppressLint
import android.example.weatherapp.R
import android.example.weatherapp.bussines.model.DailyWeatherModel
import android.example.weatherapp.view.*
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.cardview.widget.CardView
import com.google.android.material.card.MaterialCardView
import com.google.android.material.textview.MaterialTextView
import java.lang.StringBuilder

class MainDailyListAdapter : BaseAdapter<DailyWeatherModel>() {


    lateinit var clickListener: DayItemClick


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DailyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_main_daily, parent, false)
        return DailyViewHolder(view)
    }


    interface DayItemClick{
        fun showDetails(data: DailyWeatherModel)
    }

    inner class DailyViewHolder(view: View) : BaseViewHolder(view){

        var container: CardView

        var date: MaterialTextView

        var popRate: MaterialTextView

        var maxTemp: MaterialTextView

        var minTemp: MaterialTextView

        var icon: ImageView


        init {

            container = itemView.findViewById(R.id.day_container)
            date = itemView.findViewById(R.id.item_daily_date_tv)
            popRate = itemView.findViewById(R.id.item_daily_percent)
            maxTemp = itemView.findViewById(R.id.item_daily_max_temp_tv)
            minTemp = itemView.findViewById(R.id.item_daily_min_temp_tv)
            icon = itemView.findViewById(R.id.item_daily_icon)
        }

        override fun bindView(position: Int) {
            val itemData = mData[position]
            if(position == 0){
                container.setBackgroundResource(R.drawable.bg_card_current)
                date.setTextColor(Color.WHITE)
                popRate.setTextColor(Color.WHITE)
                maxTemp.setTextColor(Color.WHITE)
                minTemp.setTextColor(Color.WHITE)
            } else {
                container.setBackgroundResource(R.drawable.bg_card_hourly)
                date.setTextColor(Color.GRAY)
                popRate.setTextColor(Color.GRAY)
                maxTemp.setTextColor(Color.GRAY)
                minTemp.setTextColor(Color.GRAY)
            }

            container.setOnClickListener{clickListener.showDetails(itemData)}

            if(mData.isNotEmpty()){
                itemData.apply {
                    val dataOfDay = dt.toDateFormatOf(DAY_WEEK_NAME_LONG)
                    date.text = if(dataOfDay.startsWith("0", true))
                    dataOfDay.removePrefix("0")
                    else dataOfDay

                    if(pop < 0.01) {
                        popRate.visibility = View.INVISIBLE
                    } else {
                        popRate.visibility = View.VISIBLE
                        popRate.text = pop.toPercentString("%")
                    }
                    icon.setImageResource(weather[0].icon.provideIcon())
                    maxTemp.text = StringBuilder().append(temp.max.toDegree()).append("°").toString()
                    minTemp.text = StringBuilder().append(temp.min.toDegree()).append("°").toString()
                }
            }
        }
    }
}