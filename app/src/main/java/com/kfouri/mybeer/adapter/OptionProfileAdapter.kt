package com.kfouri.mybeer.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.kfouri.mybeer.R
import com.kfouri.mybeer.network.model.OptionProfileModel
import kotlinx.android.synthetic.main.profile_item.view.*


class OptionProfileAdapter(private val clickListener: (OptionProfileModel) -> Unit) : RecyclerView.Adapter<OptionProfileAdapter.ViewHolder>() {

    var optionList = ArrayList<OptionProfileModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.profile_item, parent, false))
    }

    override fun getItemCount(): Int {
        return optionList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = optionList[position]
        holder.bind(item, clickListener)
    }

    fun setData(list: ArrayList<OptionProfileModel>) {
        optionList = list
        notifyDataSetChanged()
    }

    class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {
        fun bind(option: OptionProfileModel,  clickListener: (OptionProfileModel) -> Unit){
            itemView.textView_title.text = option.title
            itemView.textView_subtitle.text = option.subtitle
            itemView.textView_type.text = option.type
            itemView.imageView_logo.loadUrl(option.logo)
            itemView.setOnClickListener { clickListener(option)}
        }

        private fun ImageView.loadUrl(url: Int) {
            Glide.with(this)
                .load(url)
                .placeholder(R.drawable.beer_icon)
                .apply(RequestOptions.circleCropTransform())
                .into(this)
        }

    }
}