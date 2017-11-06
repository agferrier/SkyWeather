package uk.co.agfsoft.skyweather.dagger;

import android.app.Application;

import javax.inject.Singleton;

import dagger.Component;
import uk.co.agfsoft.skyweather.application.SkyWeatherApplication;



@Component (modules = {ApplicationModule.class})
@Singleton

public interface ApplicationComponent {

    ActivityComponent plus(ActivityModule activityModule);

    void inject(SkyWeatherApplication skyWeatherApplication);
}

