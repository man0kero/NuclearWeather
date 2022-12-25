package android.example.weatherapp.view

import android.example.weatherapp.bussines.model.DailyWeatherModel
import android.example.weatherapp.bussines.model.HourlyWeatherModel
import android.example.weatherapp.bussines.model.WeatherDataModel
import moxy.MvpView
import moxy.viewstate.strategy.alias.AddToEndSingle

interface MainView : MvpView {


    @AddToEndSingle
    fun displayLocation(data:String)

    @AddToEndSingle
    fun displayCurrentData (data: WeatherDataModel)

    @AddToEndSingle
    fun displayHourlyData (data:List<HourlyWeatherModel>)

    @AddToEndSingle
    fun displayDailyData (data:List<DailyWeatherModel>)

    @AddToEndSingle
    fun displayError(error: Throwable)

    @AddToEndSingle
    fun setLoading(flag:Boolean)
}