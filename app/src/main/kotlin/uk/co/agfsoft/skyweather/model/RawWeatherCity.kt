package model

import com.google.gson.annotations.SerializedName

import io.realm.annotations.PrimaryKey


class RawWeatherCity {


    private val coord: Coord? = null
    private val weather: Array<Weather>? = null
    private val base: String? = null
    private val main: Main? = null
    private val wind: Wind? = null
    private val clouds: Clouds? = null
    private val rain: Rain? = null
    private val snow: Snow? = null
    private val dt: Int = 0
    private val sys: Sys? = null
    @PrimaryKey
    val id: Int = 0
    val name: String? = null
    private val cod: String? = null


    val windSpeed: Double
        get() = wind!!.speed

    val country: String?
        get() = sys!!.country

    val windDirection: Int
        get() = wind!!.deg.toInt()

    val longitude: Double
        get() = coord!!.lon

    val latitude: Double
        get() = coord!!.lat


    protected class Coord {
        internal var lon: Double = 0.toDouble()
        internal var lat: Double = 0.toDouble()
    }

    protected class Weather {
        internal var id: Int = 0
        internal var main: String? = null
        internal var description: String? = null
        internal var icon: String? = null
    }

    protected class Main {
        internal var temp: Double = 0.toDouble()
        internal var pressure: Int = 0
        internal var humidity: Int = 0
        internal var temp_min: Double = 0.toDouble()
        internal var temp_max: Double = 0.toDouble()
        internal var sea_level: Int = 0
        internal var grnd_level: Int = 0
    }

    protected class Wind {
        internal var speed: Double = 0.toDouble()
        internal var deg: Double = 0.toDouble()
    }

    protected class Clouds {
        internal var all: String? = null
    }

    protected class Rain {
        @SerializedName("rain.3h")
        internal var rain3h: String? = null
    }

    protected class Snow {
        @SerializedName("snow.3h")
        internal var snow3h: String? = null
    }

    protected class Sys {
        internal var type: Int = 0
        internal var id: Int = 0
        internal var message: String? = null
        internal var country: String? = null
        internal var sunrise: Int = 0
        internal var sunset: Int = 0
    }
}
