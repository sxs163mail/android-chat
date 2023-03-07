package com.pu.demo.ui.chat

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.elvishew.xlog.XLog
import com.pu.demo.app.App
import com.pu.demo.app.base.BaseViewModel
import com.pu.demo.utils.TimeUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class ChatViewModel : BaseViewModel() {

    var insertAfter = MutableLiveData<InsertAfterData>()
    var inputText = MutableLiveData<String>()
    var recordType = MutableLiveData<Int>()
    var onRecyclerViewBottom = MutableLiveData<Boolean>(true)


    init {
        recordType.postValue(1)

        viewModelScope.launch(Dispatchers.IO) {
            for (n in 0..2) {
                delay(3000)
                val a = mutableListOf<ChatMessage>()
                a += ChatMessage(
                    id = (0..9000).random().toString(),
                    messageType = MessageType.Text,
                    time = System.currentTimeMillis(),
                    textBody = "adfasdfa出vv啊AV大迪欧；几把点开啊；单价法i哦额王府井阿迪克来拿v那adfasdfa出vv啊AV大迪欧；几把点开啊；单价法i哦额王府井阿迪克来拿v那adfasdfa出vv啊AV大迪欧；几把点开啊；单价法i哦额王府井阿迪克来拿v那adfasdfa出vv啊AV大迪欧；几把点开啊；单价法i哦额王府井阿迪克来拿v那adfasdfa出vv啊AV大迪欧；几把点开啊；单价法i哦额王府井阿迪克来拿v那adfasdfa出vv啊AV大迪欧；几把点开啊；单价法i哦额王府井阿迪克来拿v那adfasdfa出vv啊AV大迪欧；几把点开啊；单价法i哦额王府井阿迪克来拿v那",
                    name = "张三",
                    icon = "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fc-ssl.duitang.com%2Fuploads%2Fblog%2F202106%2F13%2F20210613214313_618dc.thumb.1000_0.jpg&refer=http%3A%2F%2Fc-ssl.duitang.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1679490866&t=bd05499022b9ba4cf2636777eaae3b8b",
                    ope = App.appVM.currentChat.value!!.ope
                )
                insertAfter(a, onRecyclerViewBottom.value?:true)
            }
        }
    }

    fun loadHistoryMessage() {

        XLog.i("历史消息类型: ${App.appVM.currentChat.value!!.ope}")

        val chatMessageList = mutableListOf<ChatMessage>()

        chatMessageList += ChatMessage(
            id = (0..9000).random().toString(),
            messageType = MessageType.Text,
            time = SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss",
                Locale.getDefault()
            ).parse("2022-11-11 11:11:11")!!.time,
            textBody = "你好呵呵",
            name = "张三",
            icon = "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fc-ssl.duitang.com%2Fuploads%2Fblog%2F202106%2F13%2F20210613214313_618dc.thumb.1000_0.jpg&refer=http%3A%2F%2Fc-ssl.duitang.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1679490866&t=bd05499022b9ba4cf2636777eaae3b8b",
            mine = false,
            ope = App.appVM.currentChat.value!!.ope
        )

        for (i in 0..20) {
            chatMessageList += ChatMessage(
                id = (0..9000).random().toString(),
                messageType = MessageType.Text,
                time = SimpleDateFormat(
                    "yyyy-MM-dd HH:mm:ss",
                    Locale.getDefault()
                ).parse("2022-11-11 11:11:11")!!.time,
                textBody = "adfasdfa出vv啊AV大迪欧；几把点开啊；单价法i哦额王府井阿迪克来拿v那adfasdfa出vv啊AV大迪欧；几把点开啊；单价法i哦额王府井阿迪克来拿v那adfasdfa出vv啊AV大迪欧；几把点开啊；单价法i哦额王府井阿迪克来拿v那adfasdfa出vv啊AV大迪欧；几把点开啊；单价法i哦额王府井阿迪克来拿v那adfasdfa出vv啊AV大迪欧；几把点开啊；单价法i哦额王府井阿迪克来拿v那adfasdfa出vv啊AV大迪欧；几把点开啊；单价法i哦额王府井阿迪克来拿v那adfasdfa出vv啊AV大迪欧；几把点开啊；单价法i哦额王府井阿迪克来拿v那",
                name = "张三",
                icon = "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fc-ssl.duitang.com%2Fuploads%2Fblog%2F202106%2F13%2F20210613214313_618dc.thumb.1000_0.jpg&refer=http%3A%2F%2Fc-ssl.duitang.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1679490866&t=bd05499022b9ba4cf2636777eaae3b8b",
                ope = App.appVM.currentChat.value!!.ope
            )
        }

        chatMessageList += ChatMessage(
            id = (0..9000).random().toString(),
            messageType = MessageType.Text,
            time = SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss",
                Locale.getDefault()
            ).parse("2022-11-12 11:11:11")!!.time,
            textBody = "你好呵呵",
            name = "张三",
            icon = "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fc-ssl.duitang.com%2Fuploads%2Fblog%2F202106%2F13%2F20210613214313_618dc.thumb.1000_0.jpg&refer=http%3A%2F%2Fc-ssl.duitang.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1679490866&t=bd05499022b9ba4cf2636777eaae3b8b",
            mine = true,
            ope = App.appVM.currentChat.value!!.ope
        )
        insertAfter(chatMessageList)
    }

    fun insertAfter(chatMessages: List<ChatMessage>, scrollBottom: Boolean = true) {
        val chatMessageList = mutableListOf<ChatMessage>()
        chatMessages.forEach { chatMessage ->

            val lastTime = if (chatMessageList.isEmpty()) {
                this.insertAfter.value?.lastItem?.time ?: 0
            } else chatMessageList.last().time

            val t = TimeUtils.chatMessageShowTime(before = lastTime, current = chatMessage.time)
            chatMessage.showTimeString = t ?: ""
            chatMessage.showTime = t != null
            chatMessageList += chatMessage
        }

        this.insertAfter.postValue(InsertAfterData(chatMessages = chatMessageList, lastItem = chatMessageList.last(), scrollBottom = scrollBottom))
    }

}

data class InsertAfterData(
    val chatMessages: List<ChatMessage>,
    val lastItem: ChatMessage? = null,
    val scrollBottom: Boolean
)