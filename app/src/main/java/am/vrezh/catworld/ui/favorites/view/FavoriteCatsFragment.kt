package am.vrezh.catworld.ui.favorites.view

import am.vrezh.catworld.R
import am.vrezh.catworld.db.FavoriteCat
import am.vrezh.catworld.di.fragment.FragmentModule
import am.vrezh.catworld.ui.favorites.presenter.FavoriteCatsPresenter
import am.vrezh.catworld.ui.favorites.view.adapter.FavoriteCatsAdapter
import am.vrezh.catworld.ui.moxy.BaseMvpFragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import kotlinx.android.synthetic.main.cats_fragment.*
import javax.inject.Inject

class FavoriteCatsFragment : BaseMvpFragment(), FavoriteCatsView {

    @Inject
    @InjectPresenter
    lateinit var presenter: FavoriteCatsPresenter

    private var adapter: FavoriteCatsAdapter = FavoriteCatsAdapter()

    @ProvidePresenter
    fun providePresenter(): FavoriteCatsPresenter = presenter

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

    override fun addFavoriteCats(favoriteCatsList: List<FavoriteCat>) {
        adapter.addData(favoriteCatsList)
    }

    override fun showProgress() {
        progressBar.show()
    }

    override fun hideProgress() {
        progressBar.hide()
    }

    companion object {

        @JvmStatic
        fun newInstance() = FavoriteCatsFragment().apply {
            arguments = Bundle().apply {}
        }

    }

}