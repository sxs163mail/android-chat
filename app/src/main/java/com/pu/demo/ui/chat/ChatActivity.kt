package com.pu.demo.ui.chat

import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.elvishew.xlog.XLog
import com.facebook.drawee.view.SimpleDraweeView
import com.pu.demo.BR
import com.pu.demo.R
import com.pu.demo.app.App
import com.pu.demo.app.base.BaseAdapter
import com.pu.demo.app.base.BaseViewHolder
import com.pu.demo.app.base.BaseVmActivity
import com.pu.demo.databinding.ActivityChatBinding
import com.pu.demo.model.CurrentChat
import com.pu.demo.view.setOnSingleClickListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChatActivity : BaseVmActivity<ActivityChatBinding, ChatViewModel>() {
    override fun initContentView(savedInstanceState: Bundle?) = R.layout.activity_chat
    private var chatMessageAdapter: ChatMessageAdapter = ChatMessageAdapter(this)
    private var chatMessageLayoutManager: LinearLayoutManager = LinearLayoutManager(this)

    lateinit var currentChat: CurrentChat

    override fun onView() {
        // 设置全局聊天背景
        App.appVM.chatBackground.observe(this) {
            window.decorView.background = it
        }

        // 设置当前聊天对象
        currentChat = intent.getParcelableExtra("CurrentChat")!!
        App.appVM.currentChat.value = currentChat

        // 加载历史消息
        vm.loadHistoryMessage()

        // 设置导航栏
        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
        }
        binding.toolbarTitle.text = currentChat.title
        binding.chatIcon.setImageURI(currentChat.iconUrl)

        // 初始化聊天界面列表
        binding.chatList.apply {
            layoutManager = chatMessageLayoutManager
            adapter = chatMessageAdapter
        }

        // 订阅聊天数据
        vm.insertAfter.observe(this) {
            val smooth = chatMessageAdapter.data.isNotEmpty()
            chatMessageAdapter.insertAfterData(it.chatMessages)
            if (it.scrollBottom) {
                scrollBottom(smooth)
            }
        }

        // 监控输入框状态
        vm.inputText.observe(this) {

            if (it.isNullOrBlank()) {
                binding.moreBtn.visibility = View.VISIBLE
                binding.recordLayout.visibility = View.VISIBLE
                binding.sendBtn.visibility = View.GONE
            } else {
                binding.moreBtn.visibility = View.GONE
                binding.recordLayout.visibility = View.GONE
                binding.sendBtn.visibility = View.VISIBLE
            }
        }

        // 监控录制方式变化
        vm.recordType.observe(this) {
            when (it) {
                1 -> {
                    binding.cameraBtn.visibility = View.GONE
                    binding.audioBtn.visibility = View.VISIBLE
                }
                2 -> {
                    binding.cameraBtn.visibility = View.VISIBLE
                    binding.audioBtn.visibility = View.GONE
                }
            }
        }

        // 切换录制方式
        binding.recordLayout.setOnClickListener {
            var v = (vm.recordType.value ?: 1) + 1
            if (v > RecordType.Max) {
                v = RecordType.Min
            }
            vm.recordType.postValue(v)
        }

        // 发文本消息
        binding.sendBtn.setOnClickListener {
            vm.inputText.value?.let { body ->
                val c = ChatMessage(
                    id = (0..9000).random().toString(),
                    messageType = MessageType.Text,
                    time = System.currentTimeMillis(),
                    textBody = body,
                    mine = true
                )
                vm.insertAfter(listOf(c), true)
            }
            vm.inputText.postValue("")
        }

        // 列表滚动事件
        binding.chatList.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                // 当滚动条静止后
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    // 如果在底部, 就触发在底部的事件
                    val atBottom = chatMessageLayoutManager.findLastCompletelyVisibleItemPosition() >= chatMessageLayoutManager.itemCount - 2
                    vm.onRecyclerViewBottom.value = atBottom
                    if (atBottom) {
                        XLog.i("在底部")
                    } else {
                        XLog.i("没有---在底部")
                    }
//                    // 如果在顶部, 就加载历时消息
//                    if (linearLayoutManager.findFirstCompletelyVisibleItemPosition() < 2) {
//                        loadMoreMessage()
//                    }
                }
            }
        })

        // 监控是否在底部
        vm.onRecyclerViewBottom.observe(this) {
            binding.arrowDown.visibility = if (it) View.GONE else View.VISIBLE
        }

        // 滚到底部按钮 点击事件
        binding.arrowDown.setOnSingleClickListener({
            scrollBottom()
        })

    }

    // 滚动到底底部
    private fun scrollBottom(smooth: Boolean = true) {
        binding.chatList.scrollToPosition(chatMessageAdapter.itemCount - 1)
        vm.onRecyclerViewBottom.value = true
//        if (smooth) {
//            // 平滑的滚到底部
//            binding.chatList.smoothScrollToPosition(chatMessageAdapter.itemCount - 1)
//        } else {
//            // 直接的滚到底部
//            binding.chatList.scrollToPosition(chatMessageAdapter.itemCount - 1)
//        }
    }

    override fun onDestroy() {
        super.onDestroy()
        App.appVM.currentChat.value = null
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.chat_toolbar_menu, menu)
        return true
    }
}

