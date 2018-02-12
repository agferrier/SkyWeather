package uk.co.agfsoft.skyweather.presenter

import com.android.volley.VolleyError

import java.util.ArrayList

import javax.inject.Inject

import model.FindCitiesResponse
import model.Model
import model.WeatherCity
import uk.co.agfsoft.skyweather.view.AddFavouriteView
import uk.co.agfsoft.skyweather.volley.BaseVolleyRequest


class AddFavouritePresenter @Inject
constructor(private val addFavouriteView: AddFavouriteView, private val model: Model) {

    fun startSearch(searchQuery: String) {
        model.findCities(searchQuery, object : BaseVolleyRequest.Listener<FindCitiesResponse> {
            override fun onSuccess(response: FindCitiesResponse) {
                val weatherCities = ArrayList<WeatherCity>(response.list!!.size)
                for (rawWeatherCity in response.list!!)
                    weatherCities.add(WeatherCity(rawWeatherCity))
                addFavouriteView.updateResults(weatherCities)
            }

            override fun onError(error: VolleyError) {
                addFavouriteView.showError()

            }
        })
    }

    fun citySelected(weatherCity: WeatherCity) {
        model.saveCity(weatherCity)
        addFavouriteView.dismiss()

    }
}
