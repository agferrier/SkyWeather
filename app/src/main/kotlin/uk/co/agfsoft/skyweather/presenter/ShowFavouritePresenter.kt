package uk.co.agfsoft.skyweather.presenter

import com.android.volley.VolleyError

import javax.inject.Inject

import model.Model
import model.RawWeatherCity
import uk.co.agfsoft.skyweather.view.ShowFavouriteView
import uk.co.agfsoft.skyweather.volley.BaseVolleyRequest


open class ShowFavouritePresenter @Inject
constructor(private val showFavouriteView: ShowFavouriteView, private val model: Model) {
    private var cityId: Int = 0

    fun viewReady() {
        if (cityId != 0) {
            submitWeatherRequest(cityId)
        }

    }

    private fun submitWeatherRequest(cityId: Int) {
        model.getWeatherForCity(cityId, object : BaseVolleyRequest.Listener<RawWeatherCity> {
            override fun onSuccess(response: RawWeatherCity) {
                showFavouriteView.updateWeatherDisplay(response)
            }

            override fun onError(error: VolleyError) {
                showFavouriteView.showError()

            }
        })
    }


    fun setCityId(cityId: Int) {
        this.cityId = cityId
    }


}
