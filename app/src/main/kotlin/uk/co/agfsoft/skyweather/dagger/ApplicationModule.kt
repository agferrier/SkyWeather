package dagger

import android.content.Context
import uk.co.agfsoft.skyweather.SkyWeatherApplication
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import uk.co.agfsoft.skyweather.volley.OkHttp3Stack
import javax.inject.Singleton

/**
 * Created by afa28 on 05/02/2018.
 */
@Module
class ApplicationModule() {

    private val interceptors = listOf(StethoInterceptor())


    @Provides
    fun providesContext(application: SkyWeatherApplication): Context {
        return application.applicationContext
    }

    @Provides
    @Singleton
    fun providesRequestQueue(application: SkyWeatherApplication): RequestQueue {
        return Volley.newRequestQueue(application, OkHttp3Stack(interceptors));
    }

    @Provides
    @Singleton
    fun providesGson(): Gson {
        return GsonBuilder().create();
    }

}