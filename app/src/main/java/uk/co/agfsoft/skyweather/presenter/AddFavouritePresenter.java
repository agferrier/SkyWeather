package uk.co.agfsoft.skyweather.presenter;

import uk.co.agfsoft.skyweather.model.WeatherCity;



public interface AddFavouritePresenter {
    void startSearch(String searchQuery);

    void citySelected(WeatherCity weatherCity);
}
