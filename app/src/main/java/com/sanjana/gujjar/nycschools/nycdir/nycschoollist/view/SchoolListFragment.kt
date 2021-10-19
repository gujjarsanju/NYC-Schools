package com.sanjana.gujjar.nycschools.nycdir.nycschoollist.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.VisibleForTesting
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.sanjana.gujjar.nycschools.R
import com.sanjana.gujjar.nycschools.base.android.components.lifecycle.ClearOnDestroyProperty
import com.sanjana.gujjar.nycschools.base.view.BaseFragment
import com.sanjana.gujjar.nycschools.databinding.NycSchoolListFragmentBinding
import com.sanjana.gujjar.nycschools.nycdir.detailsView.model.DetailsContract
import com.sanjana.gujjar.nycschools.nycdir.nycschoollist.model.SchoolListActionEvent
import com.sanjana.gujjar.nycschools.nycdir.nycschoollist.network.NYCSchoolRepository
import com.sanjana.gujjar.nycschools.nycdir.nycschoollist.network.Service
import com.sanjana.gujjar.nycschools.nycdir.nycschoollist.viewmodel.SchoolListViewModel
import com.sanjana.gujjar.nycschools.nycdir.model.NYCSchoolListContract
import com.sanjana.gujjar.nycschools.utils.registerSharedViewModel
import com.sanjana.gujjar.nycschools.utils.shouldShow
import com.sanjana.gujjar.nycschools.utils.viewModelContract

class SchoolListFragment(viewModelFactory: ViewModelProvider.Factory? = null): BaseFragment(TAG) {

    @VisibleForTesting
    internal var binding by ClearOnDestroyProperty<NycSchoolListFragmentBinding> { viewLifecycleOwner.lifecycle }

    private val sharedViewModel: NYCSchoolListContract by viewModelContract()
    private val retrofitService = Service.getInstance()
    private var schoolListAdapter: SchoolDetailsAdapter = SchoolDetailsAdapter(this) { viewModel.showDetails(it)}

    private val viewModel: SchoolListViewModel by viewModels {
        viewModelFactory ?: SchoolListViewModel.SchoolListViewModelFactory(
            resources,
            NYCSchoolRepository(retrofitService)
        )
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = NycSchoolListFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.fetchSchoolList()
        initialize()
        wireObservers()
        clickListeners()
        setFragmentTitle(getString(R.string.nyc_school_list))
        showBackButton(false)
    }

    private fun clickListeners() {
        binding.errorView.tryAgain.setOnClickListener{
            viewModel.fetchSchoolList()
        }
    }

    private fun initialize() {
        binding.list.adapter = schoolListAdapter
    }

    private fun wireObservers() {
        viewModel.actionEvent.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { action ->
                if (action is SchoolListActionEvent) {
                    handleActionEvent(action)
                }
            }
        }
        viewModel.progressEvent.observe(viewLifecycleOwner) {
            binding.progressBar.shouldShow(it)
            if (it)
                binding.errorView.root.shouldShow(!it)
        }
    }

    private fun handleActionEvent(actionEvent: SchoolListActionEvent) {
        when(actionEvent) {
            is SchoolListActionEvent.ShowSchoolList -> {
                binding.list.shouldShow(true)
                schoolListAdapter.updateSchoolList(actionEvent.schoolList)
            }
            is SchoolListActionEvent.ShowErrorPage -> {
                binding.errorView.root.shouldShow(actionEvent.shouldShow)
                binding.errorView.commonUiTextview2.text = actionEvent.errorMessage
                binding.progressBar.shouldShow(!actionEvent.shouldShow)
                binding.list.shouldShow(!actionEvent.shouldShow)
            }
            is SchoolListActionEvent.ShowDetailsView -> {
                sharedViewModel.setSelectedSchoolDetails(actionEvent.school)
                findNavController().navigate(
                    R.id.action_schoolListfragment_to_detailviewfragment,
                    registerSharedViewModel<DetailsContract>(requireArguments())
                )
            }
        }
    }

    companion object {
        const val TAG = "SchoolListFragment"
    }
}