package com.pu.demo.app.event

import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import com.pu.demo.R
import com.pu.demo.app.App
import com.pu.demo.app.base.BaseViewModel
import com.pu.demo.model.CurrentChat
import com.pu.demo.model.User


class AppViewModel : BaseViewModel() {

    // 当前用户
    var currentUser = MutableLiveData<User>()

    // 当前聊天对象
    var currentChat = MutableLiveData<CurrentChat>()

    // 首页toolbar标题
    var mainActivityTitle = MutableLiveData<String>()

    var chatBackground = MutableLiveData<Drawable>()

    init {
        currentUser.value = User("3faoen", "李梦琪2", "+86 130-9922-3344", icon = "https://tupian.qqw21.com/article/UploadPic/2021-3/2021352334866731.png")
        mainActivityTitle.value = App.INSTANCE.getString(R.string.app_name)

        chatBackground.value = ContextCompat.getDrawable(App.INSTANCE, R.drawable.test)
    }
}