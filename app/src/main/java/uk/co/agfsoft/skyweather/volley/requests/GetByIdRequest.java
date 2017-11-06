package uk.co.agfsoft.skyweather.volley.requests;

import com.android.volley.Request;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import uk.co.agfsoft.skyweather.BuildConfig;
import uk.co.agfsoft.skyweather.model.RawWeatherCity;
import uk.co.agfsoft.skyweather.volley.BaseVolleyRequest;



public class GetByIdRequest extends BaseVolleyRequest<RawWeatherCity> {

    private static final String ID = "id";
    private static final String APPID = "APPID";

    private Map<String, String> queryParams;

    @Inject
    public GetByIdRequest() {
    }

    @Override
    protected String getUrl() {
        return BuildConfig.OPEN_WEATHER_DATA_URL;
    }

    @Override
    protected int getMethod() {
        return Request.Method.GET;
    }

    @Override
    protected Class getResponseClass() {
        return RawWeatherCity.class;
    }


    @Override
    protected Map<String, String> getQueryParams() {
        return queryParams;
    }

    public void setQuery(int cityId) {
        queryParams = new HashMap<>(3);
        queryParams.put(ID, String.valueOf(cityId));
        queryParams.put(APPID, BuildConfig.OPEN_WEATHER_API_KEY);
    }
}
