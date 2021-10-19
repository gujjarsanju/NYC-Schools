package com.sanjana.gujjar.nycschools.base.baseEventModel

sealed class BaseNavActionEvent : ActionEvent() {
    data class SetupNavControllerAction(
        val needNavGraphSetup: Boolean = true
    ) : BaseNavActionEvent()
}