package uk.co.agfsoft.skyweather.presenter;

import com.android.volley.VolleyError;

import java.util.ArrayList;
import java.util.List;

import uk.co.agfsoft.skyweather.model.FindCitiesResponse;
import uk.co.agfsoft.skyweather.model.Model;
import uk.co.agfsoft.skyweather.model.RawWeatherCity;
import uk.co.agfsoft.skyweather.model.WeatherCity;
import uk.co.agfsoft.skyweather.view.AddFavouriteView;
import uk.co.agfsoft.skyweather.volley.BaseVolleyRequest;



public class AddFavouritePresenterImpl implements AddFavouritePresenter {
    private final AddFavouriteView addFavouriteView;
    private final Model model;

    public AddFavouritePresenterImpl(AddFavouriteView addFavouriteView, Model model) {
        this.addFavouriteView = addFavouriteView;
        this.model = model;
    }

    @Override
    public void startSearch(String searchQuery) {
        model.findCities(searchQuery, new BaseVolleyRequest.Listener<FindCitiesResponse>() {
            @Override
            public void onSuccess(FindCitiesResponse response) {
                List<WeatherCity> weatherCities = new ArrayList<WeatherCity>(response.getList().length);
                for (RawWeatherCity rawWeatherCity : response.getList())
                    weatherCities.add(new WeatherCity(rawWeatherCity));
                addFavouriteView.updateResults(weatherCities);
            }

            @Override
            public void onError(VolleyError error) {
                addFavouriteView.showError();

            }
        });
    }

    @Override
    public void citySelected(WeatherCity weatherCity) {
        model.saveCity(weatherCity);
        addFavouriteView.dismiss();

    }
}
