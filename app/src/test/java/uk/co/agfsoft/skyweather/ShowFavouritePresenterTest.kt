package uk.co.agfsoft.skyweather

import android.content.res.Resources

import com.android.volley.VolleyError
import com.google.gson.GsonBuilder

import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

import java.io.InputStreamReader

import model.Model
import model.RawWeatherCity
import uk.co.agfsoft.skyweather.presenter.ShowFavouritePresenter
import uk.co.agfsoft.skyweather.view.ShowFavouriteView
import uk.co.agfsoft.skyweather.volley.BaseVolleyRequest

import org.mockito.Matchers.any
import org.mockito.Matchers.anyInt
import org.mockito.Matchers.eq
import org.mockito.Mockito.doAnswer
import org.mockito.Mockito.mock
import org.mockito.Mockito.never
import org.mockito.Mockito.verify

/**
 * Created by drew on 06/11/2017.
 */

class ShowFavouritePresenterTest {

    @Mock
    internal var view: ShowFavouriteView? = null
    @Mock
    internal var model: Model? = null
    @Mock
    internal var resources: Resources? = null

    internal var presenter: ShowFavouritePresenter

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        presenter = object : ShowFavouritePresenter(view!!, model!!) {

        }
    }

    @Test
    fun shouldNotRequestCityWetaherWHenIdIsNotSet() {
        presenter.setCityId(0)
        presenter.viewReady()
        verify<Model>(model, never()).getWeatherForCity(anyInt(), any<BaseVolleyRequest.Listener<*>>(BaseVolleyRequest.Listener<*>::class.java))
    }

    @Test
    fun shouldRequestCityWetaherWHenIdIsSet() {
        presenter.setCityId(9999)
        presenter.viewReady()
        verify<Model>(model).getWeatherForCity(anyInt(), any<BaseVolleyRequest.Listener<*>>(BaseVolleyRequest.Listener<*>::class.java))
    }

    @Test
    fun shouldShowCityWeatherWHenSuccessful() {
        val response = loadRawCityWeatherResponse()
        presenter.setCityId(9999)
        doAnswer { invocation ->
            val listener = invocation.arguments[1] as BaseVolleyRequest.Listener<RawWeatherCity>
            listener.onSuccess(response)
            null
        }.`when`<Model>(model).getWeatherForCity(eq(9999), any<BaseVolleyRequest.Listener<*>>(BaseVolleyRequest.Listener<*>::class.java))

        presenter.viewReady()
        verify<ShowFavouriteView>(view).updateWeatherDisplay(response)
        verify<ShowFavouriteView>(view, never()).showError()
    }

    @Test
    fun shouldShowErrorWhenUnsuccessful() {
        val response = loadRawCityWeatherResponse()
        presenter.setCityId(9999)
        doAnswer { invocation ->
            val listener = invocation.arguments[1] as BaseVolleyRequest.Listener<RawWeatherCity>
            listener.onError(mock(VolleyError::class.java))
            null
        }.`when`<Model>(model).getWeatherForCity(eq(9999), any<BaseVolleyRequest.Listener<*>>(BaseVolleyRequest.Listener<*>::class.java))

        presenter.viewReady()
        verify<ShowFavouriteView>(view).showError()
        verify<ShowFavouriteView>(view, never()).updateWeatherDisplay(any(RawWeatherCity::class.java))
    }


    private fun loadRawCityWeatherResponse(): RawWeatherCity {
        val gson = GsonBuilder().create()
        val reader = InputStreamReader(javaClass.classLoader.getResourceAsStream("raw_city_response.json"))
        return gson.fromJson(reader, RawWeatherCity::class.java)
    }
}
