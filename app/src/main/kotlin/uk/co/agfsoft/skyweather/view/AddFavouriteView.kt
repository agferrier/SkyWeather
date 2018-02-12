package uk.co.agfsoft.skyweather.view

import model.WeatherCity


interface AddFavouriteView {
    fun updateResults(response: List<WeatherCity>)

    fun showError()

    fun dismiss()
}
