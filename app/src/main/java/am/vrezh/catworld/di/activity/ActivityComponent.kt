package am.vrezh.catworld.di.activity

import am.vrezh.catworld.di.fragment.FragmentComponent
import am.vrezh.catworld.di.fragment.FragmentModule
import am.vrezh.catworld.ui.main.MainActivity
import dagger.Subcomponent

@ActivityScope
@Subcomponent(modules = [ActivityModule::class])
interface ActivityComponent {

    operator fun plus(module: FragmentModule): FragmentComponent

    fun inject(baseActivity: MainActivity)

}
