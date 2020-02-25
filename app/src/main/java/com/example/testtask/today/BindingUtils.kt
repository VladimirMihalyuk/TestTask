package com.example.testtask.today

import android.widget.ImageView
import androidx.databinding.BindingAdapter


@BindingAdapter("image")
fun ImageView.setImageFromResource(id: Int) {
    setImageResource(id)
}