package com.pu.demo.ui.group.list

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
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
import com.pu.demo.databinding.ActivityGroupListBinding
import com.pu.demo.ui.group.select.NewGroupSelectActivity
import com.pu.demo.view.setOnSingleClickListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GroupListActivity : BaseVmActivity<ActivityGroupListBinding, GroupListViewModel>() {
    override fun initContentView(savedInstanceState: Bundle?) = R.layout.activity_group_list
    private var groupAdapter: GroupAdapter = GroupAdapter(this)
    private var groupLayoutManager: LinearLayoutManager = LinearLayoutManager(this)
    override fun onView() {

        // 设置导航栏
        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = getString(R.string.title_group)
        }

        // 初始化群组列表
        binding.groupList.apply {
            layoutManager = groupLayoutManager
            adapter = groupAdapter
        }

        // 监听群组列表数据变化
        vm.groupList.observe(this) {
            groupAdapter.refreshData(it)
            binding.groupCount.text =
                String.format(getString(R.string.group_activity_group_count), it.size)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
            }
            R.id.group_add -> {
                val intent = Intent(this, NewGroupSelectActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                this.startActivity(intent)
            }
        }
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.group_toolbar_menu, menu)
        return true
    }
}

/**
 * 群组列表
 */
class GroupAdapter constructor(private val activity: GroupListActivity) :
    BaseAdapter<UIGroup>() {

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        val binding: ViewDataBinding = holder.dataBinding
        val it = data[position]
        binding.setVariable(BR.it, it)

        // 设置头像
        val iconView =
            holder.dataBinding.root.findViewById<View>(R.id.group_icon) as SimpleDraweeView
        iconView.setImageURI(it.iconUrl)

        // 设置群成员数
        val groupMemberCountView =
            holder.dataBinding.root.findViewById<View>(R.id.group_member_count) as TextView
        groupMemberCountView.text = String.format(
            activity.getString(R.string.group_activity_member_count),
            it.groupMemberCount
        )


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

        })
    }

    private fun setBackground(view: View, resId: Int) {
        view.background = ResourcesCompat.getDrawable(App.INSTANCE.resources, resId, null)
    }

    override fun getItemViewType(position: Int): Int {
        return R.layout.item_group
    }
}

/**
 * 群组对象
 */
data class UIGroup(
    val title: String,
    val iconUrl: String,
    val groupMemberCount: Long,
)