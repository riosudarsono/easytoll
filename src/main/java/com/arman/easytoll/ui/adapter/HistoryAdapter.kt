package com.arman.easytoll.ui.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.arman.easytoll.R
import com.arman.easytoll.data.model.HistoryData
import com.arman.easytoll.utils.StringUtils
import kotlinx.android.synthetic.main.view_item_history.view.*

class HistoryAdapter(private val activity: Activity, private var data: MutableList<HistoryData>) : RecyclerView.Adapter<HistoryAdapter.Holder>() {

    private var count = 1

    fun setItems(data: MutableList<HistoryData>) {
        this.data = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_item_history, parent, false)
        return Holder(view)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(data[position], position)
    }

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(mData: HistoryData, position: Int) {
            with(itemView) {
                tv_name.text = mData.status
                tv_price.text = StringUtils.toRp(activity, mData.price)
            }
        }

    }


}