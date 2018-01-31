package uk.co.agfsoft.skyweather.application;

import android.app.Application;

import com.facebook.stetho.Stetho;

import uk.co.agfsoft.skyweather.dagger.ApplicationComponent;
import uk.co.agfsoft.skyweather.dagger.ApplicationModule;
import uk.co.agfsoft.skyweather.dagger.DaggerApplicationComponent;
import uk.co.agfsoft.skyweather.dagger.InjectableApplication;



public class SkyWeatherApplication extends Application implements InjectableApplication {

    private ApplicationComponent applicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        getApplicationComponent().inject(this);
        Stetho.initializeWithDefaults(this);
    }

    @Override
    public ApplicationComponent getApplicationComponent() {
        if (applicationComponent == null) {
            applicationComponent = DaggerApplicationComponent
                    .builder()
                    .applicationModule(new ApplicationModule(this))
                    .build();
        }
        return applicationComponent;
    }

}
