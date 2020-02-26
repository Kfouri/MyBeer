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
import com.kfouri.mybeer.R
import com.kfouri.mybeer.network.model.BarModel
import kotlinx.android.synthetic.main.bar_item.view.*

class BarAdapter(private val clickListener: (BarModel) -> Unit) : RecyclerView.Adapter<BarAdapter.ViewHolder>(), Filterable {

    var barList = ArrayList<BarModel>()
    var barListFull = ArrayList<BarModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.bar_item, parent, false))
    }

    override fun getItemCount(): Int {
        return barList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = barList[position]
        holder.bind(item, clickListener)
    }

    fun setData(list: ArrayList<BarModel>) {
        barList = list
        barListFull = ArrayList(list)
        notifyDataSetChanged()
    }

    class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {
        fun bind(bar: BarModel,  clickListener: (BarModel) -> Unit){
            itemView.textView_name.text = bar.nombre
            itemView.textView_address.text = bar.direccion
            itemView.textView_city.text = bar.ciudad
            itemView.textView_distance.text = bar.distance.toString() + " Km"
            itemView.imageView_logo.loadUrl(bar.logo)
            itemView.textView_rating.text = bar.rating.toString()
            itemView.setOnClickListener { clickListener(bar)}
        }

        private fun ImageView.loadUrl(url: String) {
            Glide.with(this)
                .load(url)
                .placeholder(R.drawable.beer_icon)
                .apply(RequestOptions.circleCropTransform())
                .into(this)
        }
    }

    override fun getFilter(): Filter {
        return barFilter
    }

    private val barFilter = object: Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val filterList = ArrayList<BarModel>()
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
}