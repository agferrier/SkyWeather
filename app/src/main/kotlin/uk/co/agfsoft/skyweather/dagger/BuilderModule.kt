package dagger

import dagger.android.ContributesAndroidInjector
import uk.co.agfsoft.skyweather.view.AddFavouriteActivity
import uk.co.agfsoft.skyweather.view.FavouritesActivity
import uk.co.agfsoft.skyweather.view.ShowFavouriteActivity

/**
 * Created by afa28 on 06/02/2018.
 */
@Module
abstract class BuilderModule {
    @ContributesAndroidInjector
    abstract fun bindAddFavouriteActivity(): AddFavouriteActivity

    @ContributesAndroidInjector
    abstract fun bindFavouritesActivity(): FavouritesActivity

    @ContributesAndroidInjector
    abstract fun bindShowFavouriteActivity(): ShowFavouriteActivity

}