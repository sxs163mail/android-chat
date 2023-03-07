package com.pu.demo.ui.group.create

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.LinearLayoutManager
import com.facebook.drawee.view.SimpleDraweeView
import com.google.android.material.snackbar.Snackbar
import com.pu.demo.BR
import com.pu.demo.R
import com.pu.demo.app.base.BaseAdapter
import com.pu.demo.app.base.BaseViewHolder
import com.pu.demo.app.base.BaseVmActivity
import com.pu.demo.databinding.ActivityGroupCreateBinding
import com.pu.demo.ui.group.select.UIContact4GroupSelected
import com.pu.demo.utils.TimeUtils
import com.pu.demo.view.setOnSingleClickListener
import dagger.hilt.android.AndroidEntryPoint
import java.util.ArrayList

@AndroidEntryPoint
class NewGroupCreateActivity :
    BaseVmActivity<ActivityGroupCreateBinding, GroupCreateViewModel>() {
    override fun initContentView(savedInstanceState: Bundle?) = R.layout.activity_group_create
    private var selectedAdapter: ContactForGroupCreateAdapter = ContactForGroupCreateAdapter(this)
    private var contactLayoutManager: LinearLayoutManager = LinearLayoutManager(this)

    override fun onView() {
        val selectedList: ArrayList<UIContact4GroupSelected> = intent.extras?.getParcelableArrayList("SelectedList")!!

        vm.selectedList.postValue(selectedList.map { s-> UIContact4GroupCreate(id = s.id, iconUrl = s.iconUrl, title = s.title, lastSeenTime = s.lastSeenTime ) })

        // 设置导航栏
        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = getString(R.string.title_group_create)
        }


        // 设置tips
        binding.memberCountTips.text = String.format(
            getString(R.string.group_create_activity_member_tips),
            selectedList.size
        )

        // 群成员
        binding.memberList.apply {
            layoutManager = contactLayoutManager
            adapter = selectedAdapter
        }

        // 监听通讯录数据变化
        vm.selectedList.observe(this) {
            selectedAdapter.refreshData(it)
        }

        // 群名称
        vm.groupName.observe(this) {
            if (it.isBlank()) {
                vm.checkDataOk.postValue(false)
            } else {
                vm.checkDataOk.postValue(true)
            }
        }

        // 下一步
        binding.nextBtn.setOnSingleClickListener({
            if (vm.checkDataOk.value == true) {
                Snackbar.make(binding.root, "提交创建群", Snackbar.LENGTH_SHORT).show()
            }
        })

        vm.checkDataOk.observe(this) {
            if (it) {
//                android:backgroundTint="@color/lightBlueLevel1"
                binding.nextBtn.backgroundTintList = ColorStateList.valueOf(this.getColor(R.color.lightBlueLevel1))
            } else {
                binding.nextBtn.backgroundTintList = ColorStateList.valueOf(this.getColor(R.color.textLevel4))
            }
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
 * 创建群聊的群成员列表
 */
class ContactForGroupCreateAdapter constructor(private val activity: NewGroupCreateActivity) :
    BaseAdapter<UIContact4GroupCreate>() {

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
    }

    override fun getItemViewType(position: Int): Int {
        return R.layout.item_contact_for_new_group_member
    }
}

/**
 * 联系人对象（for GroupCreate）
 */
data class UIContact4GroupCreate(
    val id : String,
    val title: String,
    val iconUrl: String,
    val lastSeenTime: Long,
) {
    val lastSeenTimeString = TimeUtils.contactLastSeenShowTime(lastSeenTime)
}

