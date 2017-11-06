package uk.co.agfsoft.skyweather.dagger;

import dagger.Subcomponent;
import uk.co.agfsoft.skyweather.view.AddFavouriteActivity;
import uk.co.agfsoft.skyweather.view.FavouritesActivity;
import uk.co.agfsoft.skyweather.view.ShowFavouriteActivity;



@Subcomponent(modules = {ActivityModule.class})
public interface ActivityComponent {
    void inject(FavouritesActivity favouritesActivity);
    void inject(AddFavouriteActivity addFavouriteActivity);
    void inject(ShowFavouriteActivity showFavouriteActivity);
}
