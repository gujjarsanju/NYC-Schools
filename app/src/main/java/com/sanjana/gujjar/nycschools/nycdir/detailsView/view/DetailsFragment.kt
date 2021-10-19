package com.sanjana.gujjar.nycschools.nycdir.detailsView.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.VisibleForTesting
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.sanjana.gujjar.nycschools.R
import com.sanjana.gujjar.nycschools.base.android.components.lifecycle.ClearOnDestroyProperty
import com.sanjana.gujjar.nycschools.base.view.BaseFragment
import com.sanjana.gujjar.nycschools.databinding.NycSchoolDetailFragmentBinding
import com.sanjana.gujjar.nycschools.nycdir.detailsView.model.DetailsActionEvent
import com.sanjana.gujjar.nycschools.nycdir.detailsView.model.DetailsContract
import com.sanjana.gujjar.nycschools.nycdir.detailsView.model.DetailsStateEvent
import com.sanjana.gujjar.nycschools.nycdir.detailsView.viewmodel.DetailsViewModel
import com.sanjana.gujjar.nycschools.nycdir.nycschoollist.network.NYCSchoolRepository
import com.sanjana.gujjar.nycschools.nycdir.nycschoollist.network.Service
import com.sanjana.gujjar.nycschools.utils.shouldShow
import com.sanjana.gujjar.nycschools.utils.viewModelContract

class DetailsFragment(viewModelFactory: ViewModelProvider.Factory? = null): BaseFragment(TAG)  {

    @VisibleForTesting
    internal var binding by ClearOnDestroyProperty<NycSchoolDetailFragmentBinding> { viewLifecycleOwner.lifecycle }

    private val sharedViewModel: DetailsContract by viewModelContract()
    private val retrofitService = Service.getInstance()

    private val viewModel: DetailsViewModel by viewModels {
        viewModelFactory ?: DetailsViewModel.DetailsViewModelFactory(
            resources = resources,
            NYCSchoolRepository = NYCSchoolRepository(retrofitService),
            selectedItem = sharedViewModel.getSelectedItem()
        )
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = NycSchoolDetailFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.fetchSatScoreList()
        clickListeners()
        wireObservers()
        showBackButton(true)
        setFragmentTitle(getString(R.string.details))
    }

    private fun clickListeners() {
        binding.errorView.tryAgain.setOnClickListener{
            viewModel.fetchSatScoreList()
        }
    }

    private fun wireObservers() {
        viewModel.actionEvent.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { action ->
                if (action is DetailsActionEvent) {
                    handleActionEvent(action)
                }
            }
        }

        viewModel.errorView.observe(viewLifecycleOwner) {
            handleStateEvent(it)
        }

        viewModel.progressEvent.observe(viewLifecycleOwner) {
            binding.progressBar.shouldShow(it)
            if (it)
                binding.errorView.root.shouldShow(!it)
        }
    }

    private fun handleStateEvent(stateEvent: DetailsStateEvent) {
        when(stateEvent) {
            is DetailsStateEvent.ShowErrorPage -> {
                binding.errorView.root.shouldShow(stateEvent.shouldShow)
                binding.errorView.commonUiTextview2.text = stateEvent.errorMessage
                binding.progressBar.shouldShow(!stateEvent.shouldShow)
                binding.nycSchoolDetails.shouldShow(!stateEvent.shouldShow)
            }
        }
    }

    private fun handleActionEvent(actionEvent: DetailsActionEvent) {
        when(actionEvent) {
            is DetailsActionEvent.ShowSchoolList -> {
                binding.nycSchoolDetails.shouldShow(true)
                binding.nycDetailName.text = actionEvent.schoolDetail.schoolName
                binding.nycOverview.text = resources.getString(R.string.Overview,sharedViewModel.getSelectedItem().overviewParagraph)
                binding.nycSatTotal.text = resources.getString(R.string.sat_total,actionEvent.schoolDetail.totalSatTakers)
                binding.nycSatMath.text = resources.getString(R.string.sat_math,actionEvent.schoolDetail.satMathScore)
                binding.nycSatReading.text = resources.getString(R.string.sat_reading,actionEvent.schoolDetail.satAvgRedersScore)
                binding.nycSatWrite.text = resources.getString(R.string.sat_writing,actionEvent.schoolDetail.satAvgWritingScore)
            }
        }
    }

    override fun onHandleBackPress(): Boolean {
        return super.onHandleBackPress()
    }

    companion object {
        const val TAG = "DetailsFragment"
    }
}