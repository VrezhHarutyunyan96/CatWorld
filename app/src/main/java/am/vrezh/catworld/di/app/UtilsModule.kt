package am.vrezh.catworld.di.app

import am.vrezh.catworld.rx.AppRxSchedulers
import am.vrezh.catworld.rx.RxSchedulers
import dagger.Module
import dagger.Provides

@Module
class UtilsModule {

    @Provides
    internal fun provideRxSchedulers(): RxSchedulers = AppRxSchedulers()
}
