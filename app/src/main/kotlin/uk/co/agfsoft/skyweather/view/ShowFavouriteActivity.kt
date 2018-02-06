package uk.co.agfsoft.skyweather.view

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import butterknife.ButterKnife
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_show_favourite.*
import kotlinx.android.synthetic.main.content_show_favourite.*
import uk.co.agfsoft.skyweather.R
import uk.co.agfsoft.skyweather.model.RawWeatherCity
import uk.co.agfsoft.skyweather.presenter.ShowFavouritePresenter
import uk.co.agfsoft.skyweather.utils.ContextLogger
import uk.co.agfsoft.skyweather.utils.showAlert
import javax.inject.Inject

class ShowFavouriteActivity : AppCompatActivity(), ShowFavouriteView {

    @Inject
    lateinit internal var showFavouritePresenter: ShowFavouritePresenter

    @Inject
    lateinit internal var contextLogger: ContextLogger

    //<editor-fold desc="Lifecycle methods">
    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        contextLogger.logContext()
        super.onCreate(savedInstanceState)
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
        showAlert(this, R.string.network_error, R.string.network_error_message)
    }

    companion object {

        val CITY_ID = "city_id"
        val CITY_NAME = "city_name"
        val CITY_COUNTRY = "city_country"
    }
    //</editor-fold>
}
