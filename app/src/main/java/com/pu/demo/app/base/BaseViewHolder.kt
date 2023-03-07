package com.pu.demo.app.base

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

open class BaseViewHolder(var dataBinding: ViewDataBinding) :
    RecyclerView.ViewHolder(dataBinding.root) {


}
