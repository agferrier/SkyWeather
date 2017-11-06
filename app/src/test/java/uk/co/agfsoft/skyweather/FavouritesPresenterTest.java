package uk.co.agfsoft.skyweather;

import android.content.res.Resources;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import uk.co.agfsoft.skyweather.model.Model;
import uk.co.agfsoft.skyweather.model.WeatherCity;
import uk.co.agfsoft.skyweather.presenter.FavouritesPresenter;
import uk.co.agfsoft.skyweather.presenter.FavouritesPresenterImpl;
import uk.co.agfsoft.skyweather.view.FavouritesView;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by drew on 05/11/2017.
 */

public class FavouritesPresenterTest {
    @Mock
    FavouritesView view;
    @Mock
    Model model;
    @Mock
    Resources resources;

    FavouritesPresenter presenter;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        presenter = new FavouritesPresenterImpl(view, model);
    }

    @Test
    public void willStartAddFavouriteActivityWhenFABClicked() {
        presenter.addFavouriteClicked();
        verify(view).showAddFavouriteView();
    }

    @Test
    public void shouldShowOverlayWhenFavouritesEmpty() {
        when(model.getFavourites()).thenReturn(new ArrayList<WeatherCity>(0));
        presenter.viewReady();
        verify(view).showIntroOverlay(true);
    }

    @Test
    public void shouldNotShowOverlayWhenFavouritesIsNotEmpty() {
        WeatherCity[] cities = new WeatherCity[3];
        when(model.getFavourites()).thenReturn(Arrays.asList(cities));
        presenter.viewReady();
        verify(view).showIntroOverlay(false);
    }

    @Test
    public void shouldUpdateFavouritesDisplayWithModelDataWhenViewIsReady() {
        List<WeatherCity> cities = Arrays.asList(new WeatherCity[3]);
        when(model.getFavourites()).thenReturn(cities);
        presenter.viewReady();
        verify(view).updateFavourites(cities);
    }

    @Test
    public void shouldShowFavouriteWhetherWhenCityIsSelected() {
        WeatherCity city = new WeatherCity();
        presenter.favouriteClicked(city);
        verify(view).showWeatherForCity(city);
    }


}
