package android.example.weatherapp.bussines

import android.example.weatherapp.bussines.model.WeatherDataModel
import io.reactivex.rxjava3.core.Observable
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
    @GET("data/3.0/onecall?")
    fun getWeatherForecast(
        @Query("lat") lat: String,
        @Query("lon") lon: String,
        @Query("exclude") exclude: String = "minutely,alerts",
        @Query("appid") apiid: String = "45d8067cc659e0c87a0c40905a9fe21f",
        @Query("lang") lang: String = "en"
    ) : Observable<WeatherDataModel>
}