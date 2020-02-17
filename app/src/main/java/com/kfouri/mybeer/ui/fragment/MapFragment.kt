package com.kfouri.mybeer.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kfouri.mybeer.R

class MapFragment : BaseFragment() {

    companion object {
        fun newInstance(): MapFragment = MapFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_map, container, false)
}