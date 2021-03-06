package com.example.testtask.forecast

import com.example.testtask.network.data.ForecastNetwork
import com.example.testtask.utils.convertToDate
import com.example.testtask.utils.kelvinToCelsius

import java.util.*

data class ForecastModel(
    val date: Date,

    val icon: String,

    val description: String,

    val degree: Int)

fun ForecastNetwork.toListOfModels(): List<ForecastModel>{
    val mutableList = mutableListOf<ForecastModel>()
    this.list?.let {
        for(item in list){
            if(item?.dtTxt != null){
                mutableList.add(
                    ForecastModel(
                        item.dtTxt.convertToDate(),
                        item.weather?.getOrNull(0)?.icon ?: "01d",
                        item.weather?.getOrNull(0)?.main ?: "Clouds",
                        item.main?.temp?.kelvinToCelsius() ?: 0
                        )
                )
            }
        }
    }
    return mutableList.toList()
}

