package model

import android.content.Context

import java.util.ArrayList

import javax.inject.Inject
import javax.inject.Singleton

import io.realm.Realm
import io.realm.RealmResults
import uk.co.agfsoft.skyweather.volley.BaseVolleyRequest
import uk.co.agfsoft.skyweather.volley.requests.GetByIdRequest
import uk.co.agfsoft.skyweather.volley.requests.GetByNameRequest


@Singleton
class Model @Inject
constructor(val context: Context) {

    private var realm: Realm? = null
    private var weatherCities: RealmResults<WeatherCity>? = null

    @Inject
    lateinit var getByNameRequest: GetByNameRequest
    @Inject
    lateinit var getByIdRequest: GetByIdRequest

    val favourites: List<WeatherCity>
        get() = ArrayList(weatherCities!!)

    init {
        initialiseRealm(context)
        loadFavourites()
    }

    private fun initialiseRealm(context: Context) {
        Realm.init(context)
        realm = Realm.getDefaultInstance()
    }

    private fun loadFavourites() {
        weatherCities = realm!!.where(WeatherCity::class.java).findAll()
    }

    fun findCities(searchQuery: String, listener: BaseVolleyRequest.Listener<FindCitiesResponse>) {
        getByNameRequest.setQuery(searchQuery)
        getByNameRequest.submit(context, false, listener)
    }

    fun getWeatherForCity(cityId: Int, listener: BaseVolleyRequest.Listener<RawWeatherCity>) {
        getByIdRequest.setQuery(cityId)
        getByIdRequest.submit(context, false, listener)
    }


    fun saveCity(weatherCity: WeatherCity) {
        var isCityAlreadySaved = false
        for (city in weatherCities!!) {
            if (city.id == weatherCity.id) {
                isCityAlreadySaved = true
                break
            }
        }
        if (!isCityAlreadySaved) {
            realm!!.beginTransaction()
            realm!!.copyToRealm(weatherCity)
            realm!!.commitTransaction()
            loadFavourites()
        }
    }
}
