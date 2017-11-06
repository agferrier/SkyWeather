package uk.co.agfsoft.skyweather;

import android.content.res.Resources;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.io.InputStreamReader;

import uk.co.agfsoft.skyweather.model.Model;
import uk.co.agfsoft.skyweather.model.RawWeatherCity;
import uk.co.agfsoft.skyweather.presenter.ShowFavouritePresenter;
import uk.co.agfsoft.skyweather.presenter.ShowFavouritePresenterImpl;
import uk.co.agfsoft.skyweather.view.ShowFavouriteView;
import uk.co.agfsoft.skyweather.volley.BaseVolleyRequest;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

/**
 * Created by drew on 06/11/2017.
 */

public class ShowFavouritePresenterTest {

    @Mock
    ShowFavouriteView view;
    @Mock
    Model model;
    @Mock
    Resources resources;

    ShowFavouritePresenter presenter;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        presenter = new ShowFavouritePresenterImpl(view, model) {
        };
    }

    @Test
    public void shouldNotRequestCityWetaherWHenIdIsNotSet() {
        presenter.setCityId(0);
        presenter.viewReady();
        verify(model, never()).getWeatherForCity(anyInt(), any(BaseVolleyRequest.Listener.class));
    }

    @Test
    public void shouldRequestCityWetaherWHenIdIsSet() {
        presenter.setCityId(9999);
        presenter.viewReady();
        verify(model).getWeatherForCity(anyInt(), any(BaseVolleyRequest.Listener.class));
    }

    @Test
    public void shouldShowCityWeatherWHenSuccessful() {
        final RawWeatherCity response = loadRawCityWeatherResponse();
        presenter.setCityId(9999);
        doAnswer(new Answer<Void>() {
            public Void answer(InvocationOnMock invocation) {
                BaseVolleyRequest.Listener<RawWeatherCity> listener = (BaseVolleyRequest.Listener<RawWeatherCity>) invocation.getArguments()[1];
                listener.onSuccess(response);
                return null;
            }
        }).when(model).getWeatherForCity(eq(9999), any(BaseVolleyRequest.Listener.class));

        presenter.viewReady();
        verify(view).updateWeatherDisplay(response);
        verify(view, never()).showError();
    }

    @Test
    public void shouldShowErrorWhenUnsuccessful() {
        final RawWeatherCity response = loadRawCityWeatherResponse();
        presenter.setCityId(9999);
        doAnswer(new Answer<Void>() {
            public Void answer(InvocationOnMock invocation) {
                BaseVolleyRequest.Listener<RawWeatherCity> listener = (BaseVolleyRequest.Listener<RawWeatherCity>) invocation.getArguments()[1];
                listener.onError(mock(VolleyError.class));
                return null;
            }
        }).when(model).getWeatherForCity(eq(9999), any(BaseVolleyRequest.Listener.class));

        presenter.viewReady();
        verify(view).showError();
        verify(view, never()).updateWeatherDisplay(any(RawWeatherCity.class));
    }


    private RawWeatherCity loadRawCityWeatherResponse() {
        Gson gson = new GsonBuilder().create();
        InputStreamReader reader = new InputStreamReader(getClass().getClassLoader().getResourceAsStream("raw_city_response.json"));
        return gson.fromJson(reader, RawWeatherCity.class);
    }
}
