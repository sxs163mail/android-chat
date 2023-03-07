package com.pu.demo.ui.group.list

import androidx.lifecycle.MutableLiveData
import com.pu.demo.app.base.BaseViewModel

class GroupListViewModel: BaseViewModel() {

    var groupList = MutableLiveData<List<UIGroup>>()

    init {
        val list = mutableListOf<UIGroup>()

//        for (v in 0..2000) {
            list+= UIGroup("班级群1", "https://www.bizhizj.com/d/file/2020-07-14/2bc6c4b10a2811be4d72cc5b1bb570a6.jpg", 12)
            list+= UIGroup("泳池の派对", "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fc-ssl.duitang.com%2Fuploads%2Fblog%2F202107%2F23%2F20210723190856_22391.jpg&refer=http%3A%2F%2Fc-ssl.duitang.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1679543771&t=73f0a98fbaa908ddaf13731dbcef25bd", 30)
            list+= UIGroup("李二娘的群组", "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fc-ssl.duitang.com%2Fuploads%2Fblog%2F202106%2F23%2F20210623175340_03f0d.thumb.1000_0.jpg&refer=http%3A%2F%2Fc-ssl.duitang.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1679490866&t=727f78803f277e188e32d2f17c4652d6", 100)
            list+= UIGroup("王二嫂1号群", "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fc-ssl.duitang.com%2Fuploads%2Fblog%2F202106%2F13%2F20210613214313_618dc.thumb.1000_0.jpg&refer=http%3A%2F%2Fc-ssl.duitang.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1679490866&t=bd05499022b9ba4cf2636777eaae3b8b", 500)
//        }

        this.groupList.postValue(list)
    }

}