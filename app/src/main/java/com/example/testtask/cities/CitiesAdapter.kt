package com.example.testtask.cities


import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Switch
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.testtask.R
import com.example.testtask.database.City


class CitiesAdapter(val selected: (City, Boolean) -> Unit, val delete: (City) -> Unit) : ListAdapter<City,
        CitiesAdapter.ViewHolder>(CityDiffCallback()) {


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), selected, delete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.cities_list_item, parent, false))
    }

    class ViewHolder (itemView: View)
        : RecyclerView.ViewHolder(itemView) {

        private val cityName: TextView = itemView.findViewById(R.id.city)
        private val selectedSwitch: Switch = itemView.findViewById(R.id.selected)
        private val cancel: ImageView = itemView.findViewById(R.id.cancel)

        fun bind(city: City, selected: (City, Boolean) -> Unit, delete: (City) -> Unit) {
            Log.d("WTF", "$city")
            cityName.text = city.name
            selectedSwitch.isChecked = city.isLastSelected
            selectedSwitch.setOnCheckedChangeListener { _, isChecked ->
               selected(city, isChecked)
            }
            cancel.setOnClickListener { delete(city) }
        }
    }
}

class CityDiffCallback : DiffUtil.ItemCallback<City>() {
    override fun areItemsTheSame(oldItem: City, newItem: City) = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: City, newItem: City)= oldItem == newItem
}

