package com.sanjana.gujjar.nycschools.nycdir.viewmodel

import android.content.res.Resources
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sanjana.gujjar.nycschools.base.viewmodel.NYCSchoolViewModel
import com.sanjana.gujjar.nycschools.nycdir.detailsView.model.DetailsContract
import com.sanjana.gujjar.nycschools.nycdir.model.NYCSchoolListContract
import com.sanjana.gujjar.nycschools.nycdir.nycschoollist.model.School
import com.sanjana.gujjar.nycschools.utils.dispatchers.NYCDispatcher
import kotlinx.coroutines.CoroutineDispatcher

class SchoolViewModel(
    TAG:String,
    resources: Resources,
    private val ioDispatcher: CoroutineDispatcher
): NYCSchoolViewModel(TAG, resources), NYCSchoolListContract, DetailsContract {
    private lateinit var selectSchoolItem: School


    override fun setSelectedSchoolDetails(selectedItem: School) {
        this.selectSchoolItem = selectedItem
    }

    override fun getSelectedItem(): School {
        return this.selectSchoolItem
    }

    class SchoolViewModelFactory(
        private val TAG: String,
        private val resources: Resources,
        private val ioDispatcher: CoroutineDispatcher = NYCDispatcher.IO
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return if (modelClass.isAssignableFrom(SchoolViewModel::class.java)) {
                SchoolViewModel(TAG, resources,ioDispatcher) as T
            } else {
                throw IllegalArgumentException("SchoolViewModel Not Found")
            }
        }
    }
}
