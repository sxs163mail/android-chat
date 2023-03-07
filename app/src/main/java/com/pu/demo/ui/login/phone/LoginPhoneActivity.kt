package com.pu.demo.ui.login.phone

import android.content.Intent
import android.os.Bundle
import com.pu.demo.R
import com.pu.demo.app.base.BaseVmActivity
import com.pu.demo.databinding.ActivityLoginPhoneBinding
import com.pu.demo.ui.login.verification.PhoneVerificationActivity
import com.pu.demo.view.setOnSingleClickListener

class LoginPhoneActivity : BaseVmActivity<ActivityLoginPhoneBinding, LoginPhoneViewModel>() {
    override fun initContentView(savedInstanceState: Bundle?) = R.layout.activity_login_phone
    override fun onView() {
        super.statusBarBlack()


        binding.nextBtn.setOnSingleClickListener({
            val intent = Intent(this, PhoneVerificationActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            this.startActivity(intent)
        })

        binding.etInput.requestFocus()
    }
}