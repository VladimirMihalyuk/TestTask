package com.example.testtask.cities


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.testtask.activity.MainActivity
import com.example.testtask.R
import com.example.testtask.database.City
import com.example.testtask.database.MyDatabase
import com.example.testtask.network.WeatherAPIClient
import com.example.testtask.repository.Repository
import kotlinx.android.synthetic.main.fragment_cities.*
import kotlinx.android.synthetic.main.fragment_cities.view.*

/**
 * A simple [Fragment] subclass.
 */
class CitiesFragment : Fragment() {

    lateinit var viewModel: CitiesViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_cities, container, false)

        val application = requireNotNull(this.activity).application

        val client = WeatherAPIClient.getClient()
        val database = MyDatabase.getInstance(application).databaseDao
        val repository = Repository.getInstance(client, database)

        val viewModelFactory = CitiesViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(CitiesViewModel::class.java)

        val adapter = CitiesAdapter({ city: City, isSelected: Boolean ->
            viewModel.selectCity(city,isSelected )},
            {city: City -> viewModel.deleteCity(city)} )

        view.list.adapter = adapter

        view.addNewCity.setOnClickListener {
            MyDialog{
               viewModel.addNewCity(it)
            }.show((activity as MainActivity).getSupportManager(), "dialog")
        }

        viewModel.allCities.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
                Log.d("WTF", "$it")
            }
        })

        (activity as MainActivity).viewModel.useGeolocation.observe(viewLifecycleOwner, Observer {
            it?.let {
                geolocation.isChecked = it
            }
        })

        view.geolocation.setOnCheckedChangeListener { _, isChecked ->
            (activity as MainActivity).viewModel.setUseGeolocation(isChecked)
            if(isChecked){
                (activity as MainActivity).viewModel.requestLocation()
                (activity as MainActivity).setTitle("Your current location")
            }
        }


        return view
    }


}
