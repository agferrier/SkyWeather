package uk.co.agfsoft.skyweather.volley.requests;

import com.android.volley.Request;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import uk.co.agfsoft.skyweather.BuildConfig;
import uk.co.agfsoft.skyweather.model.FindCitiesResponse;
import uk.co.agfsoft.skyweather.model.RawWeatherCity;
import uk.co.agfsoft.skyweather.volley.BaseVolleyRequest;



public class GetByNameRequest extends BaseVolleyRequest<FindCitiesResponse> {

    private static final String Q = "q";
    private static final String APPID = "APPID";
    public static final String TYPE = "type";
    public static final String LIKE = "like";

    private Map<String, String> queryParams;

    @Inject
    public GetByNameRequest() {
    }

    @Override
    protected String getUrl() {
        return BuildConfig.OPEN_WEATHER_FIND_URL;
    }

    @Override
    protected int getMethod() {
        return Request.Method.GET;
    }

    @Override
    protected Class getResponseClass() {
        return FindCitiesResponse.class;
    }


    @Override
    protected Map<String, String> getQueryParams() {
        return queryParams;
    }

    public void setQuery(String cityName) {
        queryParams = new HashMap<>(3);
        queryParams.put(Q, cityName);
        queryParams.put(TYPE, LIKE);
        queryParams.put(APPID, BuildConfig.OPEN_WEATHER_API_KEY);
    }
}
