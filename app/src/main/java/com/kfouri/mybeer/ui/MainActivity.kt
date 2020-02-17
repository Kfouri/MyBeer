package com.kfouri.mybeer.ui

import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.SearchView
import androidx.fragment.app.Fragment
import com.kfouri.mybeer.R
import com.kfouri.mybeer.ui.fragment.BarFragment
import com.kfouri.mybeer.ui.fragment.BaseFragment
import com.kfouri.mybeer.ui.fragment.MapFragment
import com.kfouri.mybeer.ui.fragment.ProfileFragment
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : BaseActivity() {

    var searchView : SearchView? = null
    lateinit var fragment: BaseFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        configBottomNavigation()
    }

    private fun configBottomNavigation() {
        bottom_navigation.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.navigation_bar -> {
                    searchView?.visibility = View.VISIBLE
                    searchView?.setQuery("", false)
                    searchView?.isIconified = true
                    fragment = BarFragment.newInstance()
                    openFragment(fragment)
                    true
                }
                R.id.navigation_map -> {
                    searchView?.visibility = View.GONE
                    fragment = MapFragment.newInstance()
                    openFragment(fragment)
                    true
                }
                R.id.navigation_profile -> {
                    searchView?.visibility = View.GONE
                    fragment = ProfileFragment.newInstance()
                    openFragment(fragment)
                    true
                }
                else -> false
            }
        }

        bottom_navigation.selectedItemId = R.id.navigation_bar
    }

    private fun openFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.main_container, fragment)
        transaction.commit()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.bar_menu, menu)

        val searchItem = menu?.findItem(R.id.action_search)
        searchView = searchItem?.actionView as SearchView

        searchView?.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (fragment is BarFragment) {
                    (fragment as BarFragment).setFilterAdapter(newText)
                }
                return true
            }
        })
        return super.onCreateOptionsMenu(menu)
    }
}
