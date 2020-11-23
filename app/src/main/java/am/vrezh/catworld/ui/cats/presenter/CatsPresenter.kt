package am.vrezh.catworld.ui.cats.presenter

import am.vrezh.catworld.CatWorldApplication
import am.vrezh.catworld.api.CatsApi
import am.vrezh.catworld.db.FavoriteCat
import am.vrezh.catworld.rx.RxSchedulers
import am.vrezh.catworld.ui.cats.view.CatsView
import am.vrezh.catworld.ui.moxy.BaseMvpPresenter
import am.vrezh.catworld.utils.Prefs
import android.app.DownloadManager
import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.widget.Toast
import com.arellomobile.mvp.InjectViewState
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import io.reactivex.rxjava3.core.Observable
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
    private var msg: String? = ""
    private var lastMsg = ""

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

    fun downloadImage(url: String, context: Context) {
        val directory = File(Environment.DIRECTORY_PICTURES)

        if (!directory.exists()) {
            directory.mkdirs()
        }

        val downloadManager =
            context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

        val downloadUri = Uri.parse(url)

        val request = DownloadManager.Request(downloadUri).apply {
            setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
                .setAllowedOverRoaming(false)
                .setTitle(url.substring(url.lastIndexOf("/") + 1))
                .setDescription("")
                .setDestinationInExternalPublicDir(
                    directory.toString(),
                    url.substring(url.lastIndexOf("/") + 1)
                )
        }

        val downloadId = downloadManager.enqueue(request)
        val query = DownloadManager.Query().setFilterById(downloadId)

        Thread {
            var downloading = true
            while (downloading) {

                val cursor: Cursor = downloadManager.query(query)
                cursor.moveToFirst()
                if (cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)) == DownloadManager.STATUS_SUCCESSFUL) {
                    downloading = false
                }
                val status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))
                msg = statusMessage(url, directory, status)
                if (msg != lastMsg) {

                    Handler(Looper.getMainLooper()).post {
                        Toast.makeText(
                            context,
                            msg,
                            Toast.LENGTH_LONG
                        ).show()
                    }

                    lastMsg = msg ?: ""

                }

                cursor.close()

            }

        }.start()

    }

    private fun statusMessage(url: String, directory: File, status: Int): String? {
        var msg = ""
        msg = when (status) {
            DownloadManager.STATUS_FAILED -> "Download has been failed, please try again"
            DownloadManager.STATUS_PAUSED -> "Paused"
            DownloadManager.STATUS_PENDING -> "Pending"
            DownloadManager.STATUS_RUNNING -> "Downloading..."
            DownloadManager.STATUS_SUCCESSFUL -> "Image downloaded successfully in $directory" + File.separator + url.substring(
                url.lastIndexOf("/") + 1
            )
            else -> "There's nothing to download"
        }
        return msg
    }

}