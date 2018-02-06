package uk.co.agfsoft.skyweather.presenter;

import java.util.List;

import javax.inject.Inject;

import uk.co.agfsoft.skyweather.model.Model;
import uk.co.agfsoft.skyweather.model.WeatherCity;
import uk.co.agfsoft.skyweather.view.FavouritesView;



public class FavouritesPresenter {
    private final FavouritesView favouritesView;
    private final Model model;

    @Inject
    public FavouritesPresenter(FavouritesView favouritesView, Model model) {
        this.favouritesView = favouritesView;
        this.model = model;
    }

    public void addFavouriteClicked() {
        favouritesView.showAddFavouriteView();
    }

    public void viewReady() {
        List<WeatherCity> favourites = model.getFavourites();
        favouritesView.showIntroOverlay(favourites.size() == 0);
        favouritesView.updateFavourites(favourites);
    }

    public List<WeatherCity> getFavourites() {
        return model.getFavourites();
    }

    public void favouriteClicked(WeatherCity weatherCity) {
        favouritesView.showWeatherForCity(weatherCity);
    }
}
