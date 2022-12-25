package android.example.weatherapp.view.adapters

import android.annotation.SuppressLint
import android.example.weatherapp.R
import android.example.weatherapp.bussines.model.GeoCodeModel
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import com.google.android.material.textview.MaterialTextView
import java.util.*

class CityListAdapter : BaseAdapter<GeoCodeModel>() {

    lateinit var clickListener: SearchItemClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CitySearchViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_city_lists, parent, false)
        return CitySearchViewHolder(view)
    }

    interface SearchItemClickListener {

        fun addToFavorite(item: GeoCodeModel)

        fun removeFromFavorite(item: GeoCodeModel)

        fun showWeatherIn(item: GeoCodeModel)

    }

    inner class CitySearchViewHolder(view: View) : BaseViewHolder(view) {

        var mCity: MaterialTextView

        var mCountry: MaterialTextView

        var mFavorite: MaterialButton

        var mLocation: MaterialCardView

        var mState: MaterialTextView

        init {
            mCity = itemView.findViewById(R.id.search_city)
            mCountry = itemView.findViewById(R.id.search_country)
            mFavorite = itemView.findViewById(R.id.favorite)
            mLocation = itemView.findViewById(R.id.location)
            mState = itemView.findViewById(R.id.state)
        }

        override fun bindView(position: Int) {
            mLocation.setOnClickListener {
                clickListener.showWeatherIn(mData[position])
            }

            mFavorite.setOnClickListener {
                val item = mData[position]
                when ((it as MaterialButton).isChecked) {
                    true -> {
                        item.isFavorite = true
                        clickListener.addToFavorite(item)
                    }
                    false -> {
                        item.isFavorite = false
                        clickListener.removeFromFavorite(item)
                    }
                }
            }

            mData[position].apply {
                mState.text = if(!state.isNullOrEmpty())
                    itemView.context.getString(R.string.comma,state) else ""
                mCity.text = when (Locale.getDefault().displayLanguage) {
                    "русский" -> local_names.ru?: name
                    "English" -> local_names.en?: name
                    else -> "non"                }
                mCountry.text = Locale("", country).displayName
                mFavorite.isChecked = isFavorite
            }
        }
    }
}