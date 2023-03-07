package com.pu.demo.ui.group.select

import androidx.lifecycle.MutableLiveData
import com.pu.demo.app.base.BaseViewModel


class GroupMemberSelectViewModel : BaseViewModel() {

    var contactList = MutableLiveData<List<UIContact4GroupSelect>>()
    var selectedList = MutableLiveData<List<UIContact4GroupSelected>>(emptyList())
    var etSearchInput = MutableLiveData("")

    var unSelectedItemId = MutableLiveData<String>()

    init {
        val recentContactList = mutableListOf<UIContact4GroupSelect>()
        val day:Long = 1000 * 60 * 60 * 24

        for (v in 0..2000) {
            recentContactList+= UIContact4GroupSelect("赵六$v","赵六", "https://www.bizhizj.com/d/file/2020-07-14/2bc6c4b10a2811be4d72cc5b1bb570a6.jpg", System.currentTimeMillis() )
            recentContactList+= UIContact4GroupSelect("七月$v", "七月", "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fc-ssl.duitang.com%2Fuploads%2Fblog%2F202107%2F23%2F20210723190856_22391.jpg&refer=http%3A%2F%2Fc-ssl.duitang.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1679543771&t=73f0a98fbaa908ddaf13731dbcef25bd", System.currentTimeMillis()-day * 1)
            recentContactList+= UIContact4GroupSelect("李二娘$v","李二娘", "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fc-ssl.duitang.com%2Fuploads%2Fblog%2F202106%2F23%2F20210623175340_03f0d.thumb.1000_0.jpg&refer=http%3A%2F%2Fc-ssl.duitang.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1679490866&t=727f78803f277e188e32d2f17c4652d6", System.currentTimeMillis()-day * 2)
            recentContactList+= UIContact4GroupSelect("王二嫂adsfasdfadf$v","王二嫂adsfasdfadf", "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fc-ssl.duitang.com%2Fuploads%2Fblog%2F202106%2F13%2F20210613214313_618dc.thumb.1000_0.jpg&refer=http%3A%2F%2Fc-ssl.duitang.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1679490866&t=bd05499022b9ba4cf2636777eaae3b8b", System.currentTimeMillis()-day * 3)
            recentContactList+= UIContact4GroupSelect("八月asdfasdfaf$v","八月asdfasdfaf", "https://inews.gtimg.com/newsapp_bt/0/13821262541/1000", System.currentTimeMillis()-day * 6)
            recentContactList+= UIContact4GroupSelect("王五$v","王五", "https://inews.gtimg.com/newsapp_bt/0/13503621610/1000", System.currentTimeMillis()-day * 8)
            recentContactList+= UIContact4GroupSelect("赵四$v","赵四", "https://inews.gtimg.com/newsapp_bt/0/13821262534/1000", System.currentTimeMillis()-day * 31)
        }

        this.contactList.postValue(recentContactList)
    }

}