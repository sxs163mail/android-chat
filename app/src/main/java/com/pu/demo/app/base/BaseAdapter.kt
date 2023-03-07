package com.pu.demo.app.base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView

abstract class BaseAdapter<T>(var data: List<T> = listOf()) :
    RecyclerView.Adapter<BaseViewHolder>() {
    var listener: ItemListener? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return BaseViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context), viewType, parent, false
            )
        )
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun refreshData(newData: List<T>) {
        this.data = newData
        this.notifyDataSetChanged()
    }

    fun insertBeforeData(newData: List<T>) {
        this.data = newData + this.data
        this.notifyItemRangeInserted(0, newData.size)
    }

    fun insertAfterData(newData: List<T>) {
        val start = this.data.size
        this.data += newData
        this.notifyItemRangeInserted(start, this.data.size)
    }

    fun deleteItemData(index: Int) {
        val newList = this.data.toMutableList()
        newList.removeAt(index)
        this.data = newList
        this.notifyItemRemoved(index)
    }

    fun changeItemByPosition(i: Int) {
        this.notifyItemChanged(i)
    }


    fun firstItem(): T? {
        if (this.data.isEmpty()) return null
        return this.data.first()
    }

    interface ItemListener {
        fun onItemClicked(position: Int)
    }
}
