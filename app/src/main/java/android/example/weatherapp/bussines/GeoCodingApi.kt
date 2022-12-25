package android.example.weatherapp.bussines

import android.example.weatherapp.bussines.model.GeoCodeModel
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface GeoCodingApi {

    @GET("geo/1.0/direct")
    fun getCityByName(
        @Query("q") name: String,
        @Query("limit") limit: String = "5",
        @Query("appid") id: String = "45d8067cc659e0c87a0c40905a9fe21f"
    ) : Observable<List<GeoCodeModel>>


    @GET("geo/1.0/reverse?")
    fun getCityByCord(
        @Query("lat") lat: String,
        @Query("lon") lon: String,
        @Query("limit") limit: String = "5",
        @Query("appid") id: String = "45d8067cc659e0c87a0c40905a9fe21f"
    ) : Observable<List<GeoCodeModel>>


}