package com.sanjana.gujjar.nycschools.nycdir.view

import android.os.Bundle
import androidx.activity.viewModels
import com.sanjana.gujjar.nycschools.nycdir.viewmodel.SchoolViewModel
import com.sanjana.gujjar.nycschools.utils.registerSharedViewModel
import com.sanjana.gujjar.nycschools.R
import com.sanjana.gujjar.nycschools.base.view.BaseNavActivity

class NYCSchoolActivity : BaseNavActivity(TAG) {
    private val viewModel: SchoolViewModel by viewModels {
        SchoolViewModel.SchoolViewModelFactory(TAG, resources)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        showBackButton(false)
    }

    override fun getNavigationResourceId(): Int = R.navigation.nav_graph

    override fun getNavigationStartArgs(): Bundle? = registerSharedViewModel(viewModel::class.java)


    /**
     * Set a non null resource id to set as start destination, else it will check from the graph
     */
    override fun getNavigationStartDestination(): Int? = null

    companion object {
        var TAG = "NYCSchoolActivity"
    }
}