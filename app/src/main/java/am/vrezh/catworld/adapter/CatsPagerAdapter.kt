package am.vrezh.catworld.adapter

import am.vrezh.catworld.R
import am.vrezh.catworld.fragments.CatsFragment
import am.vrezh.catworld.fragments.FavoriteCatsFragment
import android.content.Context
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class CatsPagerAdapter(fragmentManager: FragmentManager, context: Context) :
    FragmentPagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private var tabs = listOf(
        context.getString(R.string.cats) to CatsFragment.newInstance(),
        context.getString(R.string.favorites) to FavoriteCatsFragment.newInstance()
    )

    override fun getItem(position: Int) = tabs[position].second

    override fun getCount() = tabs.size

    override fun getPageTitle(position: Int) = tabs[position].first

}