package uk.co.agfsoft.skyweather.model;


import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;



public class WeatherCity extends RealmObject {
    @PrimaryKey
    int id;
    String name;
    String country;
    double latitiude;
    double longitude;

    public WeatherCity() {
    }

    public WeatherCity(RawWeatherCity rawWeatherCity) {
        this.id = rawWeatherCity.getId();
        this.name = rawWeatherCity.getName();
        this.country = rawWeatherCity.getCountry();
        this.longitude = rawWeatherCity.getLongitude();
        this.latitiude = rawWeatherCity.getLatitude();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCountry() {
        return country;
    }

    public double getLatitiude() {
        return latitiude;
    }

    public double getLongitude() {
        return longitude;
    }


}
