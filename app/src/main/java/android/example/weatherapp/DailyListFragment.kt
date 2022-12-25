package android.example.weatherapp

import android.example.weatherapp.bussines.model.DailyWeatherModel
import android.example.weatherapp.view.adapters.MainDailyListAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.TransitionInflater
import kotlinx.android.synthetic.main.fragment_daily_list.*

class DailyListFragment: DailyBaseFragment<List<DailyWeatherModel>>() {


    private val dailyListAdapter = MainDailyListAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val inflater = TransitionInflater.from(requireContext())
        exitTransition = inflater.inflateTransition(R.transition.slide_right)
        enterTransition = inflater.inflateTransition(R.transition.slide_out_right)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_daily_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dailyList.apply {
            adapter = dailyListAdapter
            layoutManager = LinearLayoutManager(view.context, LinearLayoutManager.VERTICAL, false)
            setHasFixedSize(true)
            dailyListAdapter.clickListener = clickListener
        }
        mData?.let { updateView() }
    }

    override fun updateView() {
        dailyListAdapter.updateData(mData!!)
    }

    private val clickListener = object : MainDailyListAdapter.DayItemClick{
        override fun showDetails(data: DailyWeatherModel) {
            val fragment = DailyInfoFragment()
            fragment.setData(data)
            fm.beginTransaction().replace(R.id.fragmentContainer, fragment).addToBackStack(null).commit()
        }
}
}