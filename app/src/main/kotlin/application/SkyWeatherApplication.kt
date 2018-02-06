package application

import android.app.Activity
import android.app.Application

import com.facebook.stetho.Stetho
import dagger.ApplicationComponent
import dagger.DaggerApplicationComponent

import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import javax.inject.Inject


class SkyWeatherApplication : Application(), HasActivityInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

    override fun onCreate() {
        super.onCreate()
        DaggerApplicationComponent
                .builder()
                .application(this)
                .build()
                .inject(this)

        Stetho.initializeWithDefaults(this)
    }

    override fun activityInjector(): AndroidInjector<Activity>? {
        return dispatchingAndroidInjector
    }
}
