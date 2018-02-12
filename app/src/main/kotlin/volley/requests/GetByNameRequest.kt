package uk.co.agfsoft.skyweather.volley.requests

import com.android.volley.Request

import java.util.HashMap

import javax.inject.Inject

import uk.co.agfsoft.skyweather.BuildConfig
import model.FindCitiesResponse
import uk.co.agfsoft.skyweather.volley.BaseVolleyRequest


class GetByNameRequest @Inject
constructor() : BaseVolleyRequest<FindCitiesResponse>() {

    private var queryParams: MutableMap<String, String>? = null

    protected override val method: Int
        get() = Request.Method.GET

    protected override val responseClass: Class<*>
        get() = FindCitiesResponse::class.java

    override fun getUrl(): String {
        return BuildConfig.OPEN_WEATHER_FIND_URL
    }


    override fun getQueryParams(): Map<String, String>? {
        return queryParams
    }

    fun setQuery(cityName: String) {
        queryParams = HashMap(3)
        queryParams!![Q] = cityName
        queryParams!![TYPE] = LIKE
        queryParams!![APPID] = BuildConfig.OPEN_WEATHER_API_KEY
    }

    companion object {

        private val Q = "q"
        private val APPID = "APPID"
        val TYPE = "type"
        val LIKE = "like"
    }
}
