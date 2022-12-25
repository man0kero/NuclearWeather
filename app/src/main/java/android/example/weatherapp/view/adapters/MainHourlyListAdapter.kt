package android.example.weatherapp.view.adapters

import android.annotation.SuppressLint
import android.example.weatherapp.R
import android.example.weatherapp.bussines.model.HourlyWeatherModel
import android.example.weatherapp.view.*
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.google.android.material.card.MaterialCardView
import com.google.android.material.textview.MaterialTextView
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.StringBuilder


class MainHourlyListAdapter : BaseAdapter<HourlyWeatherModel>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HourlyViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_main_hourly, parent, false)
        return HourlyViewHolder(view)
    }



    inner class HourlyViewHolder(view : View) : BaseViewHolder(view){

        var time: MaterialTextView

        var temperature: MaterialTextView

        var popRate: MaterialTextView

        var icon: ImageView

        var card: MaterialCardView

       init {

            time = itemView.findViewById(R.id.item_hourly_time_tv)
            temperature = itemView.findViewById(R.id.item_hourly_temp_tv)
            popRate = itemView.findViewById(R.id.item_hourly_pop_tv)
            icon = itemView.findViewById(R.id.item_hourly_icon)
            card = itemView.findViewById(R.id.hourCard)

        }

        override fun bindView(position: Int) {

            if(position == 0){
                card.setBackgroundResource(R.drawable.bg_card_current)
                time.setTextColor(Color.WHITE)
                popRate.setTextColor(Color.WHITE)
                temperature.setTextColor(Color.WHITE)

            } else {
                card.setBackgroundResource(R.drawable.bg_card_hourly)
                time.setTextColor(Color.GRAY)
                popRate.setTextColor(Color.GRAY)
                temperature.setTextColor(Color.GRAY)
            }

            mData[position].apply {
                time.text = dt.toDateFormatOf(HOUR_DOUBLE_DOT_MINUTE)
                temperature.text = StringBuilder().append(temp.toDegree()).append("°").toString()
                popRate.text = pop.toPercentString(" %")
                icon.setImageResource(weather[0].icon.provideIcon())
            }
        }

    }

}