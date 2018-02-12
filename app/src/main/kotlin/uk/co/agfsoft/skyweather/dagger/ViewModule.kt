package dagger

import uk.co.agfsoft.skyweather.view.*

/**
 * Created by afa28 on 06/02/2018.
 */
@Module
abstract class ViewModule {

    @Binds
    abstract fun bindAddFavouriteView(addFavouriteActivity: AddFavouriteActivity): AddFavouriteView

    @Binds
    abstract fun bindFavouritesView(addFavouriteActivity: FavouritesActivity): FavouritesView

    @Binds
    abstract fun bindShowFavouriteView(addFavouriteActivity: ShowFavouriteActivity): ShowFavouriteView

}