package am.vrezh.catworld.ui.cats.presenter

import am.vrezh.catworld.api.CatsApi
import am.vrezh.catworld.rx.RxSchedulers
import am.vrezh.catworld.ui.cats.view.CatsView
import am.vrezh.catworld.ui.moxy.BaseMvpPresenter
import com.arellomobile.mvp.InjectViewState
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

const val CATS_PAGE_SIZE = 10

@InjectViewState
class CatsPresenter @Inject internal constructor(
    private val catsApi: CatsApi,
    private val rxSchedulers: RxSchedulers,
) : BaseMvpPresenter<CatsView>() {

    var loadingInProgress = false
    var hasLoadedAllItems = false
    private var pageNumber = 0

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.setPagination()
    }

    fun loadNextPage() {

        loadingInProgress = true

        catsApi.getCats(
            "med",
            "RANDOM",
            CATS_PAGE_SIZE,
            pageNumber,
            "json"
        )
            .delay(500, TimeUnit.MILLISECONDS)
            .compose(rxSchedulers.ioToMain())
            .progress()
            .subscribe(
                { result ->

                    loadingInProgress = false
                    hasLoadedAllItems = result.size < CATS_PAGE_SIZE
                    pageNumber++

                    viewState.addCats(result)

                },
                { throwable ->
                    loadingInProgress = false
                    hasLoadedAllItems = true
                    Timber.e(throwable as Throwable, "Error loading cats list!")
                }
            ).unsubscribeOnDestroy()

    }

}
