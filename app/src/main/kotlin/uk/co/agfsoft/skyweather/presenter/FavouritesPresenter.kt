package uk.co.agfsoft.skyweather.presenter

import javax.inject.Inject

import model.Model
import model.WeatherCity
import uk.co.agfsoft.skyweather.view.FavouritesView


class FavouritesPresenter @Inject
constructor(private val favouritesView: FavouritesView, private val model: Model) {

    val favourites: List<WeatherCity>
        get() = model.favourites

    fun addFavouriteClicked() {
        favouritesView.showAddFavouriteView()
    }

    fun viewReady() {
        val favourites = model.favourites
        favouritesView.showIntroOverlay(favourites.size == 0)
        favouritesView.updateFavourites(favourites)
    }

    fun favouriteClicked(weatherCity: WeatherCity) {
        favouritesView.showWeatherForCity(weatherCity)
    }
}
