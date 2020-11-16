package am.vrezh.catworld

import am.vrezh.catworld.di.app.AppComponent
import am.vrezh.catworld.di.app.AppContextModule
import am.vrezh.catworld.di.app.DaggerAppComponent
import androidx.multidex.MultiDexApplication

class CatWorldApplication : MultiDexApplication() {

    lateinit var appComponent: AppComponent
        private set

    override fun onCreate() {
        super.onCreate()
        initAppComponent()

    }

    private fun initAppComponent() {
        appComponent = DaggerAppComponent.builder().appContextModule(AppContextModule(this)).build()
    }

}