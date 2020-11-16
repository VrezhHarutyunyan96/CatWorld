package am.vrezh.catworld.ui.moxy

import am.vrezh.catworld.di.activity.ActivityComponent
import am.vrezh.catworld.ui.main.MainActivity

open class BaseMvpFragment : MvpAppCompatFragment(), BaseMvpView {

    protected val activityComponent: ActivityComponent
        get() = mainActivity.getActivityComponent()

    protected val mainActivity: MainActivity
        get() = requireActivity() as MainActivity

    /**
     * @return true if handled in this method
     */
    fun onBackPressed(): Boolean {
        return false
    }

}
