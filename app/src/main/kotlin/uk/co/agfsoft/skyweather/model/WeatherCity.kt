package model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey


open class WeatherCity : RealmObject {
    @PrimaryKey
    var id: Int = 0
        internal set
    var name: String? = null
        internal set
    var country: String? = null
        internal set
    var latitiude: Double = 0.toDouble()
        internal set
    var longitude: Double = 0.toDouble()
        internal set

    constructor() {}

    constructor(rawWeatherCity: RawWeatherCity) {
        this.id = rawWeatherCity.id
        this.name = rawWeatherCity.name
        this.country = rawWeatherCity.country
        this.longitude = rawWeatherCity.longitude
        this.latitiude = rawWeatherCity.latitude
    }


}
