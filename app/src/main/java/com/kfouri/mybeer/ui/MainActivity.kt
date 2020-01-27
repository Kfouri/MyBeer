package com.kfouri.mybeer.ui

import android.os.Bundle
import android.view.Menu
import android.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.kfouri.mybeer.R
import com.kfouri.mybeer.adapters.BarAdapter
import com.kfouri.mybeer.databinding.ActivityMainBinding
import com.kfouri.mybeer.network.model.BarModel
import com.kfouri.mybeer.viewmodels.MainViewModel

class MainActivity : BaseActivity() {

    private lateinit var binding: ActivityMainBinding
    private val adapter = BarAdapter()
    private val DEFAULT_RADIUS = 10000

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
        (viewModel as MainViewModel).getBars(DEFAULT_RADIUS)
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
}
