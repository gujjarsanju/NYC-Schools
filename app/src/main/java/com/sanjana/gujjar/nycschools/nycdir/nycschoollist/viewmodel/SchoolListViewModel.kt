package com.sanjana.gujjar.nycschools.nycdir.nycschoollist.viewmodel

import android.content.res.Resources
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.*
import com.sanjana.gujjar.nycschools.R
import com.sanjana.gujjar.nycschools.base.viewmodel.NYCSchoolViewModel
import com.sanjana.gujjar.nycschools.nycdir.nycschoollist.model.SchoolListActionEvent
import com.sanjana.gujjar.nycschools.nycdir.nycschoollist.network.NYCSchoolRepository
import com.sanjana.gujjar.nycschools.utils.dispatchers.NYCDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response
import com.sanjana.gujjar.nycschools.nycdir.nycschoollist.model.School

class SchoolListViewModel(
    resources: Resources,
    private val ioDispatcher: CoroutineDispatcher,
    private val NYCSchoolRepository: NYCSchoolRepository
) : NYCSchoolViewModel(TAG, resources) {

    private val _errorView = MutableLiveData<SchoolListActionEvent.ShowErrorPage>()
    val errorView: LiveData<SchoolListActionEvent.ShowErrorPage> = _errorView

    @VisibleForTesting
    fun fetchSchoolList() {
        showProgress(true)
        viewModelScope.launch(ioDispatcher) {
            val response = NYCSchoolRepository.getSchoolList()
            with(response) {
                enqueue(object : retrofit2.Callback<List<School>> {
                    override fun onResponse(
                        call: Call<List<School>>,
                        response: Response<List<School>>
                    ) {
                        showProgress(false)
                        val result = response.body()
                        when {
                            validateResponseData(result) -> {
                                postActionEvent(
                                    SchoolListActionEvent.ShowSchoolList(
                                        getSortedList(result!!)
                                    )
                                )
                            }
                            isEmptyList(result) -> {
                                showRetryPage(resources.getString(R.string.empty_list))
                            }
                            else -> {
                                showRetryPage(resources.getString(R.string.common_ui_error_technical_issue_message))
                            }
                        }
                    }

                    override fun onFailure(call: Call<List<School>>, t: Throwable) {
                        showProgress(false)
                        showRetryPage(resources.getString(R.string.common_ui_error_technical_issue_message))
                    }

                })
            }
        }
    }

    private fun getSortedList(schoolList: List<School>): List<School> {
        return schoolList.sortedBy { it.schoolName }
    }

    @VisibleForTesting
    fun isEmptyList(schoolList: List<School>?): Boolean {
        return schoolList != null && schoolList.isEmpty()
    }

    @VisibleForTesting
    fun showRetryPage(errorMessage: String) {
        postActionEvent(SchoolListActionEvent.ShowErrorPage(true, errorMessage))
    }

    @VisibleForTesting
    fun validateResponseData(schoolList: List<School>?): Boolean {
        when {
            schoolList != null && schoolList.isNotEmpty()  -> {
                schoolList.forEach {
                    val isValid =
                        (it.dbn.isNotBlank()
                                && it.schoolName.isNotBlank())
                    if (!isValid)
                        return isValid
                }
                return true
            }
            else -> {
                return false
            }
        }
    }

    fun showDetails(selectedItem: School) {
        postActionEvent(SchoolListActionEvent.ShowDetailsView(selectedItem))
    }

    class SchoolListViewModelFactory(
        private val resources: Resources,
        private val NYCSchoolRepository: NYCSchoolRepository,
        private val ioDispatcher: CoroutineDispatcher = NYCDispatcher.IO
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return if (modelClass.isAssignableFrom(SchoolListViewModel::class.java)) {
                SchoolListViewModel(resources, ioDispatcher, this.NYCSchoolRepository) as T
            } else {
                throw IllegalArgumentException("SchoolListViewModel Not Found")
            }
        }
    }

    companion object {
        private const val TAG = "SchoolListViewModel"
    }
}