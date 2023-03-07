package com.pu.demo.ui.user.profile

import androidx.lifecycle.MutableLiveData
import com.pu.demo.app.base.BaseViewModel
import com.pu.demo.model.User

class UserProfileViewModel : BaseViewModel() {

    var user = MutableLiveData<User>()

    init {
        user.postValue(User(id = "afad", name = "Mino", icon = "https://inews.gtimg.com/newsapp_bt/0/13821262541/1000", phone = "12030232"))
    }
}