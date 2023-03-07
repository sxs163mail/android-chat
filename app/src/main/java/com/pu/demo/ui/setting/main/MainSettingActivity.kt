package com.pu.demo.ui.setting.main

import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import com.elvishew.xlog.XLog
import com.pu.demo.R
import com.pu.demo.app.base.BaseVmActivity
import com.pu.demo.databinding.ActivityMainSettingBinding
import java.lang.reflect.Method

class MainSettingActivity : BaseVmActivity<ActivityMainSettingBinding, MainSettingViewModel>() {
    override fun initContentView(savedInstanceState: Bundle?) = R.layout.activity_main_setting
    override fun onView() {

        // 设置导航栏
        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = getString(R.string.title_settings)
        }
        supportFragmentManager
            .beginTransaction()
            .replace(
                R.id.settings,
                SettingMainFragment()
            )
            .commit()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.setting_main_toolbar_menu, menu)
        return true
    }

    override fun onMenuOpened(featureId: Int, menu: Menu): Boolean {
        if (menu.javaClass.simpleName.equals("MenuBuilder", ignoreCase = true)) {
            try {
                val method: Method = menu.javaClass.getDeclaredMethod(
                    "setOptionalIconsVisible",
                    java.lang.Boolean.TYPE
                )
                method.isAccessible = true
                method.invoke(menu, true)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return super.onMenuOpened(featureId, menu)
    }

}

internal class SettingMainFragment : PreferenceFragmentCompat(),
    Preference.OnPreferenceClickListener {
    private var sharedPreferences: SharedPreferences? = null
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.setting_main, rootKey)
        //用于取值的SharedPreferences
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(
            requireActivity()
        )
    }

    override fun onPreferenceClick(preference: Preference): Boolean {
        val v = sharedPreferences!!.getString(preference.key, "")
        XLog.i("preference.getKey()： " + preference.key + ", v = " + v)
        return false
    }
}