package uk.co.agfsoft.skyweather

import android.content.res.Resources

import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

import java.util.ArrayList
import java.util.Arrays

import model.Model
import model.WeatherCity
import uk.co.agfsoft.skyweather.presenter.FavouritesPresenter
import uk.co.agfsoft.skyweather.view.FavouritesView

import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`

/**
 * Created by drew on 05/11/2017.
 */

class FavouritesPresenterTest {
    @Mock
    internal var view: FavouritesView? = null
    @Mock
    internal var model: Model? = null
    @Mock
    internal var resources: Resources? = null

    internal var presenter: FavouritesPresenter

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        presenter = FavouritesPresenter(view!!, model!!)
    }

    @Test
    fun willStartAddFavouriteActivityWhenFABClicked() {
        presenter.addFavouriteClicked()
        verify<FavouritesView>(view).showAddFavouriteView()
    }

    @Test
    fun shouldShowOverlayWhenFavouritesEmpty() {
        `when`(model!!.favourites).thenReturn(ArrayList(0))
        presenter.viewReady()
        verify<FavouritesView>(view).showIntroOverlay(true)
    }

    @Test
    fun shouldNotShowOverlayWhenFavouritesIsNotEmpty() {
        val cities = arrayOfNulls<WeatherCity>(3)
        `when`(model!!.favourites).thenReturn(Arrays.asList<WeatherCity>(*cities))
        presenter.viewReady()
        verify<FavouritesView>(view).showIntroOverlay(false)
    }

    @Test
    fun shouldUpdateFavouritesDisplayWithModelDataWhenViewIsReady() {
        val cities = Arrays.asList<WeatherCity>(*arrayOfNulls(3))
        `when`(model!!.favourites).thenReturn(cities)
        presenter.viewReady()
        verify<FavouritesView>(view).updateFavourites(cities)
    }

    @Test
    fun shouldShowFavouriteWhetherWhenCityIsSelected() {
        val city = model.WeatherCity()
        presenter.favouriteClicked(city)
        verify<FavouritesView>(view).showWeatherForCity(city)
    }


}
