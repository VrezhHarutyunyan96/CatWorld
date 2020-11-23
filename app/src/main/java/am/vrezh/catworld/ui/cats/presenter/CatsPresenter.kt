package am.vrezh.catworld.ui.cats.presenter

import am.vrezh.catworld.CatWorldApplication
import am.vrezh.catworld.api.CatsApi
import am.vrezh.catworld.db.FavoriteCat
import am.vrezh.catworld.rx.RxSchedulers
import am.vrezh.catworld.ui.cats.view.CatsView
import am.vrezh.catworld.ui.moxy.BaseMvpPresenter
import android.content.Context
import android.util.Log
import com.arellomobile.mvp.InjectViewState
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import timber.log.Timber
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
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
            .asFile()
            .load(imageUrl)
            .onlyRetrieveFromCache(true)
            .listener(object : RequestListener<File> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<File>?,
                    isFirstResource: Boolean
                ): Boolean {
                    Log.d("LOAD_FAILED", "FAILED")
                    return false
                }

                override fun onResourceReady(
                    resource: File?,
                    model: Any?,
                    target: Target<File>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    Log.d("RECOURCE_READY", "READY")
                    if (resource?.exists() == true) {
                        val destFile = copyFile(resource?.name!!)
                        CatWorldApplication.catWorldDb.favoriteCatDao()
                            .insert(FavoriteCat(destFile.toInt(), destFile))
                    }
                    return false
                }

            })


    }

    private fun copyFile(source: String): String {

        val fis = FileInputStream(source)
        val bufferLength = 1024
        val buffer = ByteArray(bufferLength)
        val destFile = "favorite$source"
        val fos = FileOutputStream(destFile)
        val bos = BufferedOutputStream(fos, bufferLength)
        var read = fis.read(buffer)
        while (read != -1) {
            bos.write(buffer, 0, read)
            read = fis.read(buffer) // if read value is -1, it escapes loop.
        }
        fis.close()
        bos.flush()

        bos.close()

        return destFile

    }

}
