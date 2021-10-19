package com.sanjana.gujjar.nycschools.nycdir.nycschoollist.viewmodel

import android.content.res.Resources
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.sanjana.gujjar.nycschools.R
import com.sanjana.gujjar.nycschools.nycdir.nycschoollist.model.School
import com.sanjana.gujjar.nycschools.nycdir.nycschoollist.model.SchoolListActionEvent
import com.sanjana.gujjar.nycschools.nycdir.nycschoollist.network.NYCSchoolRepository
import com.sanjana.gujjar.nycschools.nycdir.nycschoollist.network.Service
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.junit.Assert
import org.junit.Rule

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class SchoolListViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: SchoolListViewModel
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
        viewModel = constructViewModel(resources = resources, nycSchoolRepository = repository)
    }

    @AfterEach
    fun tearDown() {
    }

    @Test
    fun fetchSchoolList() {
        every { service.getSchoolList() } returns mockk{}
        val testList = listOf<School>()
        coEvery { repository.getSchoolList() } returns mockk {
            run { viewModel.postActionEvent(SchoolListActionEvent.ShowSchoolList(testList)) }
        }
        val event = viewModel.actionEvent.value?.getContentIfNotHandled()
        assert(event is SchoolListActionEvent.ShowSchoolList)
        Assert.assertEquals(testList, (event as SchoolListActionEvent.ShowSchoolList).schoolList)
    }

    @Test
    fun isEmptyList() {
        val isEmpty = viewModel.isEmptyList(listOf())
        Assert.assertEquals(true,isEmpty)
    }

    @Test
    fun showRetryPage() {
        viewModel.showRetryPage(empty_list)
        val event = viewModel.actionEvent.value?.getContentIfNotHandled()
        assert(event is SchoolListActionEvent.ShowErrorPage)
        Assert.assertEquals(
            empty_list,
            (event as SchoolListActionEvent.ShowErrorPage).errorMessage
        )
    }

    @Test
    fun validateResponseData() {
        val value = viewModel.validateResponseData(schoolList)
        Assert.assertEquals(true,value)
    }

    @Test
    fun showDetails() {
        viewModel.showDetails(schoolList.first())
        val event = viewModel.actionEvent.value?.getContentIfNotHandled()
        assert(event is SchoolListActionEvent.ShowDetailsView)
        Assert.assertEquals(
            schoolList.first(),
            (event as SchoolListActionEvent.ShowDetailsView).school
        )
    }

    private fun constructViewModel(
        resources: Resources,
        ioDispatcher: CoroutineDispatcher = Dispatchers.Unconfined,
        nycSchoolRepository: NYCSchoolRepository
    ): SchoolListViewModel = SchoolListViewModel(resources, ioDispatcher, nycSchoolRepository)

    companion object {
        val empty_list  = "NO ITEM FOUND."

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