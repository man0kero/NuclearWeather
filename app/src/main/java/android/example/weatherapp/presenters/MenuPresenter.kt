package android.example.weatherapp.presenters

import android.example.weatherapp.bussines.ApiProvider
import android.example.weatherapp.bussines.model.GeoCodeModel
import android.example.weatherapp.bussines.repos.MenuRepository
import android.example.weatherapp.bussines.repos.SAVED
import android.example.weatherapp.view.MenuView
import android.util.Log

class MenuPresenter : BasePresenter<MenuView>() {
    private val repo = MenuRepository(ApiProvider())

    override fun enable() {
        repo.dataEmitter.subscribe{
            viewState.setLoading(false)
            if(it.purpose == SAVED){
                Log.d("123321", "enable: SAVED ${it.data}")
                viewState.fillFavoriteList(it.data)
            }else{
                Log.d("123321", "enable: CURRENT ${it.data}")

                viewState.fillPredictionList(it.data)
            }
        }
    }

    fun searchFor(str: String){
        repo.getCities(str)
    }

    fun removeLocation(data: GeoCodeModel){
        repo.remove(data)
    }

    fun saveLocation(data: GeoCodeModel){
        repo.add(data)
    }

    fun getFavoriteList() {
        repo.updateFavorite()
    }
}