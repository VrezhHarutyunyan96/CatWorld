package am.vrezh.catworld.activity

import am.vrezh.catworld.R
import am.vrezh.catworld.adapter.CatsPagerAdapter
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val catsPagerAdapter = CatsPagerAdapter(supportFragmentManager, this)
        cats_view_pager.adapter = catsPagerAdapter
        cats_tab_layout.setupWithViewPager(cats_view_pager)

    }

}