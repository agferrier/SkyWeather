package uk.co.agfsoft.skyweather

import android.content.res.Resources

import com.android.volley.VolleyError
import com.google.gson.GsonBuilder

import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

import java.io.InputStreamReader

import model.FindCitiesResponse
import model.Model
import uk.co.agfsoft.skyweather.presenter.AddFavouritePresenter
import uk.co.agfsoft.skyweather.view.AddFavouriteView
import uk.co.agfsoft.skyweather.volley.BaseVolleyRequest

import org.mockito.Matchers.any
import org.mockito.Matchers.eq
import org.mockito.Mockito.doAnswer
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

/**
 * Created by drew on 06/11/2017.
 */

class AddFavouritePresenterTest {

    @Mock
    internal var view: AddFavouriteView? = null
    @Mock
    internal var model: Model? = null
    @Mock
    internal var resources: Resources? = null

    internal var presenter: AddFavouritePresenter

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        presenter = AddFavouritePresenter(view!!, model!!)
    }

    @Test
    fun shouldRequestModelToFindCitiesWHenStartSearchCalled() {
        presenter.startSearch("Eastbourne")
        verify<Model>(model).findCities(eq("Eastbourne"), any<BaseVolleyRequest.Listener<*>>(BaseVolleyRequest.Listener<*>::class.java))
    }

    @Test
    fun shouldUpdateViewResultsIfFindIsSuccessful() {
        val response = loadFindCitiesResponse()
        doAnswer { invocation ->
            val listener = invocation.arguments[1] as BaseVolleyRequest.Listener<FindCitiesResponse>
            listener.onSuccess(response)
            null
        }.`when`<Model>(model).findCities(eq("Eastbourne"), any<BaseVolleyRequest.Listener<*>>(BaseVolleyRequest.Listener<*>::class.java))

        presenter.startSearch("Eastbourne")
        verify<AddFavouriteView>(view).updateResults(any<List<*>>(List<*>::class.java))

    }

    @Test
    fun shouldShowErrorWhenFindCitiesFails() {
        doAnswer { invocation ->
            val listener = invocation.arguments[1] as BaseVolleyRequest.Listener<FindCitiesResponse>
            listener.onError(mock(VolleyError::class.java))
            null
        }.`when`<Model>(model).findCities(eq("Eastbourne"), any<BaseVolleyRequest.Listener<*>>(BaseVolleyRequest.Listener<*>::class.java))

        presenter.startSearch("Eastbourne")
        verify<AddFavouriteView>(view).showError()
    }

    @Test
    fun shouldSaveCityAndDismissViewhenCitySelected() {
        val city = model.WeatherCity()

        presenter.citySelected(city)
        verify<Model>(model).saveCity(city)
        verify<AddFavouriteView>(view).dismiss()
    }

    private fun loadFindCitiesResponse(): FindCitiesResponse {
        val gson = GsonBuilder().create()
        val reader = InputStreamReader(javaClass.classLoader.getResourceAsStream("find_cities_response.json"))
        return gson.fromJson(reader, FindCitiesResponse::class.java)
    }
}
