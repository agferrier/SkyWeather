package uk.co.agfsoft.skyweather.view

import uk.co.agfsoft.skyweather.model.RawWeatherCity


interface ShowFavouriteView {
    fun updateWeatherDisplay(response: RawWeatherCity)

    fun showError()

}
