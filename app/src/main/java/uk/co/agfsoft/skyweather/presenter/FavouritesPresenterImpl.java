package uk.co.agfsoft.skyweather.presenter;

import java.util.List;

import uk.co.agfsoft.skyweather.model.Model;
import uk.co.agfsoft.skyweather.model.WeatherCity;
import uk.co.agfsoft.skyweather.view.FavouritesView;



public class FavouritesPresenterImpl implements FavouritesPresenter {
    private final FavouritesView favouritesView;
    private final Model model;

    public FavouritesPresenterImpl(FavouritesView favouritesView, Model model) {
        this.favouritesView = favouritesView;
        this.model = model;
    }

    @Override
    public void addFavouriteClicked() {
        favouritesView.showAddFavouriteView();
    }

    @Override
    public void viewReady() {
        List<WeatherCity> favourites = model.getFavourites();
        favouritesView.showIntroOverlay(favourites.size() == 0);
        favouritesView.updateFavourites(favourites);
    }

    @Override
    public List<WeatherCity> getFavourites() {
        return model.getFavourites();
    }

    @Override
    public void favouriteClicked(WeatherCity weatherCity) {
        favouritesView.showWeatherForCity(weatherCity);
    }
}
