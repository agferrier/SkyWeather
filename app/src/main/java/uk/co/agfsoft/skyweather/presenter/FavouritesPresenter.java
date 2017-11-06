package uk.co.agfsoft.skyweather.presenter;

import java.util.List;

import uk.co.agfsoft.skyweather.model.WeatherCity;



public interface FavouritesPresenter {
    void addFavouriteClicked();

    void viewReady();

    List<WeatherCity> getFavourites();

    void favouriteClicked(WeatherCity id);
}
