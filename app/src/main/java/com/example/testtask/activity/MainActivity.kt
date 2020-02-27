package com.example.testtask.activity


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.testtask.R
import com.example.testtask.cities.CitiesFragment
import com.example.testtask.database.MyDatabase
import com.example.testtask.forecast.ForecastFragment
import com.example.testtask.network.WeatherAPIClient
import com.example.testtask.repository.Repository
import com.example.testtask.today.TodayFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    lateinit var viewModel: ActivityViewModel
    lateinit var container: FrameLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        container = findViewById(R.id.container)
        if (savedInstanceState == null) {
            showStartFragment(TodayFragment())
        }
        navigation_view.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        val application = requireNotNull(this).application


        val client = WeatherAPIClient.getClient()
        val database = MyDatabase.getInstance(application).databaseDao
        val repository = Repository.getInstance(client, database)

        val viewModelFactory = ActivityViewModelFactory(repository, application)
        viewModel = ViewModelProvider(this, viewModelFactory).get(ActivityViewModel::class.java)

        viewModel.city.observe(this, Observer {list ->
            list.firstOrNull()?.let {
                toolbar_title.text = it.name
            }

        })


    }


    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.today -> {
                loadFragment(TodayFragment())
                return@OnNavigationItemSelectedListener true
            }
            R.id.for_day -> {
                loadFragment(CitiesFragment())
                return@OnNavigationItemSelectedListener true
            }
            R.id.forecast -> {
                loadFragment(ForecastFragment())
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment)
            .addToBackStack(null)
            .commit()
    }

    private fun showStartFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment)
            .commit()
    }

    fun getSupportManager() = supportFragmentManager
}
