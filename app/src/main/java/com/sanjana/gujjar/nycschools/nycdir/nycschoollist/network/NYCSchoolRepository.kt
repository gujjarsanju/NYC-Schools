package com.sanjana.gujjar.nycschools.nycdir.nycschoollist.network

import androidx.annotation.VisibleForTesting

class NYCSchoolRepository(private val retrofitService: Service) {
    @VisibleForTesting
    fun getSchoolList() = retrofitService.getSchoolList()

    @VisibleForTesting
    fun getSchoolDetails() = retrofitService.getSchoolDetails()
}