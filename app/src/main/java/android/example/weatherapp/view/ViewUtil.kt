package android.example.weatherapp.view

import android.example.weatherapp.R
import android.text.Editable
import android.text.TextWatcher
import com.google.android.material.textfield.TextInputEditText
import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.core.Flowable
import okhttp3.internal.Version
import java.lang.StringBuilder
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

const val DAY_FULL_MONTH_NAME = "dd MMMM"
const val DAY_WEEK_NAME_LONG = "dd EEEE"
const val HOUR_DOUBLE_DOT_MINUTE = "HH:mm"

fun Long.toDateFormatOf(format: String): String {
    val cal = Calendar.getInstance()
    val timeZone = cal.timeZone
    val sdf = SimpleDateFormat(format, Locale.getDefault())
    sdf.timeZone = timeZone
    return sdf.format(Date(this * 1000))
}

fun Double.toDegree() = SettingsHolder.temp.getValue(this)

fun Double.toPercentString(extraPart: String = "") = (this * 100).roundToInt().toString() + extraPart




fun Int.provideWindDir(): String = when(this){
    in 350..360 -> "N"
    in 1..10 -> "N"
    in 11..79 -> "NE"
    in 80..100 -> "E"
    in 101..169 -> "SE"
    in 170..190 -> "S"
    in 191..259 -> "SW"
    in 260..280 -> "W"
    in 281..349 -> "NW"
    else -> "W/O"
}

fun String.provideIcon(): Int = when(this) {
        "01n" -> R.drawable.ic_01n
        "01d" -> R.drawable.ic_01d
        "02n" -> R.drawable.ic_02n
        "02d" -> R.drawable.ic_02d
        //"03n", "04n" -> R.drawable.ic_03n
        "03d", "04d", "03n", "04n" -> R.drawable.ic_03d
        "09n", "10n", "09d", "10d" -> R.drawable.ic_09d
        "11n", "11d" -> R.drawable.ic_11d
        "13n", "13d" -> R.drawable.ic_13d
        "50n", "50d" -> R.drawable.ic_50d
    else -> R.drawable.ic_error
}

fun TextInputEditText.createObservable() : Flowable<String> {
    return Flowable.create({
        addTextChangedListener(object : SimpleTextWatcher() {
            override fun afterTextChanged(s: Editable?) {
                it.onNext(s.toString())
            }
        })
    }, BackpressureStrategy.BUFFER)
}

private abstract class SimpleTextWatcher : TextWatcher {
    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }

    override fun afterTextChanged(p0: Editable?) {
    }
}

