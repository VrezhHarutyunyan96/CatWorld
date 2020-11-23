package am.vrezh.catworld.ui.favorites.view

import am.vrezh.catworld.db.FavoriteCat
import am.vrezh.catworld.ui.moxy.BaseMvpView
import am.vrezh.catworld.ui.moxy.MvpProgressView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

interface FavoriteCatsView : BaseMvpView, MvpProgressView {

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun addFavoriteCats(favoriteCatsList: List<FavoriteCat>)

}