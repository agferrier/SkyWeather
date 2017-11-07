package uk.co.agfsoft.skyweather.view

import uk.co.agfsoft.skyweather.model.WeatherCity


interface FavouritesView {
    fun showAddFavouriteView()

    fun showIntroOverlay(showView: Boolean)

    fun updateFavourites(favourites: List<WeatherCity>)

    fun showWeatherForCity(weatherCity: WeatherCity)
}
