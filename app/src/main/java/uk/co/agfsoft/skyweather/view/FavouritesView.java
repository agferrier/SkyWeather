package uk.co.agfsoft.skyweather.view;

import java.util.List;

import uk.co.agfsoft.skyweather.model.WeatherCity;



public interface FavouritesView {
    void showAddFavouriteView();

    void showIntroOverlay(boolean showView);

    void updateFavourites(List<WeatherCity> favourites);

    void showWeatherForCity(WeatherCity weatherCity);
}
