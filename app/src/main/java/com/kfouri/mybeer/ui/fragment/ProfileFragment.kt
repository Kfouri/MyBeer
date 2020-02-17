package com.kfouri.mybeer.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.kfouri.mybeer.R
import com.kfouri.mybeer.adapter.OptionProfileAdapter
import com.kfouri.mybeer.databinding.FragmentProfileBinding
import com.kfouri.mybeer.network.model.OptionProfileModel
import com.kfouri.mybeer.util.PrefsHelper
import com.kfouri.mybeer.viewmodel.*
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment : BaseFragment() {

    private lateinit var binding: FragmentProfileBinding
    private val adapter = OptionProfileAdapter { option : OptionProfileModel -> optionItemClicked(option) }

    companion object {
        fun newInstance(): ProfileFragment = ProfileFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {
        viewModel = ViewModelProviders.of(this).get(ProfileFragmentViewModel::class.java)
        (viewModel as ProfileFragmentViewModel).onOptionProfileList().observe(this, Observer { getOptionList(it) })
        subscribe()
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)
        binding.viewmodel = viewModel as ProfileFragmentViewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Glide.with(this)
            .load(R.drawable.photo)
            .apply(RequestOptions.circleCropTransform())
            .into(imageView)

        val recyclerView = binding.recyclerViewOptions
        recyclerView.setHasFixedSize(true)
        recyclerView.addItemDecoration(DividerItemDecoration(activity, LinearLayoutManager.VERTICAL))
        recyclerView.adapter = adapter
    }

    private fun getOptionList(list: ArrayList<OptionProfileModel>) {
        adapter.setData(list)
    }

    private fun optionItemClicked(option : OptionProfileModel) {
        when (option.type) {
            OPTION_EXIT -> exitApp()
            OPTION_HELP -> callHelpActivity()
            OPTION_INVITE_FRIENDS -> callInviteFriendActivity()
        }
    }

    private fun exitApp() {
        PrefsHelper.write(PrefsHelper.TOKEN, "")
        activity?.finish()
    }

    private fun callHelpActivity() {

    }

    private fun callInviteFriendActivity() {

    }

}