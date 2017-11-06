package uk.co.agfsoft.skyweather.view;

import uk.co.agfsoft.skyweather.model.RawWeatherCity;



public interface ShowFavouriteView {
    void updateWeatherDisplay(RawWeatherCity response);

    void showError();

}
