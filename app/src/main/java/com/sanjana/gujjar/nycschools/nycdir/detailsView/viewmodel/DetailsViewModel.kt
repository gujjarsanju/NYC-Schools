package com.sanjana.gujjar.nycschools.nycdir.detailsView.viewmodel

import android.content.res.Resources
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.*
import com.sanjana.gujjar.nycschools.R
import com.sanjana.gujjar.nycschools.base.viewmodel.NYCSchoolViewModel
import com.sanjana.gujjar.nycschools.nycdir.detailsView.model.DetailsActionEvent
import com.sanjana.gujjar.nycschools.nycdir.detailsView.model.DetailsStateEvent
import com.sanjana.gujjar.nycschools.nycdir.detailsView.model.SchoolDetail
import com.sanjana.gujjar.nycschools.nycdir.nycschoollist.model.School
import com.sanjana.gujjar.nycschools.nycdir.nycschoollist.network.NYCSchoolRepository
import com.sanjana.gujjar.nycschools.utils.dispatchers.NYCDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response

class DetailsViewModel(resources: Resources,
                       private val ioDispatcher: CoroutineDispatcher,
                       private val NYCSchoolRepository: NYCSchoolRepository,
                       private val selectedItem: School
) : NYCSchoolViewModel(TAG, resources) {

    private val _errorView = MutableLiveData<DetailsStateEvent.ShowErrorPage>()
    val errorView: LiveData<DetailsStateEvent.ShowErrorPage> = _errorView


    @VisibleForTesting
    fun fetchSatScoreList() {
        showProgress(true)
        viewModelScope.launch(ioDispatcher) {
            val response = NYCSchoolRepository.getSchoolDetails()
            with(response) {
                enqueue(object : retrofit2.Callback<List<SchoolDetail>> {
                    override fun onResponse(
                        call: Call<List<SchoolDetail>>,
                        response: Response<List<SchoolDetail>>
                    ) {
                        showProgress(false)
                        when {
                            hasElement(response.body()) -> {
                                postActionEvent(
                                    DetailsActionEvent.ShowSchoolList(
                                        getSelectedSchool(response.body()!!)
                                    )
                                )
                            }
                            isEmptyList(response.body()) -> {
                                showRetryPage(resources.getString(R.string.empty_list))
                            }
                            else -> {
                                showRetryPage(resources.getString(R.string.common_ui_error_technical_issue_message))
                            }
                        }
                    }

                    override fun onFailure(call: Call<List<SchoolDetail>>, t: Throwable) {
                        showProgress(false)
                        showRetryPage(resources.getString(R.string.common_ui_error_technical_issue_message))
                    }

                })
            }
        }
    }

    private fun getSelectedSchool(schoolList: List<SchoolDetail>): SchoolDetail {
        schoolList.forEach {
            if (it.dbn == selectedItem.dbn) {
                return it
            }
        }
        return SchoolDetail(
            dbn = selectedItem.dbn,
            schoolName = selectedItem.schoolName,
            "NA",
            "NA",
            "NA",
            "NA"
        )
    }

    @VisibleForTesting
    fun isEmptyList(schoolList: List<SchoolDetail>?): Boolean {
        return schoolList != null && schoolList.isEmpty()
    }

    @VisibleForTesting
    fun showRetryPage(errorMessage: String) {
        _errorView.value = DetailsStateEvent.ShowErrorPage(true, errorMessage)
    }

    fun hasElement(schoolList: List<SchoolDetail>?): Boolean {
        schoolList?.forEach {
            if (it.dbn == selectedItem.dbn) {
                return true
            }
        }
        return false
    }

    class DetailsViewModelFactory(
        private val resources: Resources,
        private val NYCSchoolRepository: NYCSchoolRepository,
        private val ioDispatcher: CoroutineDispatcher = NYCDispatcher.IO,
        private val selectedItem: School
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return if (modelClass.isAssignableFrom(DetailsViewModel::class.java)) {
                DetailsViewModel(resources, ioDispatcher, this.NYCSchoolRepository,selectedItem) as T
            } else {
                throw IllegalArgumentException("DetailsViewModel Not Found")
            }
        }
    }

    companion object {
        private const val TAG = "DetailsViewModel"
    }

}