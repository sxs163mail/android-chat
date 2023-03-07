package com.pu.demo.ui.group.select

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.LinearLayoutManager
import com.facebook.drawee.view.SimpleDraweeView
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.pu.demo.BR
import com.pu.demo.R
import com.pu.demo.app.App
import com.pu.demo.app.base.BaseAdapter
import com.pu.demo.app.base.BaseViewHolder
import com.pu.demo.app.base.BaseVmActivity
import com.pu.demo.databinding.ActivityGroupMemberSelectBinding
import com.pu.demo.ui.group.create.NewGroupCreateActivity
import com.pu.demo.utils.TimeUtils
import com.pu.demo.view.setOnSingleClickListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.parcel.Parcelize

@AndroidEntryPoint
class NewGroupSelectActivity :
    BaseVmActivity<ActivityGroupMemberSelectBinding, GroupMemberSelectViewModel>() {
    override fun initContentView(savedInstanceState: Bundle?) = R.layout.activity_group_member_select

    private var contactAdapter: ContactForGroupSelectAdapter = ContactForGroupSelectAdapter(this)
    private var contactLayoutManager: LinearLayoutManager = LinearLayoutManager(this)

    private var selectedAdapter: ContactForGroupSelectedAdapter =
        ContactForGroupSelectedAdapter(this)
    private var selectedLayoutManager: FlexboxLayoutManager = FlexboxLayoutManager(this)
    override fun onView() {

        // 设置导航栏
        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = getString(R.string.title_group_create)
        }

//        binding.toolbarTitle.text = getString(R.string.title_group_create)
        // 初始化通讯录列表
        binding.contactList.apply {
            layoutManager = contactLayoutManager
            adapter = contactAdapter
        }

        // 初始化(锁定)通讯录列表
        binding.selectedList.apply {
            selectedLayoutManager.flexDirection = FlexDirection.ROW
            selectedLayoutManager.flexWrap = FlexWrap.WRAP
            selectedLayoutManager.justifyContent = JustifyContent.FLEX_START

            layoutManager = selectedLayoutManager
            adapter = selectedAdapter
        }

        // 监听通讯录数据变化
        vm.contactList.observe(this) {
            contactAdapter.refreshData(it)
        }

        // 监听锁定的用户数据变化
        vm.selectedList.observe(this) {
            if (it.isEmpty()) {
//                binding.toolbarSubTitle.text =
                    supportActionBar?.subtitle =
                    getString(R.string.group_select_activity_sub_title_zero)
                binding.nextBtn.visibility = View.GONE
            } else {
//                binding.toolbarSubTitle.text =
                    supportActionBar?.subtitle = String.format(
                    getString(R.string.group_select_activity_sub_title_selected),
                    it.size
                )
                binding.nextBtn.visibility = View.VISIBLE
            }
            selectedAdapter.refreshData(it)
        }

        // 下一步
        binding.nextBtn.setOnSingleClickListener({
            val intent = Intent(this, NewGroupCreateActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            intent.putParcelableArrayListExtra("SelectedList", vm.selectedList.value!!.toCollection(arrayListOf()))
            this.startActivity(intent)
        })

        
        // 监听搜索框内容
        vm.etSearchInput.observe(this) { k ->

            vm.contactList.value?.let { list ->
                if (k.isNullOrBlank()) {
                    contactAdapter.refreshData(list)
                } else {
                    // 内存搜索
                    val result = mutableListOf<UIContact4GroupSelect>()
                    for (contact in list) {
                        if (contact.title.contains(k)) {
                            contact.searchKey = k
                            result += contact
                        }
                    }
                    contactAdapter.refreshData(result)
                }
            }
        }

        vm.unSelectedItemId.observe(this) {
            contactAdapter.unSelectedItem(it)
            selectedAdapter.unSelectedItem(it)
        }
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
            }
        }
        return true
    }
}


/**
 * 联系人（锁定）搜索列表
 */
