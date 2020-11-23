package am.vrezh.catworld.ui.cats.view

import am.vrezh.catworld.R
import am.vrezh.catworld.api.models.Cat
import am.vrezh.catworld.di.fragment.FragmentModule
import am.vrezh.catworld.ui.cats.presenter.CatsPresenter
import am.vrezh.catworld.ui.cats.view.adapter.CatsAdapter
import am.vrezh.catworld.ui.moxy.BaseMvpFragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
    CatsAdapter.AddFavoriteListener {

    @Inject
    @InjectPresenter
    lateinit var presenter: CatsPresenter

    @ProvidePresenter
    fun providePresenter(): CatsPresenter = presenter

    private var adapter: CatsAdapter = CatsAdapter(this)

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
        val gridLayoutManager = GridLayoutManager(requireContext(), 1)
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
            .setLoadingTriggerThreshold(1)
            .build()

    }

    override fun showProgress() {
        progressBar.show()
    }

    override fun hideProgress() {
        progressBar.hide()
    }

    companion object {

        @JvmStatic
        fun newInstance() = CatsFragment().apply {
            arguments = Bundle().apply {}
        }

    }

    override fun addFavorite(imageUrl: String) {
        presenter.addFavorite(imageUrl, requireContext())
    }

}