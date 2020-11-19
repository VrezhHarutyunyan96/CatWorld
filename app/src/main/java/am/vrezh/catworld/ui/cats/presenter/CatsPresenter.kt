package am.vrezh.catworld.ui.cats.presenter

import am.vrezh.catworld.api.CatsApi
import am.vrezh.catworld.api.models.Cat
import am.vrezh.catworld.rx.RxSchedulers
import am.vrezh.catworld.ui.cats.view.CatsView
import am.vrezh.catworld.ui.moxy.BaseMvpPresenter
import com.arellomobile.mvp.InjectViewState
import timber.log.Timber
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@InjectViewState
class CatsPresenter @Inject internal constructor(
    private val catsApi: CatsApi,
    private val rxSchedulers: RxSchedulers,
) : BaseMvpPresenter<CatsView>() {

    private val cats = ArrayList<Cat>()

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        loadCatsList()
    }

    private fun loadCatsList() {

        catsApi.getCats(
            "med",
            "RANDOM",
            10,
            0,
            "json"
        )
            .delay(500, TimeUnit.MILLISECONDS)
            .compose(rxSchedulers.ioToMain())
            .progress()
            .subscribe(
                { result ->
                    cats.clear()
                    cats.addAll(result)
                    viewState.showCats(cats)
                },
                { throwable ->
                    Timber.e(throwable as Throwable, "Error loading cats list!")
                }
            ).unsubscribeOnDestroy()
    }

}
