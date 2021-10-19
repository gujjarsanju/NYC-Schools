package com.sanjana.gujjar.nycschools.base.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sanjana.gujjar.nycschools.base.baseEventModel.BaseNavActionEvent
import com.sanjana.gujjar.nycschools.base.baseEventModel.BaseNavRootContract

class BaseNavViewModel(TAG: String) : BaseViewModel(TAG),
    BaseNavRootContract {

    override fun setupNavController(needNavGraphSetup: Boolean) {
        postActionEvent(BaseNavActionEvent.SetupNavControllerAction(needNavGraphSetup))
    }

    companion object {
        const val TAG = "BaseNavViewModel"
    }

    class BaseNavViewModelFactory(
        private val TAG: String
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return if (modelClass.isAssignableFrom(BaseNavViewModel::class.java)) {
                BaseNavViewModel(TAG) as T
            } else {
                throw IllegalArgumentException("BaseNavViewModel Not Found")
            }
        }
    }
}