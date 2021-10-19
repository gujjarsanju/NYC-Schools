package com.sanjana.gujjar.nycschools.nycdir.detailsView.model

import com.sanjana.gujjar.nycschools.base.baseEventModel.ActionEvent
import com.sanjana.gujjar.nycschools.base.baseEventModel.StateEvent

sealed class DetailsStateEvent: StateEvent() {
    data class ShowErrorPage(val shouldShow: Boolean, val errorMessage: String) : DetailsStateEvent()
}

sealed class DetailsActionEvent: ActionEvent() {
    data class ShowSchoolList(val schoolDetail: SchoolDetail) : DetailsActionEvent()
}
