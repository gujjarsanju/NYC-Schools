package com.sanjana.gujjar.nycschools.nycdir.detailsView.model

import com.sanjana.gujjar.nycschools.nycdir.nycschoollist.model.School

interface DetailsContract {
    fun getSelectedItem(): School
}