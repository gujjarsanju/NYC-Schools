package com.sanjana.gujjar.nycschools.base.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sanjana.gujjar.nycschools.R
import com.sanjana.gujjar.nycschools.base.baseEventModel.BaseNavRootContract
import com.sanjana.gujjar.nycschools.utils.viewModelContract

class NavBaseRootFragment: BaseFragment(TAG) {

    private val sharedViewModel: BaseNavRootContract by viewModelContract()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        requireActivity()
        val view = inflater.inflate(R.layout.common_ui_base_nav_host_fragment, container, false)
        sharedViewModel.setupNavController(savedInstanceState == null)
        return view;
    }

    companion object {
        const val TAG = "NavBaseRootFragment"
    }
}