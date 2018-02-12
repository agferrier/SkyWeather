package model

class FindCitiesResponse {
    internal var message: String? = null
    internal var cod: String? = null
    internal var count: Int = 0
    var list: Array<RawWeatherCity>? = null
}
