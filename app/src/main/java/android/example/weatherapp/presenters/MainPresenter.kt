package android.example.weatherapp.presenters

import android.example.weatherapp.bussines.ApiProvider
import android.example.weatherapp.bussines.repos.MainRepository
import android.example.weatherapp.view.MainView
import android.util.Log

class MainPresenter : BasePresenter<MainView>() {

    private  val repo = MainRepository(ApiProvider())

    override fun enable() {
        repo.dataEmitter
            .doAfterNext { viewState.setLoading(false) }
            .subscribe { response ->
            Log.d("MAINREPO", "Presenter enable(): $response")
            viewState.displayLocation(response.cityName)
            viewState.displayCurrentData(response.weatherData)
            viewState.displayDailyData(response.weatherData.daily)
            viewState.displayHourlyData(response.weatherData.hourly)
            response.error?.let { viewState.displayError(response.error) }
            }
        }

    fun  refresh(lat: String, lon: String) {
        viewState.setLoading(true)
        repo.reloadData(lat, lon)
    }

}