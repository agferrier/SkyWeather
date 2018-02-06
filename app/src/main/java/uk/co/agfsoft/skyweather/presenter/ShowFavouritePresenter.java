package uk.co.agfsoft.skyweather.presenter;

import com.android.volley.VolleyError;

import javax.inject.Inject;

import uk.co.agfsoft.skyweather.model.Model;
import uk.co.agfsoft.skyweather.model.RawWeatherCity;
import uk.co.agfsoft.skyweather.view.ShowFavouriteView;
import uk.co.agfsoft.skyweather.volley.BaseVolleyRequest;



public class ShowFavouritePresenter {
    private final ShowFavouriteView showFavouriteView;
    private final Model model;
    private int cityId;

    @Inject
    public ShowFavouritePresenter(ShowFavouriteView showFavouriteView, Model model) {

        this.showFavouriteView = showFavouriteView;
        this.model = model;
    }

    public void viewReady() {
        if (cityId != 0) {
            submitWeatherRequest(cityId);
        }

    }

    private void submitWeatherRequest(int cityId) {
        model.getWeatherForCity(cityId, new BaseVolleyRequest.Listener<RawWeatherCity>() {
            @Override
            public void onSuccess(RawWeatherCity response) {
                showFavouriteView.updateWeatherDisplay(response);
            }

            @Override
            public void onError(VolleyError error) {
                showFavouriteView.showError();

            }
        });
    }


    public void setCityId(int cityId) {
        this.cityId = cityId;
    }


}
