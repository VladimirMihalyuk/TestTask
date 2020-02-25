package com.example.testtask.today


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.testtask.R
import com.example.testtask.network.WeatherAPIClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass.
 */
class TodayFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val client = WeatherAPIClient.getClient()
        GlobalScope.launch(Dispatchers.IO) {
            Log.d("WTF", "${client.getCurrentWeatherByCityName("Minsk").await()}")
        }


        return inflater.inflate(R.layout.fragment_today, container, false)
    }


}
