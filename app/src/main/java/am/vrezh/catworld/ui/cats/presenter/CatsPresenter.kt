package am.vrezh.catworld.ui.cats.presenter

import am.vrezh.catworld.CatWorldApplication
import am.vrezh.catworld.api.CatsApi
import am.vrezh.catworld.db.FavoriteCat
import am.vrezh.catworld.rx.RxSchedulers
import am.vrezh.catworld.ui.cats.view.CatsView
import am.vrezh.catworld.ui.moxy.BaseMvpPresenter
import am.vrezh.catworld.utils.Prefs
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import com.arellomobile.mvp.InjectViewState
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import io.reactivex.rxjava3.core.Observable
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

    fun addFavorite(imageUrl: String, context: Context) {

        Glide.with(context)
            .asBitmap()
            .load(imageUrl)
            .into(object : CustomTarget<Bitmap>() {

                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {

                    val imageName = imageUrl.substring(imageUrl.lastIndexOf("/") + 1)
                    val destFile = saveFile(resource, context, imageName)
                    Observable.fromCallable {
                        CatWorldApplication.catWorldDb.favoriteCatDao()
                            .insert(FavoriteCat(destFile))
                    }
                        .compose(rxSchedulers.ioToMain())
                        .progress()
                        .subscribe(
                            {
                                Prefs.setFavoriteChanged(true)
                            },
                            { throwable ->
                                Timber.e(throwable as Throwable, "Error adding to favorite")
                            }
                        )
                        .unsubscribeOnDestroy()

                }

                override fun onLoadCleared(placeholder: Drawable?) {}

            })


    }

    private fun saveFile(bitmap: Bitmap, context: Context, imageName: String): String {

        val destFileName = "favorite_$imageName"
        val fos = context.openFileOutput(destFileName, Context.MODE_PRIVATE)

        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
        fos.close()

        return context.getFileStreamPath(destFileName).absolutePath

    }

}
