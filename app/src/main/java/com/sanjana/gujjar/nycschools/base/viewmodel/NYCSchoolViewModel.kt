package com.sanjana.gujjar.nycschools.base.viewmodel

import android.content.res.Resources
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.sanjana.gujjar.nycschools.R
import com.sanjana.gujjar.nycschools.base.android.components.livedata.Event
import com.sanjana.gujjar.nycschools.base.baseEventModel.BaseActionEvent
import com.sanjana.gujjar.nycschools.base.baseEventModel.MessageEvent

abstract class NYCSchoolViewModel(
    TAG: String,
    val resources: Resources
): BaseViewModel(TAG) {
    private val _failureEvent = MutableLiveData<Event<BaseActionEvent.TrackFailureEvent>>()
    val failureEvent: LiveData<Event<BaseActionEvent.TrackFailureEvent>> get() = _failureEvent

    private val _warningEvent = MutableLiveData<Event<BaseActionEvent.TrackWarningEvent>>()
    val warningEvent: LiveData<Event<BaseActionEvent.TrackWarningEvent>> get() = _warningEvent


    fun postFailureEvent(message: String) {
        _failureEvent.postValue(Event(BaseActionEvent.TrackFailureEvent(message)))
    }

    fun postWarningEvent(message: String) {
        _warningEvent.postValue(Event(BaseActionEvent.TrackWarningEvent(message)))
    }

    fun showBasicMessage(message: String, title: String? = null) {
        postMessageEvent(
            MessageEvent(
                id = DIALOG_GENERIC,
                title = title,
                message = message,
                positiveOptionLabel = resources.getString(R.string.common_ok)
            )
        )
    }

    companion object {
        const val DIALOG_GENERIC = -1042
    }
}