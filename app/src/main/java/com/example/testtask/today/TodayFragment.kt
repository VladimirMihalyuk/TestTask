package com.example.testtask.today


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.testtask.R
import com.example.testtask.database.MyDatabase
import com.example.testtask.network.WeatherAPIClient
import com.example.testtask.repository.Repository
import com.example.testtask.utils.isInternetAvailable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class TodayFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val context = requireNotNull(context)
        val client = WeatherAPIClient.getClient()
        val database = MyDatabase.getInstance(context).databaseDao
        val repository = Repository.getInstance(client, database)
        GlobalScope.launch(Dispatchers.IO) {
            val today = repository.getCurrentWeatherByCoordinates(27.567444F, 53.893009F){
                isInternetAvailable(context)
            }
            Log.d("WTF", "Geolocation:$today")
            val todayCity = repository.getCurrentWeatherByCityName("Minsk"){
                isInternetAvailable(context)
            }
            Log.d("WTF", "City:$todayCity")
        }


        return inflater.inflate(R.layout.fragment_today, container, false)
    }


}
