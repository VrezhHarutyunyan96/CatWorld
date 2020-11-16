package am.vrezh.catworld.di.fragment


import am.vrezh.catworld.ui.fragments.CatsFragment
import am.vrezh.catworld.ui.fragments.FavoriteCatsFragment
import dagger.Subcomponent

@Subcomponent(modules = [FragmentModule::class])
interface FragmentComponent {

    fun inject(fragment: CatsFragment)
    fun inject(fragment: FavoriteCatsFragment)

}