class ChatMessageAdapter constructor(private val activity: ChatActivity) :
    BaseAdapter<ChatMessage>() {

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        val binding: ViewDataBinding = holder.dataBinding
        val it = data[position]
        binding.setVariable(BR.it, it)

        // 必要组件
        val rootView = holder.dataBinding.root
        val timeView = rootView.findViewById<TextView>(R.id.time)

        // 清理测试背景
        rootView.background = null

        // 如果是对方发的消息, 并且不是个人聊天
        if (!it.mine) {
            val senderIconView = rootView.findViewById<SimpleDraweeView>(R.id.sender_icon)
            val senderNameView = rootView.findViewById<TextView>(R.id.sender_name)

            if (it.showSender) {
                // 设置发消息人头像
                senderIconView.setImageURI(it.icon)
                senderIconView.visibility = View.VISIBLE
                // 设置发消息人名称
                senderNameView.text = it.name
                senderNameView.visibility = View.VISIBLE
            } else {
                senderIconView.visibility = View.GONE
                senderNameView.visibility = View.GONE
            }
        }

        // 设置消息顶部时间
        if (it.showTime) {
            timeView.visibility = View.VISIBLE
            timeView.text = it.showTimeString
        } else {
            timeView.visibility = View.GONE
        }

        // 如果是文本消息
        if (it.messageType == MessageType.Text) {
            rootView.findViewById<TextView>(R.id.text_body).text = it.textBody
        }


    }

    override fun getItemViewType(position: Int): Int {
        val it = data[position]
        when (it.messageType) {
            MessageType.Text -> {
                return if (it.mine) R.layout.item_chat_message_text_right else R.layout.item_chat_message_text_left
            }
        }
        return R.layout.item_chat_message_text_left
    }

}


object RecordType {
    const val RecordAudio = 1
    const val RecordCamera = 2

    const val Min = RecordAudio
    const val Max = RecordCamera
}

/**
 * 聊天界面
 *
 * 1 = TextMessage
 *
 */

object MessageType {
    const val Text = 0
}

object OpeType {
    const val Private = 0
    const val Teem = 1
}

data class ChatMessage(
    // 必须
    val id: String,
    val messageType: Int,
    val time: Long,
    var showTime: Boolean = true,
    var showTimeString: String = "",
    var ope: Int = OpeType.Private,
    val mine: Boolean = false,

    // 可选
    val icon: String = "",
    val name: String = "",

    // text message
    val textBody: String? = null
) {
    val showSender: Boolean = ope != OpeType.Private
}



