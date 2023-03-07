package com.pu.demo.view

import android.view.View

inline fun View.setOnSingleClickListener(crossinline onClick: () -> Unit, delayMillis: Long = 800) {
    this.setOnClickListener {
        this.isClickable = false
        onClick()
        this.postDelayed({
            this.isClickable = true
        }, delayMillis)
    }
}