class ContactForGroupSelectedAdapter constructor(private val activity: NewGroupSelectActivity) :
    BaseAdapter<UIContact4GroupSelected>() {

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        val binding: ViewDataBinding = holder.dataBinding
        val it = data[position]
        binding.setVariable(BR.it, it)

        // 设置名称
        val titleView = holder.dataBinding.root.findViewById<View>(R.id.contact_title) as TextView
        titleView.text = it.titleString

        // 设置头像
        val iconView =
            holder.dataBinding.root.findViewById<View>(R.id.contact_icon) as SimpleDraweeView
        iconView.setImageURI(it.iconUrl)

        // item 点击事件
        holder.dataBinding.root.setOnSingleClickListener({
            activity.vm.unSelectedItemId.postValue(it.id)
        })
    }

    fun unSelectedItem(id:String) {
        activity.vm.selectedList.value?.let { list->
            val result = list.toMutableList()
            val index = list.indexOfFirst { o -> o.id == id }
            if (index != -1) {
                result.removeAt(index)
                activity.vm.selectedList.value = result
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return R.layout.item_contact_for_group_selected
    }
}

/**
 * 联系人（锁定）对象（for GroupSelect）
 */
@Parcelize
data class UIContact4GroupSelected(
    val id : String,
    val title: String,
    val iconUrl: String,
    val lastSeenTime: Long

) : Parcelable {
    val titleString: String
    get() {
        if (title.length > 7) {
            return "${title.substring(0,7)}..."
        }
        return title
    }

    companion object {
        fun convert(bean: UIContact4GroupSelect): UIContact4GroupSelected {
            return UIContact4GroupSelected(id = bean.id, title = bean.title, iconUrl = bean.iconUrl, lastSeenTime = bean.lastSeenTime)
        }
    }
}

/**
 * 联系人搜索列表
 */
class ContactForGroupSelectAdapter constructor(private val activity: NewGroupSelectActivity) :
    BaseAdapter<UIContact4GroupSelect>() {

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        val binding: ViewDataBinding = holder.dataBinding
        val it = data[position]
        binding.setVariable(BR.it, it)

        // 设置名称
        val titleView = holder.dataBinding.root.findViewById<View>(R.id.contact_title) as TextView
        titleView.text = it.title

        // 设置头像
        val iconView =
            holder.dataBinding.root.findViewById<View>(R.id.contact_icon) as SimpleDraweeView
        iconView.setImageURI(it.iconUrl)

        // 设置选中标志
        val selectedView = holder.dataBinding.root.findViewById<View>(R.id.selected)
        if (it.selected) {
            selectedView.visibility = View.VISIBLE
        } else {
            selectedView.visibility = View.GONE
        }

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

            // 修改状态
            it.selected = !it.selected
            notifyItemChanged(position)

            activity.vm.selectedList.value?.let { list->
                val result = list.toMutableList()

                // 判断是否已经存在
                val index = list.indexOfFirst { o -> o.id == it.id }
                if (index != -1) {
                    result.removeAt(index)
                } else {
                    result.add(UIContact4GroupSelected.convert(it))
                }
                activity.vm.selectedList.value = result

                // 不为0时触发一次滚动到底部
                if (result.size > 0) {
                    activity.binding.selectedList.scrollToPosition(result.size - 1)
                }
            }

            // 选择以后,如果之前搜索框有数据, 才会清空搜索框, 防止过度刷新
            activity.vm.etSearchInput.value?.let {
                if (it.isNotBlank()) {
                    activity.vm.etSearchInput.postValue("")
                }
            }
        })
    }

    fun unSelectedItem(id:String) {
        activity.vm.contactList.value?.let { list->
            val index = list.indexOfFirst { o -> o.id == id }
            if (index != -1) {
                list[index].selected = !list[index].selected
                notifyItemChanged(index)
            }
        }
    }

    private fun setBackground(view: View, resId: Int) {
        view.background = ResourcesCompat.getDrawable(App.INSTANCE.resources, resId, null)
    }

    override fun getItemViewType(position: Int): Int {
        return R.layout.item_contact_for_group_select
    }
}

/**
 * 联系人对象（for GroupSelect）
 */
data class UIContact4GroupSelect(
    val id : String,
    val title: String,
    val iconUrl: String,
    val lastSeenTime: Long,
    var visible: Boolean = true,
    var searchKey: String? = null,
    var selected: Boolean = false
    ) {
    val lastSeenTimeString = TimeUtils.contactLastSeenShowTime(lastSeenTime)
}


