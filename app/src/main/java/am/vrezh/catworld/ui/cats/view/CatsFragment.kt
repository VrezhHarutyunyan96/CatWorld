package am.vrezh.catworld.ui.cats.view

import am.vrezh.catworld.R
import am.vrezh.catworld.api.models.Cat
import am.vrezh.catworld.di.fragment.FragmentModule
import am.vrezh.catworld.ui.cats.presenter.CatsPresenter
import am.vrezh.catworld.ui.cats.view.adapter.CatsAdapter
import am.vrezh.catworld.ui.moxy.BaseMvpFragment
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.annotation.TargetApi
import android.app.Activity
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.paginate.Paginate
import kotlinx.android.synthetic.main.cats_fragment.*
import javax.inject.Inject

class CatsFragment :
    BaseMvpFragment(),
    CatsView,
    CatsAdapter.AddFavoriteListener,
    CatsAdapter.DownloadImageListener {

    @Inject
    @InjectPresenter
    lateinit var presenter: CatsPresenter

    @ProvidePresenter
    fun providePresenter(): CatsPresenter = presenter

    private var adapter: CatsAdapter = CatsAdapter(this, this)

    override fun onCreate(savedInstanceState: Bundle?) {
        activityComponent.plus(FragmentModule()).inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.cats_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setRecyclerView()
    }

    private fun setRecyclerView() {

        catsList.adapter = adapter
        val gridLayoutManager = GridLayoutManager(requireContext(), COLUMN_COUNT)
        catsList.layoutManager = gridLayoutManager
        val dividerItemDecoration = DividerItemDecoration(
            requireContext(),
            DividerItemDecoration.VERTICAL
        )
        catsList.addItemDecoration(dividerItemDecoration)

    }

    override fun addCats(catsList: List<Cat>) {
        adapter.addData(catsList)
    }

    override fun setPagination() {

        val catsPaginateCallback = object : Paginate.Callbacks {

            override fun onLoadMore() {
                presenter.loadNextPage()
            }

            override fun isLoading(): Boolean {
                return presenter.loadingInProgress
            }

            override fun hasLoadedAllItems(): Boolean {
                return presenter.hasLoadedAllItems
            }

        }

        Paginate.with(catsList, catsPaginateCallback)
            .addLoadingListItem(false)
            .setLoadingTriggerThreshold(TRIGGER_THRESHOLD)
            .build()

    }

    override fun showProgress() {
        progressBar.show()
    }

    override fun hideProgress() {
        progressBar.hide()
    }

    companion object {

        private const val COLUMN_COUNT = 1
        private const val TRIGGER_THRESHOLD = 1
        private const val MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1

        @JvmStatic
        fun newInstance() = CatsFragment().apply {
            arguments = Bundle().apply {}
        }

    }

    override fun addFavorite(imageUrl: String) {
        presenter.addFavorite(imageUrl, requireContext())
    }

    override fun downloadImage(imageUrl: String) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
            && Build.VERSION.SDK_INT < Build.VERSION_CODES.Q
        ) {
            askPermissions(imageUrl)
        } else {
            presenter.downloadImage(imageUrl, requireContext())
        }

    }

    @TargetApi(Build.VERSION_CODES.M)
    fun askPermissions(imageUrl: String) {
        if (context?.let {
                ContextCompat.checkSelfPermission(
                    it,
                    WRITE_EXTERNAL_STORAGE
                )
            } != PackageManager.PERMISSION_GRANTED) {
            handleRequestPermission()
        } else {
            presenter.downloadImage(imageUrl, view?.context!!)
        }
    }

    private fun handleRequestPermission() {

        // Permission is not granted
        // Should we show an explanation?
        val shouldShowRationale =
            shouldShowRequestPermissionRationale(context as Activity, WRITE_EXTERNAL_STORAGE)

        if (shouldShowRationale) {

            AlertDialog.Builder(context)
                .setTitle(R.string.permission_required)
                .setMessage(R.string.permission_required_long)
                .setPositiveButton(R.string.allow) { dialog, id ->
                    requestPermissions(
                        context as Activity,
                        arrayOf(WRITE_EXTERNAL_STORAGE),
                        MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE
                    )

                }
                .setNegativeButton(R.string.deny) { dialog, _ -> dialog.cancel() }
                .show()

        } else {

            requestPermissions(
                context as Activity,
                arrayOf(WRITE_EXTERNAL_STORAGE),
                MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE
            )

        }

    }

}