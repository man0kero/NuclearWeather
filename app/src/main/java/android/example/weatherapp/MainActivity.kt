package android.example.weatherapp

import android.example.weatherapp.view.adapters.MainDailyListAdapter
import android.example.weatherapp.view.adapters.MainHourlyListAdapter
import android.location.Location
import android.os.Bundle
import android.annotation.SuppressLint
import android.content.Intent
import android.content.IntentSender
import android.example.weatherapp.bussines.model.DailyWeatherModel
import android.example.weatherapp.bussines.model.HourlyWeatherModel
import android.example.weatherapp.bussines.model.WeatherDataModel
import android.example.weatherapp.presenters.MainPresenter
import android.example.weatherapp.view.*
import android.graphics.Point
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.android.synthetic.main.activity_main.*
import moxy.MvpAppCompatActivity
import moxy.ktx.moxyPresenter
import java.lang.StringBuilder
import kotlin.math.roundToInt


const val TAG = "GEO_TEST"
const val COORDINATES = "Coordinates"


class MainActivity : MvpAppCompatActivity(), MainView {

    private val mainPresenter by moxyPresenter{ MainPresenter() }
    private val geoServices by lazy { LocationServices.getFusedLocationProviderClient(this) }
    private val tokenSource: CancellationTokenSource = CancellationTokenSource()
    private val locationRequest by lazy {
        LocationRequest.create().apply {
            interval = 600000
            fastestInterval = 5000
            priority = Priority.PRIORITY_HIGH_ACCURACY
        }
    }


    private val geoCallBack = object : LocationCallback() {
        override fun onLocationResult(geo: LocationResult) {
            Log.d(TAG, "onLocationResult: ${geo.locations.size}")
            mLocation = geo.locations[0]
            mainPresenter.refresh(mLocation.latitude.toString(), mLocation.longitude.toString())
        }
    }

    private lateinit var mLocation : Location

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initBottomSheets()
        initSwipeRefresh()

        refresh.isRefreshing = true

        supportFragmentManager.beginTransaction().add(R.id.fragmentContainer,
            DailyListFragment(),
            DailyListFragment::class.simpleName)
            .commit()

        if (!intent.hasExtra(COORDINATES)) {
            checkGeoAvailability()
            Log.d(TAG, "onCreate: ")
            getGeo()
        } else {
            val coord = intent.extras!!.getBundle(COORDINATES)!!
            val loc = Location("")
            loc.latitude = coord.getString("lat")!!.toDouble()
            loc.longitude = coord.getString("lon")!!.toDouble()
            mLocation = loc
            mainPresenter.refresh(
                lat = mLocation.latitude.toString(),
                lon = mLocation.longitude.toString()
            )
        }

        main_settings_btn.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_right, android.R.anim.fade_out)
        }

        main_menu_btn.setOnClickListener {
            val intent = Intent(this, MenuActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_left, android.R.anim.fade_out)
        }

        main_hourly_list.apply {
            adapter = MainHourlyListAdapter()
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
        }

        mainPresenter.enable()

        geoServices.requestLocationUpdates(locationRequest, geoCallBack, null)
    }

    // --------------- moxy code ----------------

    override fun displayLocation(data: String) {
        main_city_name_tv.text = data
    }

    @SuppressLint("ResourceType")
    override fun displayCurrentData(data: WeatherDataModel) {

        load()

        data.apply {
            main_date_tv.text = current.dt.toDateFormatOf(DAY_FULL_MONTH_NAME)
            main_weather_image.setImageResource(current.weather[0].icon.provideIcon())
            weather_info.text = current.weather[0].description.replaceFirstChar(Char::titlecase)
            main_temp.text = StringBuilder().append(current.temp.toDegree()).append("°").toString()
            main_min_t.text = StringBuilder().append(daily[0].temp.min.toDegree()).append("°").toString()
            main_feels_t.text = StringBuilder().append(current.feels_like.toDegree()).append("°").toString()
            main_max_t.text = StringBuilder().append(daily[0].temp.max.toDegree()).append("°").toString()
            val windSpeedSet = SettingsHolder.windSpeed
            main_wind_speed_mu_tv.text = getString(
                windSpeedSet.mesureUnitStringRes,
                windSpeedSet.getValue(current.wind_speed)
            )
            main_humidity_mu_tv.text = daily[0].pop.toPercentString("%")
            main_sunrise_mu_tv.text = current.sunrise.toDateFormatOf(HOUR_DOUBLE_DOT_MINUTE)
            main_sunset_mu_tv.text = current.sunset.toDateFormatOf(HOUR_DOUBLE_DOT_MINUTE)
        }

    }

    override fun displayHourlyData(data: List<HourlyWeatherModel>) {
        (main_hourly_list.adapter as MainHourlyListAdapter).updateData(data)
    }

    override fun displayDailyData(data: List<DailyWeatherModel>) {
        (supportFragmentManager.findFragmentByTag(DailyListFragment::class.simpleName)
        as DailyListFragment).setData(data)
    }

    override fun displayError(error: Throwable) {
        Toast.makeText(this,error.message,Toast.LENGTH_SHORT).show()
    }

    override fun setLoading(flag: Boolean) {
        refresh.isRefreshing = flag
    }

    fun load(){
        loading_data.visibility = View.INVISIBLE
        main_layout.visibility = View.VISIBLE
        main_bottom_sheets.visibility = View.VISIBLE
    }

    // --------------- moxy code ----------------

    //-------------- geo -------------------
    @SuppressLint("MissingPermission")
    private fun getGeo(){
        geoServices
            .getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, tokenSource.token)
            .addOnSuccessListener {
                Log.d(TAG, "getGeo: ")
                if(it!=null){
                    mLocation = it
                    mainPresenter.refresh(mLocation.latitude.toString(), mLocation.longitude.toString())
                }else{
                    displayError(Exception("Geodata is not available"))
                }
                Log.d(TAG, "requestGeo: $it")
            }
    }

    private fun checkGeoAvailability() {
        Log.d(TAG, "checkGeoAvailability: ")
        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
        val client = LocationServices.getSettingsClient(this)
        val task = client.checkLocationSettings(builder.build())
        task.addOnFailureListener { exception ->
            if (exception is ResolvableApiException) {
                try {
                    exception.startResolutionForResult(this, 100)
                } catch (sendEx: IntentSender.SendIntentException) {

                }
            }
        }
    }
    //-------------- geo -------------------

    private fun initBottomSheets() {
        main_bottom_sheets.isNestedScrollingEnabled = true
        val size = Point()
        windowManager.defaultDisplay.getSize(size)
        main_bottom_sheets_container.layoutParams =
            CoordinatorLayout.LayoutParams(size.x, (size.y * 0.6).roundToInt())
    }

    @SuppressLint("ResourceAsColor")
    private fun initSwipeRefresh() {
        refresh.apply {
            setColorSchemeResources(R.color.black)
            setProgressViewEndTarget(false, 280)
            setOnRefreshListener {
                mainPresenter.refresh(mLocation.latitude.toString(), mLocation.longitude.toString())
            }
        }
    }
}