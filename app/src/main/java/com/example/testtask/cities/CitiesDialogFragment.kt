package com.example.testtask.cities

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.testtask.R
import android.widget.Button
import android.widget.TextView


class MyDialog(val addNewCallback: (String) -> Unit) : DialogFragment() {
    lateinit var cityName:TextView
    lateinit var addNew: Button
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_my_dialog, null)
        cityName = v.findViewById(R.id.city)
        addNew = v.findViewById(R.id.add)
        addNew.setOnClickListener {
            addNewCallback(cityName.text.toString())
            dismiss()
        }
        return v
    }
}