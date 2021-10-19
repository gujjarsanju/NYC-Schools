package com.sanjana.gujjar.nycschools.base.baseEventModel

sealed class BaseActionEvent: ActionEvent() {
    data class TrackFailureEvent(val message: String?) : BaseActionEvent()
    data class TrackWarningEvent(val message: String?) : BaseActionEvent()
}
