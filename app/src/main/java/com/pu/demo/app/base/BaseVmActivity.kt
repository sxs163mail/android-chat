package com.pu.demo.app.base

import android.R
import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import com.pu.demo.BR
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity
import java.lang.reflect.ParameterizedType


abstract class BaseVmActivity<DB : ViewDataBinding, VM : BaseViewModel> : RxAppCompatActivity() {
    open lateinit var binding: DB
    open lateinit var vm: VM

    abstract fun initContentView(savedInstanceState: Bundle?): Int
    abstract fun onView()

    @SuppressLint("SourceLockedOrientationActivity")
    @Suppress("UNCHECKED_CAST")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // view binding
        binding = DataBindingUtil.setContentView(this, initContentView(savedInstanceState))
        binding.lifecycleOwner = this
        val parameterizedType = javaClass.genericSuperclass as? ParameterizedType
        val vmClass = parameterizedType?.actualTypeArguments?.getOrNull(1) as? Class<VM>?
        if (vmClass != null) vm = ViewModelProvider(this)[vmClass]
        binding.setVariable(BR.vm, vm)

        // screen portrait
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        // on view
        onView()
    }

    fun statusBarBlack() {
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        window.statusBarColor = getColor(R.color.transparent)
    }

    fun statusBarWhite() {
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_VISIBLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        window.statusBarColor = getColor(R.color.transparent)

    }
}