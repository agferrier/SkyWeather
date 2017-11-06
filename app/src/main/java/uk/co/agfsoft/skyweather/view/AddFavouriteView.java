package uk.co.agfsoft.skyweather.view;

import java.util.List;

import uk.co.agfsoft.skyweather.model.WeatherCity;



public interface AddFavouriteView {
    void updateResults(List<WeatherCity> response);

    void showError();

    void dismiss();
}
