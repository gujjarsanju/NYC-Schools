package com.sanjana.gujjar.nycschools.nycdir.model

import com.sanjana.gujjar.nycschools.nycdir.nycschoollist.model.School

interface NYCSchoolListContract {
    fun setSelectedSchoolDetails(selectedItem: School)
}