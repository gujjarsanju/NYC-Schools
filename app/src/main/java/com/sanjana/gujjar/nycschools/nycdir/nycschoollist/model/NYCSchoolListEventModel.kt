package com.sanjana.gujjar.nycschools.nycdir.nycschoollist.model

import com.sanjana.gujjar.nycschools.base.baseEventModel.ActionEvent

sealed class SchoolListActionEvent: ActionEvent() {
    data class ShowSchoolList(val schoolList: List<School>) : SchoolListActionEvent()
    data class ShowErrorPage(val shouldShow: Boolean, val errorMessage: String) : SchoolListActionEvent()
    data class ShowDetailsView(val school: School) : SchoolListActionEvent()
}