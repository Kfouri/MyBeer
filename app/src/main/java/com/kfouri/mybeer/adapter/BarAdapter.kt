package com.kfouri.mybeer.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.gms.ads.AdRequest
import com.kfouri.mybeer.R
import com.kfouri.mybeer.network.model.BarModel
import com.kfouri.mybeer.ui.fragment.ITEM_PER_AD
import kotlinx.android.synthetic.main.banner_ad_container.view.*
import kotlinx.android.synthetic.main.bar_item.view.*

class BarAdapter(private val clickListener: (BarModel) -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), Filterable {

    private var barList = ArrayList<BarModel>()
    private var barListFull = ArrayList<BarModel>()
    private val ITEM_BAR = 0
    private val ITEM_BANNER_AD = 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == ITEM_BANNER_AD) {
            BannerAdViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.banner_ad_container, parent, false))
        } else {
            BarViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.bar_item, parent, false))
        }
    }

    override fun getItemCount(): Int {
        return barList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val viewType = getItemViewType(position)

        if (viewType == ITEM_BAR) {
            val item = barList[position]
            (holder as BarViewHolder).bind(item, clickListener)
        }

    }

    fun setData(list: ArrayList<BarModel>) {
        barList.clear()
        barList.addAll(list)
        barListFull = ArrayList(list)
        notifyDataSetChanged()
    }

    override fun getFilter(): Filter {
        return barFilter
    }

    private val barFilter = object: Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val filterList = ArrayList<Any>()
            if (!constraint.isNullOrEmpty()) {
                barListFull.forEach {
                    if (it.nombre.toLowerCase().contains(constraint.toString().toLowerCase().trim())) {
                        filterList.add(it)
                    }
                }
            } else {
                filterList.addAll(barListFull)
            }

            val result = FilterResults()
            result.values = filterList

            return result
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            barList.clear()
            barList.addAll(results?.values as ArrayList<BarModel>)
            notifyDataSetChanged()
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position%ITEM_PER_AD == 0) {
            ITEM_BANNER_AD
        } else {
            ITEM_BAR
        }
    }

    class BarViewHolder (view: View) : RecyclerView.ViewHolder(view) {
        fun bind(bar: Any,  clickListener: (BarModel) -> Unit){
            if (bar is BarModel) {
                itemView.textView_name.text = bar.nombre
                itemView.textView_address.text = bar.direccion
                itemView.textView_city.text = bar.ciudad
                itemView.textView_distance.text = bar.distance.toString() + " Km"
                itemView.imageView_logo.loadUrl(bar.logo)
                itemView.textView_rating.text = bar.rating.toString()
                itemView.setOnClickListener { clickListener(bar)}
            }
        }

        private fun ImageView.loadUrl(url: String) {
            Glide.with(this)
                .load(url)
                .placeholder(R.drawable.beer_icon)
                .apply(RequestOptions.circleCropTransform())
                .into(this)
        }
    }

    class BannerAdViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        init {
            itemView.adView.loadAd(AdRequest.Builder().build())
        }
    }

}