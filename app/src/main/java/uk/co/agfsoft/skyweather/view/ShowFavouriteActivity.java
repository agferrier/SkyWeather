package uk.co.agfsoft.skyweather.view;

import android.animation.Animator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import uk.co.agfsoft.skyweather.R;
import uk.co.agfsoft.skyweather.dagger.ActivityComponent;
import uk.co.agfsoft.skyweather.dagger.ActivityModule;
import uk.co.agfsoft.skyweather.dagger.InjectableActivity;
import uk.co.agfsoft.skyweather.dagger.InjectableApplication;
import uk.co.agfsoft.skyweather.model.RawWeatherCity;
import uk.co.agfsoft.skyweather.presenter.ShowFavouritePresenter;

public class ShowFavouriteActivity extends AppCompatActivity implements InjectableActivity, ShowFavouriteView {

    public static final String CITY_ID = "city_id";
    public static final String CITY_NAME = "city_name";
    public static final String CITY_COUNTRY = "city_country";
    private ActivityComponent activityComponent;

    @Inject
    ShowFavouritePresenter showFavouritePresenter;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.speedTextView)
    TextView speedTextView;
    @BindView(R.id.directionTextView)
    TextView directionTextView;
    @BindView(R.id.directionImageView)
    ImageView directionImageView;

    //<editor-fold desc="Lifecycle methods">
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivityComponent().inject(this);

        setContentView(R.layout.activity_show_favourite);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        String title = this.getString(R.string.favourite_description_format,
                getIntent().getStringExtra(CITY_NAME),
                getIntent().getStringExtra(CITY_COUNTRY));
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        int cityId = getIntent().getIntExtra(CITY_ID, 0);
        showFavouritePresenter.setCityId(cityId);
        showFavouritePresenter.viewReady();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
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


    //<editor-fold desc="SHowFavouriteView implementation">
    @Override
    public void updateWeatherDisplay(RawWeatherCity response) {
        speedTextView.setText(this.getString(R.string.wind_speed_format, response.getWindSpeed()));
        directionTextView.setText(getString(R.string.wind_direction_format, response.getWindDirection()));
        Animation animation =  new RotateAnimation(180f, ((float) ((response.getWindDirection() + 180) % 360)),
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setFillAfter(true);
        animation.setDuration(1000L);
        directionImageView.setAnimation(animation);
        animation.start();
    }

    @Override
    public void showError() {

    }
    //</editor-fold>
}
