package com.example.testtask.activity


import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
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
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    val MY_PERMISSIONS_REQUEST = 12

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

        askGeolocation()


        viewModel.location.observe(this, Observer{
            Log.d("WTF", "$it")
        })
    }



    fun askGeolocation(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                AlertDialog.Builder(this)
                    .setTitle("Geolocation permission")
                    .setMessage("Need your location for work")
                    .setPositiveButton("Ok") { _,   _ ->
                        ActivityCompat.requestPermissions(this,
                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                        MY_PERMISSIONS_REQUEST) }
                    .setNegativeButton("Cancel") { _, _ ->
                        showSnackBarWithAction()}
                    .show()
            } else {
                ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    MY_PERMISSIONS_REQUEST)
            }
        }else{
            viewModel.requestLocation()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            MY_PERMISSIONS_REQUEST -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    viewModel.requestLocation()
                } else {
                    showSnackBarWithAction()
                }
                return
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        askGeolocation()
    }


    private fun showSnackBarWithAction(){
        val mySnackbar = Snackbar.make(container, "Please open settings and turn on permission",
            Snackbar.LENGTH_LONG)
        mySnackbar.setAction("Open", OpenSettings())
        mySnackbar.show()
    }


    private inner class OpenSettings : View.OnClickListener {

        override fun onClick(v: View) {
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            val uri = Uri.fromParts("package", packageName, null)
            intent.data = uri
            startActivityForResult(intent, 0)
        }
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
