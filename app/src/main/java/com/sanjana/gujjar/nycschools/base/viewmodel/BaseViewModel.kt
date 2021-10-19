package com.sanjana.gujjar.nycschools.base.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sanjana.gujjar.nycschools.base.android.components.livedata.Event
import com.sanjana.gujjar.nycschools.base.baseEventModel.ActionEvent
import com.sanjana.gujjar.nycschools.base.baseEventModel.MessageEvent

abstract class BaseViewModel(TAG: String): ViewModel(){

    private val _messageEvent = MutableLiveData<Event<MessageEvent>>()
    val messageEvent: LiveData<Event<MessageEvent>> = _messageEvent

    private val _actionEvent = MutableLiveData<Event<ActionEvent>>()
    val actionEvent: LiveData<Event<ActionEvent>> = _actionEvent

    private val _progressEvent = MutableLiveData<Boolean>()
    val progressEvent: LiveData<Boolean> = _progressEvent

    fun postActionEvent(action: ActionEvent) {
        _actionEvent.postValue(Event(action))
    }

    fun postMessageEvent(message: MessageEvent) {
        _messageEvent.postValue(Event(message))
    }

    fun showProgress(show: Boolean) {
        _progressEvent.postValue(show)
    }

    open fun getSourcePage(): String {
        return ""
    }
}