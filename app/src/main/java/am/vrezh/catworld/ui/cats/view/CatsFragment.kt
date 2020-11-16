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
import androidx.recyclerview.widget.LinearLayoutManager
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import kotlinx.android.synthetic.main.cats_fragment.*
import javax.inject.Inject

class CatsFragment : BaseMvpFragment(), CatsView {

    @Inject
    @InjectPresenter
    lateinit var presenter: CatsPresenter

    private var adapter: CatsAdapter = CatsAdapter()

    @ProvidePresenter
    fun providePresenter(): CatsPresenter = presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        activityComponent.plus(FragmentModule()).inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.cats_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        catsList.adapter = adapter
        val mLayoutManager = LinearLayoutManager(requireContext())
        catsList.layoutManager = mLayoutManager
    }

    override fun showCats(catsList: List<Cat>) {
        adapter.catsList = catsList
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

}