package android.example.weatherapp

import android.app.Application
import android.content.Intent
import android.example.weatherapp.bussines.room.OpenWeatherDataBase
import android.example.weatherapp.view.SettingsHolder
import androidx.room.Room

const val APP_SETTINGS = "App_settings"
const val IS_STARTED_UP = "Is started up"

class App : Application() {


    companion object{
        lateinit var db: OpenWeatherDataBase
    }

    override fun onCreate() {
        super.onCreate()

        db = Room.databaseBuilder(this, OpenWeatherDataBase::class.java, "OpenWeatherDB")
            .fallbackToDestructiveMigration()
            .build()

        // TODO delete fallback before release

        val preferences = getSharedPreferences(APP_SETTINGS, MODE_PRIVATE)

        SettingsHolder.onCreate(preferences)


        val flag = preferences.contains(IS_STARTED_UP)

        if(!flag) {
            val editor = preferences.edit()
            editor.putBoolean(IS_STARTED_UP, true)
            editor.apply()
            val intent = Intent(this, InitialActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }
    }
}