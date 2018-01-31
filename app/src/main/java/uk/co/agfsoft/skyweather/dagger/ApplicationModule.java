package uk.co.agfsoft.skyweather.dagger;

import android.app.Application;
import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Arrays;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Interceptor;
import uk.co.agfsoft.skyweather.volley.OkHttp3Stack;


@Module
public class ApplicationModule {

    private Application application;

    private Interceptor[] interceptors = {new StethoInterceptor()};

    public ApplicationModule(Application application) {
        this.application = application;
    }

    @Provides
    Application provideApplication() {
        return application;
    }

    @Provides
    Context provideContext() {
        return application.getApplicationContext();
    }

    @Provides
    @Singleton
    RequestQueue provideRequestQueue() {
        return Volley.newRequestQueue(application, new OkHttp3Stack(Arrays.asList(interceptors)));
    }

    @Provides
    @Singleton
    Gson provideGson() {
        return new GsonBuilder().create();
    }

}
