package com.pu.demo.ui.login.verification

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import com.pu.demo.R
import com.pu.demo.app.base.BaseVmActivity
import com.pu.demo.databinding.ActivityPhoneVerificationBinding
import com.pu.demo.ui.main.MainActivity
import com.wynsbin.vciv.VerificationCodeInputView

class PhoneVerificationActivity  : BaseVmActivity<ActivityPhoneVerificationBinding, VerificationViewModel>() {
    override fun initContentView(savedInstanceState: Bundle?) = R.layout.activity_phone_verification
    override fun onView() {

        // 设置导航栏
        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
        }

        binding.vcInput.setOnInputListener(object : VerificationCodeInputView.OnInputListener {
            override fun onComplete(code: String?) {
                code?.let {
                    if (code == "1111") {
                        val intent = Intent(this@PhoneVerificationActivity, MainActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                        this@PhoneVerificationActivity.startActivity(intent)
                    } else {
                        Toast.makeText(this@PhoneVerificationActivity, "验证码输入错误", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onInput() {
            }

        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return true
    }
}