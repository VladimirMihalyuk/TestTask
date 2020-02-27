package com.example.testtask.today


import android.os.Bundle

import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.testtask.R
import com.example.testtask.activity.ActivityViewModel
import com.example.testtask.activity.MainActivity
import com.example.testtask.database.MyDatabase
import com.example.testtask.databinding.FragmentTodayBinding
import com.example.testtask.network.WeatherAPIClient
import com.example.testtask.repository.Repository
import com.google.android.material.snackbar.Snackbar


class TodayFragment : Fragment() {

    private lateinit var viewModel: TodayViewModel
    private lateinit var cityText: TextView


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

        cityText = binding.city

        viewModel.loading.observe(viewLifecycleOwner, Observer {loading ->
            if(loading){
                binding.loading.visibility = View.VISIBLE
            }else{
                binding.loading.visibility = View.INVISIBLE
            }

        })

        (activity as MainActivity).getCity().observe(viewLifecycleOwner, Observer {list ->
            if((activity as MainActivity).useLocation() == false){
                val city = list.firstOrNull()
                if( city  != null){
                    viewModel.getCurrentWeatherByCityName(city.name)
                } else {
                    showSnackbar("Please select city")
                }
            }
        })

        (activity as MainActivity).location().observe(viewLifecycleOwner, Observer {
            if((activity as MainActivity).useLocation() == true){
                it?.let{
                    viewModel.getCurrentWeatherByCoordinates(it.longitude.toFloat(),
                        it.latitude.toFloat())
                }
            }
        })

        viewModel.internetError.observe(viewLifecycleOwner, Observer {
            if(it == true){
                showSnackbar("Can't get information about this city")
                viewModel.resetErrorMessage()
            }
        })



        return binding.root
    }

    fun showSnackbar(text: String){
        Snackbar.make(cityText, text, Snackbar.LENGTH_SHORT).show()
    }

}
