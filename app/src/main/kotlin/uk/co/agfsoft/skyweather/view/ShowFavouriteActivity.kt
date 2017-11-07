package uk.co.agfsoft.skyweather.view

import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import butterknife.ButterKnife
import kotlinx.android.synthetic.main.activity_show_favourite.*
import kotlinx.android.synthetic.main.content_show_favourite.*
import uk.co.agfsoft.skyweather.R
import uk.co.agfsoft.skyweather.dagger.ActivityComponent
import uk.co.agfsoft.skyweather.dagger.ActivityModule
import uk.co.agfsoft.skyweather.dagger.InjectableActivity
import uk.co.agfsoft.skyweather.dagger.InjectableApplication
import uk.co.agfsoft.skyweather.model.RawWeatherCity
import uk.co.agfsoft.skyweather.presenter.ShowFavouritePresenter
import javax.inject.Inject

class ShowFavouriteActivity : AppCompatActivity(), InjectableActivity, ShowFavouriteView {
    lateinit private var activityComponent: ActivityComponent

    @Inject
    lateinit internal var showFavouritePresenter: ShowFavouritePresenter

    //<editor-fold desc="Lifecycle methods">
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getActivityComponent().inject(this)

        setContentView(R.layout.activity_show_favourite)
        ButterKnife.bind(this)
        setSupportActionBar(toolbar)
        val title = this.getString(R.string.favourite_description_format,
                intent.getStringExtra(CITY_NAME),
                intent.getStringExtra(CITY_COUNTRY))
        supportActionBar!!.title = title
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    override fun onResume() {
        super.onResume()
        val cityId = intent.getIntExtra(CITY_ID, 0)
        showFavouritePresenter.setCityId(cityId)
        showFavouritePresenter.viewReady()

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        } else {
            return super.onOptionsItemSelected(item)
        }
    }
    //</editor-fold>


    //<editor-fold desc="InjectableActivity implementation">
    override fun getActivityComponent(): ActivityComponent {
        activityComponent = (application as InjectableApplication)
                .applicationComponent
                .plus(ActivityModule(this))
        return activityComponent

    }
    //</editor-fold>


    //<editor-fold desc="SHowFavouriteView implementation">
    override fun updateWeatherDisplay(response: RawWeatherCity) {
        speedTextView.text = this.getString(R.string.wind_speed_format, response.windSpeed)
        directionTextView.text = getString(R.string.wind_direction_format, response.windDirection)
        val animation = RotateAnimation(180f, ((response.windDirection + 180) % 360).toFloat(),
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f)
        animation.fillAfter = true
        animation.duration = 1000L
        directionImageView!!.animation = animation
        animation.start()
    }

    override fun showError() {
        AlertDialog.Builder(this)
                .setTitle(getString(R.string.network_error))
                .setMessage(getString(R.string.network_error_message))
                .create()
                .show()
    }

    companion object {

        val CITY_ID = "city_id"
        val CITY_NAME = "city_name"
        val CITY_COUNTRY = "city_country"
    }
    //</editor-fold>
}
