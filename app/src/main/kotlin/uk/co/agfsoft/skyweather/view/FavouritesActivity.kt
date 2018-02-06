package uk.co.agfsoft.skyweather.view

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.view.View
import butterknife.ButterKnife
import butterknife.OnClick
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_favourites.*
import kotlinx.android.synthetic.main.content_favourites.*
import uk.co.agfsoft.skyweather.R
import uk.co.agfsoft.skyweather.model.WeatherCity
import uk.co.agfsoft.skyweather.presenter.FavouritesPresenter
import uk.co.agfsoft.skyweather.utils.ContextLogger
import uk.co.agfsoft.skyweather.view.adapter.FavouriteAdapter
import javax.inject.Inject

class FavouritesActivity : AppCompatActivity(), FavouritesView {


    lateinit private var favouriteAdapter: FavouriteAdapter

    @Inject
    lateinit internal var favouritesPresenter: FavouritesPresenter

    //<editor-fold desc="Lifecycle methods">
    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favourites)
        ButterKnife.bind(this)
        setSupportActionBar(toolbar)
        initialiseRecyclerView()
    }

    override fun onResume() {
        super.onResume()
        favouritesPresenter.viewReady()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_favourites, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId


        return if (id == R.id.action_settings) {
            true
        } else super.onOptionsItemSelected(item)

    }
    //</editor-fold>


    //<editor-fold desc="Event handlers">
    @OnClick(R.id.fab)
    fun onFabClicked(view: View) {
        favouritesPresenter.addFavouriteClicked()
    }
    //</editor-fold>


    //<editor-fold desc="FavouritesView implementation">
    override fun showAddFavouriteView() {
        val intent = Intent(this, AddFavouriteActivity::class.java)
        startActivity(intent)
    }

    override fun showIntroOverlay(showView: Boolean) {
        introOverlay.visibility = if (showView) View.VISIBLE else View.GONE
    }

    override fun updateFavourites(favourites: List<WeatherCity>) {
        favouriteAdapter.setFavourites(favourites)
        favouriteAdapter.notifyDataSetChanged()
    }

    override fun showWeatherForCity(weatherCity: WeatherCity) {
        val intent = Intent(this, ShowFavouriteActivity::class.java)
        intent.putExtra(ShowFavouriteActivity.CITY_ID, weatherCity.id)
        intent.putExtra(ShowFavouriteActivity.CITY_NAME, weatherCity.name)
        intent.putExtra(ShowFavouriteActivity.CITY_COUNTRY, weatherCity.country)

        startActivity(intent)
    }
    //</editor-fold>


    //<editor-fold desc="Service routines">
    private fun initialiseRecyclerView() {
        favouritesRecyclerView!!.layoutManager = LinearLayoutManager(this)
        favouriteAdapter = FavouriteAdapter(favouritesPresenter.favourites, favouritesPresenter)
        favouritesRecyclerView!!.adapter = favouriteAdapter
    }
    //</editor-fold>


}
