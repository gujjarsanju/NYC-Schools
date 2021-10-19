package com.sanjana.gujjar.nycschools.nycdir.detailsView.viewmodel

import android.content.res.Resources
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.sanjana.gujjar.nycschools.R
import com.sanjana.gujjar.nycschools.nycdir.detailsView.model.DetailsActionEvent
import com.sanjana.gujjar.nycschools.nycdir.detailsView.model.SchoolDetail
import com.sanjana.gujjar.nycschools.nycdir.nycschoollist.model.School
import com.sanjana.gujjar.nycschools.nycdir.nycschoollist.model.SchoolListActionEvent
import com.sanjana.gujjar.nycschools.nycdir.nycschoollist.network.NYCSchoolRepository
import com.sanjana.gujjar.nycschools.nycdir.nycschoollist.network.Service
import com.sanjana.gujjar.nycschools.nycdir.nycschoollist.viewmodel.SchoolListViewModelTest
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.junit.Assert
import org.junit.Rule
import org.junit.jupiter.api.BeforeEach

import org.junit.jupiter.api.Test

class DetailsViewModelTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: DetailsViewModel
    private lateinit var resources: Resources
    private lateinit var service: Service
    private lateinit var repository: NYCSchoolRepository

    @BeforeEach
    fun setUp() {
        resources = mockk {
            every { getString(R.string.common_ok) } returns "OK"
            every { getString(R.string.empty_list) } returns "NO ITEM FOUND."
        }
        service = mockk{

        }
        repository = NYCSchoolRepository(service)
        viewModel = constructViewModel(resources = resources, nycRepository = repository, selectedItem = schoolList.first())
    }

    @Test
    fun fetchSatScoreList() {
        every { service.getSchoolDetails() } returns mockk{}
        coEvery { repository.getSchoolDetails() } returns mockk {
            run { viewModel.postActionEvent(DetailsActionEvent.ShowSchoolList(schoolDetail)) }
        }
        val event = viewModel.actionEvent.value?.getContentIfNotHandled()
        assert(event is DetailsActionEvent.ShowSchoolList)
        Assert.assertEquals(schoolDetail, (event as DetailsActionEvent.ShowSchoolList).schoolDetail)
    }

    @Test
    fun isEmptyList() {
        val isEmpty = viewModel.isEmptyList(listOf())
        Assert.assertEquals(true,isEmpty)
    }

    @Test
    fun showRetryPage() {
        viewModel.showRetryPage(empty_list)
        val event = viewModel.errorView
        Assert.assertEquals(
            SchoolListViewModelTest.empty_list,
            (event as SchoolListActionEvent.ShowErrorPage).errorMessage
        )
    }

    @Test
    fun hasElement() {
        val  result = viewModel.hasElement(listOf(schoolDetail))
        Assert.assertEquals(true ,result)
    }


    private fun constructViewModel(
        resources: Resources,
        ioDispatcher: CoroutineDispatcher = Dispatchers.Unconfined,
        nycRepository: NYCSchoolRepository,
        selectedItem: School
    ): DetailsViewModel = DetailsViewModel(resources, ioDispatcher, nycRepository, selectedItem)

    companion object {
        val empty_list  = "NO ITEM FOUND."

        val schoolDetail = SchoolDetail(
            dbn = "0m1234",
            schoolName = "TEST SCHOOL",
            "NA", "NA", "NA", "NA"
        )

        val schoolList = listOf<School>(
            School(
                dbn = "0m1234",
                schoolName = "TEST SCHOOL",
                phoneNumber = "123-234-2373",
                overviewParagraph = "overviewsds",
                location = null,
                emailAddress = null,
                website = null,
                faxNumber = null
            ))
    }
}