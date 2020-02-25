package com.example.testtask.today


import android.os.Bundle

import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.testtask.R
import com.example.testtask.database.MyDatabase
import com.example.testtask.databinding.FragmentTodayBinding
import com.example.testtask.network.WeatherAPIClient
import com.example.testtask.repository.Repository



class TodayFragment : Fragment() {

    private lateinit var viewModel: TodayViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding:FragmentTodayBinding =
            DataBindingUtil.inflate(inflater,
                R.layout.fragment_today, container, false)
        val application = requireNotNull(this.activity).application

        val client = WeatherAPIClient.getClient()
        val database = MyDatabase.getInstance(application).databaseDao
        val repository = Repository.getInstance(client, database)

        val viewModelFactory = TodayViewModelFactory(repository, application)

        viewModel = ViewModelProvider(this, viewModelFactory).get(TodayViewModel::class.java)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        viewModel.getCurrentWeatherByCityName()
        viewModel.getCurrentWeatherByCoordinates()


        return binding.root
    }


}
