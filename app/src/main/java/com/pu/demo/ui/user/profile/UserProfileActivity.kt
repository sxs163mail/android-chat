package com.pu.demo.ui.user.profile

import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.elvishew.xlog.XLog
import com.google.android.material.tabs.TabLayoutMediator
import com.pu.demo.R
import com.pu.demo.app.base.BaseVmActivity
import com.pu.demo.databinding.ActivityUserProfileBinding
import java.lang.reflect.Method


class UserProfileActivity  : BaseVmActivity<ActivityUserProfileBinding, UserProfileViewModel>() {
    override fun initContentView(savedInstanceState: Bundle?) = R.layout.activity_user_profile


    var list = mutableListOf<Fragment>()
    var title = mutableListOf("多媒体", "文件", "链接", "音乐", "语音")

    override fun onView() {

        // 设置导航栏
        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = null
        }

        vm.user.observe(this) { user->
            binding.icon.setImageURI(user.icon)
            binding.name.text = user.name
        }

        supportFragmentManager
            .beginTransaction()
            .replace(
                R.id.settings,
                SettingUserProfileFragment()
            )
            .commit()

        //添加Fragment
        list += (TestFragment.newInstance(title[0], "#03A9F4"));
        list += (TestFragment.newInstance(title[1], "#8BC34A"));
        list += (TestFragment.newInstance(title[2], "#009688"));
        binding.viewPager.apply {
            adapter = MyPagerAdapter(this@UserProfileActivity);
        }

        //TabLayout与ViewPager2绑定
        TabLayoutMediator(
            binding.tabLayout, binding.viewPager
        ) { tab, position -> tab.text = title[position] }.attach()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.user_profile_toolbar_menu, menu)
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

    inner class MyPagerAdapter(fragmentActivity: FragmentActivity?) :
        FragmentStateAdapter(fragmentActivity!!) {
        override fun createFragment(position: Int): Fragment {
            return list[position]
        }

        override fun getItemCount(): Int {
            return list.size
        }
    }
}

internal class SettingUserProfileFragment : PreferenceFragmentCompat(),
    Preference.OnPreferenceClickListener {
    private var sharedPreferences: SharedPreferences? = null
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.setting_user_profile, rootKey)
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


