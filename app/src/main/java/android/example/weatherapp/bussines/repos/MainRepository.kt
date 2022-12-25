package android.example.weatherapp.bussines.repos

import android.util.Log
import com.google.gson.Gson
import android.example.weatherapp.bussines.ApiProvider
import android.example.weatherapp.bussines.model.WeatherDataModel
import android.example.weatherapp.bussines.room.WeatherDataEntity
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.*

const val TAG = "MAINREPO"

class MainRepository(api: ApiProvider) : BaseRepository<MainRepository.ServerResponse>(api) {
    private val gson = Gson()
    private val dbAccess = db.getWeatherDao()
    private val defLanguage = when (Locale.getDefault().displayLanguage) {
        "русский" -> "ru"
        else -> "en"
    }

    fun reloadData(lat: String, lon: String){
        Observable.zip(
            api.provideWeatherApi().getWeatherForecast(lat, lon, lang = defLanguage),
            api.provideGeoCodeApi().getCityByCord(lat, lon).map {
                it.asSequence().map { model ->
                when (Locale.getDefault().displayLanguage) {
                    "русский" -> model.local_names.ru
                    "English" -> model.local_names.en
                    else -> model.name
                }
            }
                .toList()
                .filterNotNull()
                .first()
            },
            { weatherData, geoCode -> ServerResponse(geoCode, weatherData)}
        )
            .subscribeOn(Schedulers.io())
            .doOnNext {
                dbAccess.insertWeatherData(
                    WeatherDataEntity(
                        data = gson.toJson(it.weatherData),
                        city = it.cityName
                    )
                )
            }
            .onErrorResumeNext {
                Observable.just(
                    ServerResponse(
                    dbAccess.getWeatherData().city,
                    gson.fromJson(dbAccess.getWeatherData().data, WeatherDataModel::class.java),
                    it
                    )
                )
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                dataEmitter.onNext(it)
                }, {
                    Log.d(TAG, "reloadData: $it")
                })

    }



    data class ServerResponse(
        val cityName: String,
        val weatherData: WeatherDataModel,
        val error: Throwable? = null
    )
}