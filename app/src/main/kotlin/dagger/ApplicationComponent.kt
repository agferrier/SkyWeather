package dagger

import application.SkyWeatherApplication
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

/**
 * Created by afa28 on 05/02/2018.
 */
@Singleton
@Component(modules = [
    AndroidSupportInjectionModule::class,
    ApplicationModule::class,
    ViewModule::class,
    BuilderModule::class])

interface ApplicationComponent {
    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: SkyWeatherApplication): Builder

        fun build(): ApplicationComponent
    }

    fun inject(application: SkyWeatherApplication)
}