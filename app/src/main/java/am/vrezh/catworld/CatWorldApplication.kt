package am.vrezh.catworld

import am.vrezh.catworld.db.CatsWorldDatabase
import am.vrezh.catworld.di.app.AppComponent
import am.vrezh.catworld.di.app.AppContextModule
import am.vrezh.catworld.di.app.DaggerAppComponent
import am.vrezh.catworld.utils.Prefs
import androidx.multidex.MultiDexApplication
import androidx.room.Room

class CatWorldApplication : MultiDexApplication() {

    lateinit var appComponent: AppComponent
        private set

    override fun onCreate() {
        super.onCreate()
        initAppComponent()
        initDatabase()
        Prefs.init(this)
    }

    private fun initAppComponent() {
        appComponent = DaggerAppComponent.builder().appContextModule(AppContextModule(this)).build()
    }

    private fun initDatabase() {
        catWorldDb = Room.databaseBuilder(
            applicationContext,
            CatsWorldDatabase::class.java,
            "cat-world-database"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    companion object {
        lateinit var catWorldDb: CatsWorldDatabase
    }

}