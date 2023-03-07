package com.pu.demo.ui.main

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.GravityCompat
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.LinearLayoutManager
import com.facebook.drawee.view.SimpleDraweeView
import com.google.android.material.snackbar.Snackbar
import com.pu.demo.BR
import com.pu.demo.R
import com.pu.demo.app.App
import com.pu.demo.app.base.BaseVmActivity
import com.pu.demo.app.base.BaseAdapter
import com.pu.demo.app.base.BaseViewHolder
import com.pu.demo.databinding.ActivityMainBinding
import com.pu.demo.model.CurrentChat
import com.pu.demo.model.User
import com.pu.demo.ui.chat.ChatActivity
import com.pu.demo.ui.chat.OpeType
import com.pu.demo.ui.contacts.list.ContactsActivity
import com.pu.demo.ui.group.list.GroupListActivity
import com.pu.demo.ui.setting.main.MainSettingActivity
import com.pu.demo.view.setOnSingleClickListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseVmActivity<ActivityMainBinding, MainViewModel>() {
    override fun initContentView(savedInstanceState: Bundle?) = R.layout.activity_main
    private var menuAdapter: DrawerLayoutMenuAdapter = DrawerLayoutMenuAdapter(this)
    private var menuLayoutManager: LinearLayoutManager = LinearLayoutManager(this)

    private var rcAdapter: RecentContactAdapter = RecentContactAdapter(this)
    private var rcLayoutManager: LinearLayoutManager = LinearLayoutManager(this)

    override fun onView() {
        // 设置导航栏
        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_baseline_dehaze_24)
        }

        // 初始化菜单列表
        binding.menuList.apply {
            layoutManager = menuLayoutManager
            adapter = menuAdapter
        }

        // 初始化聊天会话列表
        binding.rcList.apply {
            layoutManager = rcLayoutManager
            adapter = rcAdapter
        }

        // 监听抽屉菜单数据变化
        vm.menuList.observe(this) {
            menuAdapter.refreshData(it)
        }

        // 监听会话列表数据变化
        vm.recentContactList.observe(this) {
            rcAdapter.refreshData(it)
        }

        // 监听用户资料对象
        App.appVM.currentUser.observe(this) {
            vm.drawerLayoutUser.postValue(UIDrawerLayoutUser.convert(it))
            binding.rcIcon.setImageURI(it.icon)
        }

        // toolbar 标题监听
        App.appVM.mainActivityTitle.observe(this) {
            supportActionBar?.title = it
        }

        // 初始化抽屉菜单
        createDrawerLayoutMenu()
    }

    override fun onStop() {
        super.onStop()
        //
        binding.drawerLayout.closeDrawers()
    }

    private fun createDrawerLayoutMenu() {
        val menuList = mutableListOf<UIDrawerLayoutMenu>()
        menuList += UIDrawerLayoutMenu(getString(R.string.title_contacts), ResourcesCompat.getDrawable(App.INSTANCE.resources, R.drawable.ic_baseline_person_24, null)!!)
        menuList += UIDrawerLayoutMenu(getString(R.string.title_group), ResourcesCompat.getDrawable(App.INSTANCE.resources, R.drawable.ic_baseline_group_24, null)!!)
        menuList += UIDrawerLayoutMenu(getString(R.string.title_settings), ResourcesCompat.getDrawable(App.INSTANCE.resources, R.drawable.ic_baseline_settings_24, null)!!)
        vm.menuList.postValue(menuList)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> binding.drawerLayout.openDrawer(GravityCompat.START)
            R.id.main_search -> Snackbar.make(
                binding.drawerLayout,
                "搜索按钮被点击",
                Snackbar.LENGTH_SHORT
            ).show()
        }
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_toolbar_menu, menu)
        return true
    }
}

/**
 * 抽屉菜单列表
 */
