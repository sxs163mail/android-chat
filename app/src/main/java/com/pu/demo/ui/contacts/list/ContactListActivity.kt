package com.pu.demo.ui.contacts.list

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.iterator
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.LinearLayoutManager
import com.facebook.drawee.view.SimpleDraweeView
import com.google.android.material.snackbar.Snackbar
import com.pu.demo.BR
import com.pu.demo.R
import com.pu.demo.app.App
import com.pu.demo.app.base.BaseAdapter
import com.pu.demo.app.base.BaseViewHolder
import com.pu.demo.app.base.BaseVmActivity
import com.pu.demo.databinding.ActivityContactListBinding
import com.pu.demo.ui.user.profile.UserProfileActivity
import com.pu.demo.utils.TimeUtils
import com.pu.demo.view.setOnSingleClickListener
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ContactsActivity : BaseVmActivity<ActivityContactListBinding, ContactListViewModel>() {
    override fun initContentView(savedInstanceState: Bundle?) = R.layout.activity_contact_list

    private var contactAdapter: ContactAdapter = ContactAdapter(this)
    private var contactLayoutManager: LinearLayoutManager = LinearLayoutManager(this)

    private var searchAdapter: ContactAdapter = ContactAdapter(this)
    private var searchLayoutManager: LinearLayoutManager = LinearLayoutManager(this)

    private var menu: Menu? = null

    override fun onView() {
        // 设置导航栏
        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
        }

        binding.toolbarTitle.text = getString(R.string.title_contacts)
        // 初始化通讯录列表
        binding.contactList.apply {
            layoutManager = contactLayoutManager
            adapter = contactAdapter
        }

        // 初始化通讯录搜索列表
        binding.searchList.apply {
            layoutManager = searchLayoutManager
            adapter = searchAdapter
        }

        // 监听通讯录列表数据变化
        vm.contactList.observe(this) {
            contactAdapter.refreshData(it)
        }

        // 监听是否进入搜索模式
        vm.searchModel.observe(this) {
            if (it) {
                // 进入搜索模式
                binding.toolbarTitle.visibility = View.GONE
                binding.etSearch.visibility = View.VISIBLE
                binding.etSearch.requestFocus()

                val imm: InputMethodManager =
                    getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.showSoftInput(binding.etSearch, InputMethodManager.SHOW_IMPLICIT)

                menuVisible(false)
            } else {
                // 退出搜索模式
                binding.toolbarTitle.visibility = View.VISIBLE
                binding.etSearch.visibility = View.GONE
                menuVisible(true)
                vm.etSearchInput.value = ""
            }
        }

        // 监听搜索框内容
        vm.etSearchInput.observe(this) { k ->

            vm.contactList.value?.let { list ->
                if (k.isNullOrBlank()) {
                    binding.contactListLayout.visibility = View.VISIBLE
                    binding.searchListLayout.visibility = View.GONE
                } else {
                    binding.contactListLayout.visibility = View.GONE
                    binding.searchListLayout.visibility = View.VISIBLE

                    // 纯内存搜索
                    val result = mutableListOf<UIContact>()
                    for (contact in list) {
                        if (contact.title.contains(k)) {
                            contact.searchKey = k
                            result += contact
                        }
                    }
                    binding.searchTips.text = String.format(getString(R.string.contacts_activity_search_result), result.size)
                    searchAdapter.refreshData(result)
                }
            }


        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                return onBack()
            }
            R.id.main_search -> {
                vm.searchModel.postValue(!vm.searchModel.value!!)
            }
        }
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        this.menu = menu
        menuInflater.inflate(R.menu.contacts_toolbar_menu, menu)
        return true
    }

    // 菜单显示/隐藏
    private fun menuVisible(visible: Boolean) {
        this.menu?.let { menu ->
            menu.iterator().forEach { menuItem ->
                menuItem.isVisible = visible
            }
        }
    }

    // 处理返回: 1系统按键 2菜单栏按钮
    private fun onBack(): Boolean {
        if (vm.searchModel.value!!) {
            vm.searchModel.value = false
            return false
        }
        finish()
        return true
    }

    // 捕获返回键的方法1
    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.repeatCount == 0) {
            // 拦截返回事件
            return onBack()
        }
        return super.onKeyDown(keyCode, event)
    }
}

/**
 * 联系人列表
 */
class ContactAdapter constructor(private val activity: ContactsActivity) :
    BaseAdapter<UIContact>() {

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        val binding: ViewDataBinding = holder.dataBinding
        val it = data[position]
        binding.setVariable(BR.it, it)

        // 通讯录姓名高亮关键词
        val titleView = holder.dataBinding.root.findViewById<View>(R.id.contact_title) as TextView
        if (it.searchKey == null) {
            titleView.text = it.title
        } else {
            val builder = SpannableStringBuilder(it.title)
            val keySpan = ForegroundColorSpan(activity.getColor(R.color.lightBlueLevel1))
            val startIndex = it.title.indexOf(it.searchKey!!)
            builder.setSpan(keySpan, startIndex, startIndex + it.searchKey!!.length, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
            titleView.text = builder
        }

        // 设置头像
        val iconView = holder.dataBinding.root.findViewById<View>(R.id.contact_icon) as SimpleDraweeView
        iconView.setImageURI(it.iconUrl)

        // item 被触摸时变色, 抬手时触发点击事件
        holder.dataBinding.root.setOnTouchListener { _, motionEvent ->
            when (motionEvent.action) {
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
            Snackbar.make(
                holder.dataBinding.root,
                "点击了item: " + it.title,
                Snackbar.LENGTH_SHORT
            ).show()

            // 打开资料页
            val intent = Intent(activity, UserProfileActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            activity.startActivity(intent)
        })
    }

    private fun setBackground(view: View, resId: Int) {
        view.background = ResourcesCompat.getDrawable(App.INSTANCE.resources, resId, null)
    }

    override fun getItemViewType(position: Int): Int {
        return R.layout.item_contact
    }
}

/**
 * 联系人对象
 */
data class UIContact(
    val title: String,
    val iconUrl: String,
    val lastSeenTime: Long,
    var visible: Boolean = true,
    var searchKey: String? = null

) {
    val lastSeenTimeString = TimeUtils.contactLastSeenShowTime(lastSeenTime)
}