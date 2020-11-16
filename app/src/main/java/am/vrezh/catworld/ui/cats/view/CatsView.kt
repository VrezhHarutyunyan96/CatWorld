package am.vrezh.catworld.ui.cats.view

import am.vrezh.catworld.api.models.Cat
import am.vrezh.catworld.ui.moxy.BaseMvpView
import am.vrezh.catworld.ui.moxy.MvpProgressView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

interface CatsView : BaseMvpView, MvpProgressView {

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showCats(catsList: List<Cat>)

}