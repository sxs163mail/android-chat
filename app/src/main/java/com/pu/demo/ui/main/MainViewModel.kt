package com.pu.demo.ui.main

import androidx.lifecycle.MutableLiveData
import com.pu.demo.app.base.BaseViewModel
import com.pu.demo.ui.chat.OpeType

class MainViewModel : BaseViewModel() {

    var menuList = MutableLiveData<List<UIDrawerLayoutMenu>>()
    var recentContactList = MutableLiveData<List<UIRecentContact>>()
    var drawerLayoutUser = MutableLiveData<UIDrawerLayoutUser>()

    init {
        

        val recentContactList = mutableListOf<UIRecentContact>()
        recentContactList+=UIRecentContact("泳池の派对 群聊", "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fc-ssl.duitang.com%2Fuploads%2Fblog%2F202106%2F13%2F20210613214313_618dc.thumb.1000_0.jpg&refer=http%3A%2F%2Fc-ssl.duitang.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1679490866&t=bd05499022b9ba4cf2636777eaae3b8b", "9:37 PM", "叔叔 现在还是寒假, 叔叔 现在还是寒假", (0..300).random(), top = true, true, at = true, ope = OpeType.Teem)
        recentContactList+=UIRecentContact("测试2 单聊", "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fc-ssl.duitang.com%2Fuploads%2Fblog%2F202106%2F23%2F20210623175340_03f0d.thumb.1000_0.jpg&refer=http%3A%2F%2Fc-ssl.duitang.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1679490866&t=727f78803f277e188e32d2f17c4652d6", "10:18 PM", "你是王静？你是王静？,你是王静？你是王静？", (0..300).random(), top = false, true, draft = "我想写个草稿")
        recentContactList+=UIRecentContact("测试3 单聊", "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fc-ssl.duitang.com%2Fuploads%2Fblog%2F202107%2F23%2F20210723190856_22391.jpg&refer=http%3A%2F%2Fc-ssl.duitang.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1679543771&t=73f0a98fbaa908ddaf13731dbcef25bd", "Jan 28", "骗我闪现可以，骗我感情不行,骗我闪现可以，骗我感情不行", (1..9).random(), top = false, false)
        recentContactList+=UIRecentContact("测试4 单聊", "https://www.bizhizj.com/d/file/2020-07-14/2bc6c4b10a2811be4d72cc5b1bb570a6.jpg", "Jan 28", "骗我闪现可以，骗我感情不行,骗我闪现可以，骗我感情不行,骗我闪现可以，骗我感情不行", 0, top = false, false)
        this.recentContactList.postValue(recentContactList)

    }

}