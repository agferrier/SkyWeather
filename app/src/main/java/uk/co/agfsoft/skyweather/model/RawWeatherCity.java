package uk.co.agfsoft.skyweather.model;

import com.google.gson.annotations.SerializedName;

import io.realm.annotations.PrimaryKey;



public class RawWeatherCity {


    private Coord coord;
    private Weather[] weather;
    private String base;
    private Main main;
    private Wind wind;
    private Clouds clouds;
    private Rain rain;
    private Snow snow;
    private int dt;
    private Sys sys;
    @PrimaryKey
    private int id;
    private String name;
    private String cod;


    public double getWindSpeed() {
        return wind.speed;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCountry() {
        return sys.country;
    }

    public int getWindDirection() {
        return (int)wind.deg;
    }

    public double getLongitude() {
        return coord.lon;
    }

    public double getLatitude() {
        return coord.lat;
    }


    protected static class Coord {
        double lon;
        double lat;
    }

    protected static class Weather {
        int id;
        String main;
        String description;
        String icon;
    }

    protected static class Main {
        double temp;
        int pressure;
        int humidity;
        double temp_min;
        double temp_max;
        int sea_level;
        int grnd_level;
    }

    protected static class Wind {
        double speed;
        double deg;
    }

    protected static class Clouds {
        String all;
    }

    protected static class Rain {
        @SerializedName("rain.3h")
        String rain3h;
    }

    protected static class Snow {
        @SerializedName("snow.3h")
        String snow3h;
    }

    protected static class Sys {
        int type;
        int id;
        String message;
        String country;
        int sunrise;
        int sunset;
    }
}
