package uk.co.agfsoft.skyweather.volley.requests

import com.android.volley.Request

import java.util.HashMap

import javax.inject.Inject

import uk.co.agfsoft.skyweather.BuildConfig
import model.RawWeatherCity
import uk.co.agfsoft.skyweather.volley.BaseVolleyRequest


class GetByIdRequest @Inject
constructor() : BaseVolleyRequest<RawWeatherCity>() {

    private var queryParams: MutableMap<String, String>? = null

    protected override val method: Int
        get() = Request.Method.GET

    protected override val responseClass: Class<*>
        get() = RawWeatherCity::class.java

    override fun getUrl(): String {
        return BuildConfig.OPEN_WEATHER_DATA_URL
    }


    override fun getQueryParams(): Map<String, String>? {
        return queryParams
    }

    fun setQuery(cityId: Int) {
        queryParams = HashMap(3)
        queryParams!![ID] = cityId.toString()
        queryParams!![APPID] = BuildConfig.OPEN_WEATHER_API_KEY
    }

    companion object {

        private val ID = "id"
        private val APPID = "APPID"
    }
}
