package uk.co.agfsoft.skyweather.view

import model.RawWeatherCity


interface ShowFavouriteView {
    fun updateWeatherDisplay(response: RawWeatherCity)

    fun showError()

}
