package am.vrezh.catworld.di.app

import am.vrezh.catworld.api.CatsApi
import am.vrezh.catworld.rx.RxSchedulers
import am.vrezh.catworld.di.activity.ActivityComponent
import am.vrezh.catworld.di.activity.ActivityModule

import dagger.Component

@AppScope
@Component(modules = [NetworkModule::class, AppContextModule::class, UtilsModule::class])
interface AppComponent {

    operator fun plus(activityModule: ActivityModule): ActivityComponent

    fun rxSchedulers(): RxSchedulers

    fun apiService(): CatsApi

}
