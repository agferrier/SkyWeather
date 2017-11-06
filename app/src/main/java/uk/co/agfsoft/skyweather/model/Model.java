package uk.co.agfsoft.skyweather.model;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.realm.Realm;
import io.realm.RealmResults;
import uk.co.agfsoft.skyweather.volley.BaseVolleyRequest;
import uk.co.agfsoft.skyweather.volley.requests.GetByIdRequest;
import uk.co.agfsoft.skyweather.volley.requests.GetByNameRequest;


@Singleton
public class Model {

    private Realm realm;
    private RealmResults<WeatherCity> weatherCities;

    @Inject
    GetByNameRequest getByNameRequest;
    @Inject
    GetByIdRequest getByIdRequest;

    private Context context;

    @Inject
    public Model(Context context) {
        this.context = context;
        initialiseRealm(context);
        loadFavourites();
    }

    private void initialiseRealm(Context context) {
        Realm.init(context);
        realm = Realm.getDefaultInstance();
    }

    private void loadFavourites() {
        weatherCities = realm.where(WeatherCity.class).findAll();
    }

    public List<WeatherCity> getFavourites() {
        return new ArrayList<>(weatherCities);
    }

    public void findCities(String searchQuery, BaseVolleyRequest.Listener<FindCitiesResponse> listener) {
        getByNameRequest.setQuery(searchQuery);
        getByNameRequest.submit(context, false, listener);
    }

    public void getWeatherForCity(int cityId, BaseVolleyRequest.Listener<RawWeatherCity> listener) {
        getByIdRequest.setQuery(cityId);
        getByIdRequest.submit(context, false, listener);
    }


    public void saveCity(WeatherCity weatherCity) {
        boolean isCitySaved = false;
        for (WeatherCity city: weatherCities) {
            if (city.getId() == weatherCity.getId()) {
                isCitySaved = true;
                break;
            }
        }
        if (!isCitySaved) {
            realm.beginTransaction();
            realm.copyToRealm(weatherCity);
            realm.commitTransaction();
            loadFavourites();
        }
    }
}
