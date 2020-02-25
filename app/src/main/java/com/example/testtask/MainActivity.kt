package com.example.testtask

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.testtask.cities.CitiesFragment
import com.example.testtask.forecast.ForecastFragment
import com.example.testtask.today.TodayFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            showStartFragment(TodayFragment())
        }
        navigation_view.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
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
}
