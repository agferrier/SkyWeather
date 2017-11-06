package uk.co.agfsoft.skyweather.model;



public class FindCitiesResponse {
    String message;
    String cod;
    int count;
    RawWeatherCity[] list;

    public RawWeatherCity[] getList() {
        return list;
    }

}
