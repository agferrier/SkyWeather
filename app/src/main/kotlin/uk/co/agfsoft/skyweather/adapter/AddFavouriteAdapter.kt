package uk.co.agfsoft.skyweather.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import java.util.ArrayList

import butterknife.ButterKnife
import butterknife.OnClick
import kotlinx.android.synthetic.main.recycler_item_add_favourite.view.*
import uk.co.agfsoft.skyweather.R
import model.WeatherCity
import uk.co.agfsoft.skyweather.presenter.AddFavouritePresenter


class AddFavouriteAdapter(val addFavouritePresenter: AddFavouritePresenter) : RecyclerView.Adapter<AddFavouriteAdapter.ViewHolder>() {
    private var weatherCities: List<WeatherCity>? = null

    init {
        weatherCities = ArrayList(0)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddFavouriteAdapter.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemView = inflater.inflate(R.layout.recycler_item_add_favourite, parent, false)
        return AddFavouriteAdapter.ViewHolder(itemView, addFavouritePresenter)
    }

    override fun onBindViewHolder(holder: AddFavouriteAdapter.ViewHolder, position: Int) {
        val weatherCity = weatherCities!![position]
        holder.updateContent(weatherCity)

    }

    override fun getItemCount(): Int {
        return weatherCities!!.size
    }

    fun setWeatherCities(weatherCities: List<WeatherCity>) {
        this.weatherCities = weatherCities
    }

    class ViewHolder(itemView: View, private val addFavouritePresenter: AddFavouritePresenter) : RecyclerView.ViewHolder(itemView) {

        internal var context: Context
        private var weatherCity: WeatherCity? = null

        init {
            ButterKnife.bind(this, itemView)
            context = itemView.context
        }

        fun updateContent(weatherCity: WeatherCity) {
            this.weatherCity = weatherCity
            itemView.descriptionTextView!!.text = context.getString(R.string.favourite_description_format, weatherCity.name, weatherCity.country)
            itemView.latLongTextView!!.text = context.getString(R.string.lat_long_format, weatherCity.latitiude, weatherCity.longitude)
        }

        @OnClick(R.id.selectButton)
        fun onCitySelected() {
            addFavouritePresenter.citySelected(weatherCity!!)
        }
    }

}