class DrawerLayoutMenuAdapter constructor(private val activity: MainActivity) :
    BaseAdapter<UIDrawerLayoutMenu>() {

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        val binding: ViewDataBinding = holder.dataBinding
        val it = data[position]
        binding.setVariable(BR.it, it)

        // item 被触摸时变色, 抬手时触发点击事件
        holder.dataBinding.root.setOnTouchListener { _, motionEvent ->
            when(motionEvent.action) {
                // 按下
                MotionEvent.ACTION_DOWN -> {
                    setBackground(holder.dataBinding.root, R.color.textLevel4_5)
                }
                // 抬起
                MotionEvent.ACTION_UP -> {
                    setBackground(holder.dataBinding.root, R.color.white_level_1)
                    // 触发点击
                    holder.dataBinding.root.performClick()
                }
                // 取消
                MotionEvent.ACTION_CANCEL -> {
                    setBackground(holder.dataBinding.root, R.color.white_level_1)
                }
            }
            true
        }

        // item 点击事件
        holder.dataBinding.root.setOnSingleClickListener({

            when (it.title) {
                activity.getString(R.string.title_contacts) -> {
                    // 打开联系人界面
                    val intent = Intent(activity, ContactsActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    activity.startActivity(intent)
                }
                activity.getString(R.string.title_group) -> {
                    // 打开群组界面
                    val intent = Intent(activity, GroupListActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    activity.startActivity(intent)
                }
                activity.getString(R.string.title_settings) -> {
                    // 打开个人设置界面
                    val intent = Intent(activity, MainSettingActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    activity.startActivity(intent)
                }
            }


        })
    }

    private fun setBackground(view: View, resId: Int) {
        view.background = ResourcesCompat.getDrawable(App.INSTANCE.resources, resId, null)
    }

    override fun getItemViewType(position: Int): Int {
        return R.layout.item_drawer_layout_menu
    }

}

/**
 * 抽屉菜单用户资料对象
 */
data class UIDrawerLayoutUser(
    val id: String,
    val name: String,
    val phone: String,
    val icon: String
) {
    companion object {
        fun convert(user: User) : UIDrawerLayoutUser {
            return UIDrawerLayoutUser(id = user.id, name = user.name, phone = user.phone, icon = user.icon)
        }
    }
}

/**
 * 抽屉菜单对象
 */
data class UIDrawerLayoutMenu(
    val title: String,
    val icon: Drawable
)

/**
 * 会话列表
 */
class RecentContactAdapter constructor(private val activity: MainActivity) :
    BaseAdapter<UIRecentContact>() {

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        val binding: ViewDataBinding = holder.dataBinding
        val it = data[position]
        binding.setVariable(BR.it, it)

        // 设置 item 背景
        setBackground(holder.dataBinding.root, it.background)

        // 设置头像
        val iconView = holder.dataBinding.root.findViewById<View>(R.id.rc_icon) as SimpleDraweeView
        iconView.setImageURI(it.iconUrl)

        // 设置气泡
        val unreadView = holder.dataBinding.root.findViewById<View>(R.id.tv_unread)
        unreadView.backgroundTintList = it.muteColor
        unreadView.visibility = it.unreadVisibility

        // 设置重要信息
        val importantView = holder.dataBinding.root.findViewById<View>(R.id.rc_important)
        importantView.visibility = it.importantVisibility

        // 静音状态
        val muteView = holder.dataBinding.root.findViewById<View>(R.id.mute)
        muteView.visibility = it.muteVisibility

        // item 被触摸时变色, 抬手时触发点击事件
        holder.dataBinding.root.setOnTouchListener { _, motionEvent ->
            when(motionEvent.action) {
                // 按下
                MotionEvent.ACTION_DOWN -> {
                    setBackground(holder.dataBinding.root, R.color.textLevel4_5)
                }
                // 抬起
                MotionEvent.ACTION_UP -> {
                    setBackground(holder.dataBinding.root, it.background)
                    // 触发点击
                    holder.dataBinding.root.performClick()
                }
                // 取消
                MotionEvent.ACTION_CANCEL -> {
                    setBackground(holder.dataBinding.root, it.background)
                }
            }
            true
        }

        // item 点击事件
        holder.dataBinding.root.setOnSingleClickListener({
            // 打开聊天界面
            val intent = Intent(activity, ChatActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            intent.putExtra("CurrentChat", CurrentChat(id="a", title = it.title, iconUrl = it.iconUrl, ope = it.ope))
            activity.startActivity(intent)
        })
    }

    private fun setBackground(view: View, resId: Int) {
        view.background = ResourcesCompat.getDrawable(App.INSTANCE.resources, resId, null)
    }

    override fun getItemViewType(position: Int): Int {
        return R.layout.item_recent_contact
    }
}

/**
 * 会话对象
 */
data class UIRecentContact(
    val title: String,
    val iconUrl: String,
    val time: String,
    val body: String,
    val unread: Int,
    val top: Boolean,
    val mute: Boolean,
    val draft: String = "",
    val at: Boolean = false,
    val ope: Int = OpeType.Private
) {
    val unreadString = unread.toString()
    val background = if(top) R.color.textLevel5 else R.color.white
    val muteColor = if (mute) ColorStateList.valueOf(App.INSTANCE.getColor(R.color.textLevel4)) else ColorStateList.valueOf(
        App.INSTANCE.getColor(R.color.greenLevel1))
    val muteVisibility = if (mute) View.VISIBLE else View.GONE
    val unreadVisibility = if (unread > 0) View.VISIBLE else View.GONE
    val hasDraft = draft.trim().isNotEmpty()
    val importantVisibility = if (hasDraft || at) View.VISIBLE else View.GONE
    val importantString: String
    get() {
        if (hasDraft) return "[草稿]"
        if (at) return "[有人@你]"
        return ""
    }
    val bodyString: String
    get() {
        if (hasDraft) return draft
        return body
    }
}