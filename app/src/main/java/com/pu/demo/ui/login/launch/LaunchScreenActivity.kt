package com.pu.demo.ui.login.launch

import android.content.Intent
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import com.pu.demo.R
import com.pu.demo.app.base.BaseVmActivity
import com.pu.demo.databinding.ActivityLaunchScreenBinding
import com.pu.demo.ui.login.phone.LoginPhoneActivity
import com.pu.demo.view.setOnSingleClickListener


class LaunchScreenActivity : BaseVmActivity<ActivityLaunchScreenBinding, LaunchScreenViewModel>() {
    override fun initContentView(savedInstanceState: Bundle?) = R.layout.activity_launch_screen

    override fun onView() {
        super.statusBarBlack()

        val subTitle = getString(R.string.launch_screen_sub_title)
        val builder = SpannableStringBuilder(subTitle)
        val keySpan = ForegroundColorSpan(getColor(R.color.lightBlueLevel1))
        val startIndex = subTitle.indexOf(" ")
        val endIndex = subTitle.lastIndexOf(" ")
        builder.setSpan(keySpan, startIndex, endIndex, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
        binding.subTitle.text = builder

        binding.nextBtnLayout.setOnSingleClickListener({
            val intent = Intent(this, LoginPhoneActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            this.startActivity(intent)
        })
    }
}