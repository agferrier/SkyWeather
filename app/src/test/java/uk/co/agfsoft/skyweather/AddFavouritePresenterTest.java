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
import java.util.List;

import uk.co.agfsoft.skyweather.model.FindCitiesResponse;
import uk.co.agfsoft.skyweather.model.Model;
import uk.co.agfsoft.skyweather.model.WeatherCity;
import uk.co.agfsoft.skyweather.presenter.AddFavouritePresenter;
import uk.co.agfsoft.skyweather.presenter.AddFavouritePresenterImpl;
import uk.co.agfsoft.skyweather.view.AddFavouriteView;
import uk.co.agfsoft.skyweather.volley.BaseVolleyRequest;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Created by drew on 06/11/2017.
 */

public class AddFavouritePresenterTest {

    @Mock
    AddFavouriteView view;
    @Mock
    Model model;
    @Mock
    Resources resources;

    AddFavouritePresenter presenter;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        presenter = new AddFavouritePresenterImpl(view, model);
    }

    @Test
    public void shouldRequestModelToFindCitiesWHenStartSearchCalled() {
        presenter.startSearch("Eastbourne");
        verify(model).findCities(eq("Eastbourne"), any(BaseVolleyRequest.Listener.class));
    }

    @Test
    public void shouldUpdateViewResultsIfFindIsSuccessful() {
        final FindCitiesResponse response = loadFindCitiesResponse();
        doAnswer(new Answer<Void>() {
            public Void answer(InvocationOnMock invocation) {
                BaseVolleyRequest.Listener<FindCitiesResponse> listener = (BaseVolleyRequest.Listener<FindCitiesResponse>) invocation.getArguments()[1];
                listener.onSuccess(response);
                return null;
            }
        }).when(model).findCities(eq("Eastbourne"), any(BaseVolleyRequest.Listener.class));

        presenter.startSearch("Eastbourne");
        verify(view).updateResults(any(List.class));

    }

    @Test
    public void shouldShowErrorWhenFindCitiesFails() {
        doAnswer(new Answer<Void>() {
            public Void answer(InvocationOnMock invocation) {
                BaseVolleyRequest.Listener<FindCitiesResponse> listener = (BaseVolleyRequest.Listener<FindCitiesResponse>) invocation.getArguments()[1];
                listener.onError(mock(VolleyError.class));
                return null;
            }
        }).when(model).findCities(eq("Eastbourne"), any(BaseVolleyRequest.Listener.class));

        presenter.startSearch("Eastbourne");
        verify(view).showError();
    }

    @Test
    public void shouldSaveCityAndDismissViewhenCitySelected() {
        WeatherCity city = new WeatherCity();

        presenter.citySelected(city);
        verify(model).saveCity(city);
        verify(view).dismiss();
    }

    private FindCitiesResponse loadFindCitiesResponse() {
        Gson gson = new GsonBuilder().create();
        InputStreamReader reader = new InputStreamReader(getClass().getClassLoader().getResourceAsStream("find_cities_response.json"));
        return gson.fromJson(reader, FindCitiesResponse.class);
    }
}
