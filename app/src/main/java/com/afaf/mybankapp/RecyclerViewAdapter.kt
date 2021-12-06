package com.afaf.mybankapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_row.view.*

class RecyclerViewAdapter(private val bankProcess: ArrayList<String>): RecyclerView.Adapter<RecyclerViewAdapter.ItemViewHolder>() {
    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int ): RecyclerViewAdapter.ItemViewHolder {
        return ItemViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_row, parent, false),
        )
    }

    override fun onBindViewHolder(holder: RecyclerViewAdapter.ItemViewHolder, position: Int) {
        val process = bankProcess[position]

        holder.itemView.apply {
            tvProcess.text = process
        }
    }

    override fun getItemCount(): Int = bankProcess.size
}