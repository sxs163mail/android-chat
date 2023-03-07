package com.pu.demo.ui.group.create

import androidx.lifecycle.MutableLiveData
import com.pu.demo.app.base.BaseViewModel


class GroupCreateViewModel : BaseViewModel() {

    var selectedList = MutableLiveData<List<UIContact4GroupCreate>>()

    var groupName = MutableLiveData<String>("")
    var checkDataOk = MutableLiveData<Boolean> (false)
}