package uk.co.agfsoft.skyweather.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import uk.co.agfsoft.skyweather.R;
import uk.co.agfsoft.skyweather.dagger.ActivityComponent;
import uk.co.agfsoft.skyweather.dagger.ActivityModule;
import uk.co.agfsoft.skyweather.dagger.InjectableActivity;
import uk.co.agfsoft.skyweather.dagger.InjectableApplication;
import uk.co.agfsoft.skyweather.model.WeatherCity;
import uk.co.agfsoft.skyweather.presenter.FavouritesPresenter;
import uk.co.agfsoft.skyweather.view.adapter.FavouriteAdapter;

public class FavouritesActivity extends AppCompatActivity implements InjectableActivity, FavouritesView {


    private ActivityComponent activityComponent;
    private FavouriteAdapter favouriteAdapter;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.intro_overlay)
    View introOverlay;
    @BindView(R.id.favouritesRecyclerView)
    RecyclerView favouritesRecyclerView;

    @Inject
    FavouritesPresenter favouritesPresenter;

    //<editor-fold desc="Lifecycle methods">
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivityComponent().inject(this);

        setContentView(R.layout.activity_favourites);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        initialiseRecyclerView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        favouritesPresenter.viewReady();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_favourites, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    //</editor-fold>



    //<editor-fold desc="Event handlers">
    @OnClick(R.id.fab)
    protected void onFabClicked(View view) {
        favouritesPresenter.addFavouriteClicked();
    }
    //</editor-fold>


    //<editor-fold desc="FavouritesView implementation">
    @Override
    public void showAddFavouriteView() {
        Intent intent = new Intent(this, AddFavouriteActivity.class);
        startActivity(intent);
    }

    @Override
    public void showIntroOverlay(boolean showView) {
        introOverlay.setVisibility(showView ? View.VISIBLE : View.GONE);
    }

    @Override
    public void updateFavourites(List<WeatherCity> favourites) {
        favouriteAdapter.setFavourites(favourites);
        favouriteAdapter.notifyDataSetChanged();
    }

    @Override
    public void showWeatherForCity(WeatherCity weatherCity) {
        Intent intent = new Intent(this, ShowFavouriteActivity.class);
        intent.putExtra(ShowFavouriteActivity.CITY_ID, weatherCity.getId());
        intent.putExtra(ShowFavouriteActivity.CITY_NAME, weatherCity.getName());
        intent.putExtra(ShowFavouriteActivity.CITY_COUNTRY, weatherCity.getCountry());

        startActivity(intent);
    }
    //</editor-fold>



    //<editor-fold desc="InjectableActivity implementation">
    @Override
    public ActivityComponent getActivityComponent() {
        if (activityComponent == null) {
            activityComponent = ((InjectableApplication) getApplication())
                    .getApplicationComponent()
                    .plus(new ActivityModule(this));
        }
        return activityComponent;

    }
    //</editor-fold>



    //<editor-fold desc="Service routines">
    private void initialiseRecyclerView() {
        favouritesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        favouriteAdapter = new FavouriteAdapter(favouritesPresenter.getFavourites(), favouritesPresenter);
        favouritesRecyclerView.setAdapter(favouriteAdapter);
    }
    //</editor-fold>


}
