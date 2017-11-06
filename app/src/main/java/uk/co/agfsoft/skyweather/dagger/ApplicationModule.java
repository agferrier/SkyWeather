package uk.co.agfsoft.skyweather.dagger;

import android.app.Application;
import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;



@Module
public class ApplicationModule {

    private Application application;

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
        return Volley.newRequestQueue(application);
    }

    @Provides
    @Singleton
    Gson provideGson() {
        return new GsonBuilder().create();
    }

}
