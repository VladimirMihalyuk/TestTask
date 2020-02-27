package com.example.testtask.forecast


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.testtask.R
import com.example.testtask.activity.ActivityViewModel
import com.example.testtask.activity.MainActivity
import com.example.testtask.database.MyDatabase
import com.example.testtask.forecast.adapter.ForecastAdapter
import com.example.testtask.forecast.adapter.ForecastListItem
import com.example.testtask.network.WeatherAPIClient
import com.example.testtask.repository.Repository
import kotlinx.android.synthetic.main.fragment_forecast.view.*

/**
 * A simple [Fragment] subclass.
 */
class ForecastFragment : Fragment() {

    val startList = mutableListOf<ForecastListItem>()
    private lateinit var adapter: ForecastAdapter
    private lateinit var viewModel: ForecastViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_forecast, container, false)
        val application = requireNotNull(this.activity).application

        val client = WeatherAPIClient.getClient()
        val database = MyDatabase.getInstance(application).databaseDao
        val repository = Repository.getInstance(client, database)

        val viewModelFactory = ForecastViewModelFactory(repository, application)
        viewModel = ViewModelProvider(this, viewModelFactory).get(ForecastViewModel::class.java)

        adapter = ForecastAdapter(startList, application)

        view.list.adapter = adapter

        viewModel.list.observe(viewLifecycleOwner, Observer {list ->
            adapter.updateList(list)
        })

        viewModel.loading.observe(viewLifecycleOwner, Observer { loading ->
            if(loading){
                view.loading.visibility = View.VISIBLE
            }else{
                view.loading.visibility = View.INVISIBLE
            }
        })

        (activity as MainActivity).viewModel.city.observe(viewLifecycleOwner, Observer {list ->
            list.firstOrNull()?.let{
                viewModel.getForecastByCityName(it.name)
            }
        })


        return view
    }


}
