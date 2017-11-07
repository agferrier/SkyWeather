package uk.co.agfsoft.skyweather.view

import uk.co.agfsoft.skyweather.model.WeatherCity


interface AddFavouriteView {
    fun updateResults(response: List<WeatherCity>)

    fun showError()

    fun dismiss()
}
