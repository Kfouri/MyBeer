package com.kfouri.mybeer.ui.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.ads.MobileAds
import com.kfouri.mybeer.R
import com.kfouri.mybeer.adapter.BarAdapter
import com.kfouri.mybeer.databinding.FragmentBarBinding
import com.kfouri.mybeer.model.ActivityModel
import com.kfouri.mybeer.network.model.BarModel
import com.kfouri.mybeer.ui.BarDetailsActivity
import com.kfouri.mybeer.util.PrefsHelper
import com.kfouri.mybeer.viewmodel.BarFragmentViewModel
import kotlinx.android.synthetic.main.fragment_bar.*

const val DEFAULT_RADIUS = 5
const val ITEM_PER_AD = 5

class BarFragment : BaseFragment() {

    private lateinit var binding: FragmentBarBinding
    private val adapter = BarAdapter { bar : BarModel -> barItemClicked(bar) }
    private var barAdList =  ArrayList<BarModel>()

    companion object {
        fun newInstance(): BarFragment = BarFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {
        viewModel = ViewModelProviders.of(this).get(BarFragmentViewModel::class.java)
        (viewModel as BarFragmentViewModel).onBarList().observe(this, Observer { getBarList(it) })
        subscribe()
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_bar, container, false)
        binding.viewmodel = viewModel as BarFragmentViewModel
        binding.lifecycleOwner = this
        MobileAds.initialize(activity) {}
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = binding.recyclerViewBars
        recyclerView.setHasFixedSize(true)
        recyclerView.addItemDecoration(DividerItemDecoration(activity, LinearLayoutManager.VERTICAL))
        recyclerView.adapter = adapter

        adapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
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
        barAdList.clear()
        barAdList.addAll(list)

        for (i in barAdList.indices step ITEM_PER_AD) {
            val adView = BarModel(0, "", "", "", 0.0, 0.0, "", 0.0, 0.0,0, 0F, 2, ArrayList())
            barAdList.add(i, adView)
        }
        adapter.setData(barAdList)

    }

    fun setFilterAdapter(newText: String?) {
        adapter.filter.filter(newText)
    }

    private fun setupDialog() {

        val kms = arrayOf("1", "2", "5", "10", "20", "30", "50", "100")

        val builder: AlertDialog.Builder = AlertDialog.Builder(activity)
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
        val lat = PrefsHelper.read(PrefsHelper.LAT, 0.0).toString()
        val lon = PrefsHelper.read(PrefsHelper.LON, 0.0).toString()
        val rad = PrefsHelper.read(PrefsHelper.RADIUS, DEFAULT_RADIUS)
        (viewModel as BarFragmentViewModel).getBars(rad, lat, lon)
    }

    private fun barItemClicked(bar: BarModel) {
        val bundle = Bundle()
        bundle.putInt("idBar", bar.id)
        startActivityModel(ActivityModel(BarDetailsActivity::class.java, bundle))
    }
}