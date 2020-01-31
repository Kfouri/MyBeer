package com.kfouri.mybeer.ui

import android.app.AlertDialog
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.AdapterDataObserver
import com.kfouri.mybeer.R
import com.kfouri.mybeer.adapters.BarAdapter
import com.kfouri.mybeer.databinding.ActivityMainBinding
import com.kfouri.mybeer.network.model.BarModel
import com.kfouri.mybeer.utils.PrefsHelper
import com.kfouri.mybeer.viewmodels.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

    private lateinit var binding: ActivityMainBinding
    private val adapter = BarAdapter()
    private val DEFAULT_RADIUS = 30

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        subscribe()
        (viewModel as MainViewModel).onBarList().observe(this, Observer { getBarList(it) })
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.viewmodel = viewModel as MainViewModel
        binding.lifecycleOwner = this

        val recyclerView = binding.recyclerViewBars
        recyclerView.setHasFixedSize(true)
        recyclerView.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))
        recyclerView.adapter = adapter

        adapter.registerAdapterDataObserver(object : AdapterDataObserver() {
            override fun onChanged() {
                super.onChanged()
                val cnt = adapter.itemCount
                textView_listCount.text = getString(R.string.main_activity_bar_found, cnt.toString())
                textView_emptyList.visibility = if (cnt > 0) View.GONE else View.VISIBLE
            }
        })

        textView_setRadius.setOnClickListener {
            setupDialog()
        }
    }

    override fun onResume() {
        super.onResume()
        getBars()
    }

    private fun getBarList(list: ArrayList<BarModel>) {
        adapter.setData(list)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.bar_menu, menu)

        val searchItem = menu?.findItem(R.id.action_search)
        val searchView = searchItem?.actionView as SearchView

        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filter.filter(newText)
                return true
            }
        })
        return super.onCreateOptionsMenu(menu)
    }

    private fun setupDialog() {

        val kms = arrayOf("1", "2", "5", "10", "20", "30", "50", "100")

        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.main_activity_dialog_title))
            .setIcon(R.drawable.beer_icon)

        builder.setItems(kms) { _, which ->
            PrefsHelper.write(PrefsHelper.RADIUS, kms[which].toInt())
            getBars()
        }
        builder.setNegativeButton(getString(R.string.cancel), null)
        builder.show()
    }

    private fun getBars() {
        textView_setRadius.text = getString(R.string.main_activity_set_radius, PrefsHelper.read(PrefsHelper.RADIUS, DEFAULT_RADIUS).toString())
        (viewModel as MainViewModel).getBars(PrefsHelper.read(PrefsHelper.RADIUS, DEFAULT_RADIUS))
    }
}
