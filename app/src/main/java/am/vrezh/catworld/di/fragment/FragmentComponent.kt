package am.vrezh.catworld.di.fragment


import am.vrezh.catworld.ui.cats.view.CatsFragment
import am.vrezh.catworld.ui.favorites.view.FavoriteCatsFragment
import dagger.Subcomponent

@Subcomponent(modules = [FragmentModule::class])
interface FragmentComponent {

    fun inject(fragment: CatsFragment)
    fun inject(fragment: FavoriteCatsFragment)

}
