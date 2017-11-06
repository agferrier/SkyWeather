package uk.co.agfsoft.skyweather.dagger;

import android.app.Activity;

import dagger.Module;
import dagger.Provides;
import uk.co.agfsoft.skyweather.model.Model;
import uk.co.agfsoft.skyweather.presenter.AddFavouritePresenter;
import uk.co.agfsoft.skyweather.presenter.AddFavouritePresenterImpl;
import uk.co.agfsoft.skyweather.presenter.FavouritesPresenter;
import uk.co.agfsoft.skyweather.presenter.FavouritesPresenterImpl;
import uk.co.agfsoft.skyweather.presenter.ShowFavouritePresenter;
import uk.co.agfsoft.skyweather.presenter.ShowFavouritePresenterImpl;
import uk.co.agfsoft.skyweather.view.AddFavouriteView;
import uk.co.agfsoft.skyweather.view.FavouritesView;
import uk.co.agfsoft.skyweather.view.ShowFavouriteView;



@Module
public class ActivityModule {

    private Activity activity;

    public ActivityModule(Activity activity) {
        this.activity = activity;
    }

    @Provides
    FavouritesView provideFavouritesView() {
        return (FavouritesView) this.activity;
    }

    @Provides
    FavouritesPresenter provideFavouritesPresenter(FavouritesView favouritesView, Model model) {
        return new FavouritesPresenterImpl(favouritesView, model);
    }

    @Provides
    AddFavouriteView provideAddFavouriteView() {
        return (AddFavouriteView)activity;
    }

    @Provides
    AddFavouritePresenter provideAddFavouritePresenter(AddFavouriteView addFavouriteView, Model model) {
        return new AddFavouritePresenterImpl(addFavouriteView, model);
    }

    @Provides
    ShowFavouriteView provideShowFavouriteView() {
        return (ShowFavouriteView)activity;
    }

    @Provides
    ShowFavouritePresenter provideShowFavouritePresenter(ShowFavouriteView showFavouriteView, Model model) {
        return new ShowFavouritePresenterImpl(showFavouriteView, model);
    }

}
