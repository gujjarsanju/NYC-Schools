package com.sanjana.gujjar.nycschools.base.view

import android.os.Bundle
import android.view.View
import android.view.accessibility.AccessibilityEvent
import androidx.activity.viewModels
import androidx.annotation.IdRes
import androidx.annotation.NavigationRes
import androidx.annotation.VisibleForTesting
import androidx.appcompat.widget.Toolbar
import androidx.navigation.findNavController
import com.sanjana.gujjar.nycschools.R
import com.sanjana.gujjar.nycschools.base.baseEventModel.BaseNavActionEvent
import com.sanjana.gujjar.nycschools.base.viewmodel.BaseNavViewModel
import com.sanjana.gujjar.nycschools.utils.registerSharedViewModel

abstract class BaseNavActivity(TAG:  String):  BaseActivity(TAG) {

    @VisibleForTesting
    internal val navViewModel: BaseNavViewModel by viewModels {
        BaseNavViewModel.BaseNavViewModelFactory(TAG)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        baseConfig.rootFragmentFactory = {
            NavBaseRootFragment().apply {
                arguments = registerSharedViewModel(navViewModel::class.java)
            }
        }
        super.onCreate(savedInstanceState)
        supportFragmentManager.executePendingTransactions()
        wireBaseObservers()
        showToolbar(false)
    }

    private fun wireBaseObservers() {
        navViewModel.actionEvent.observe(this) { event ->
            event.getContentIfNotHandled()?.let {
                if (it is BaseNavActionEvent) {
                    handleBaseActionEvent(it)
                }
            }
        }
    }


    private fun handleBaseActionEvent(actionEvent: BaseNavActionEvent) {
        when (actionEvent) {
            is BaseNavActionEvent.SetupNavControllerAction -> {
                if (actionEvent.needNavGraphSetup) {
                    val stateNavHostFragment = supportFragmentManager.findFragmentByTag(TAG)
                        ?.childFragmentManager
                        ?.findFragmentById(R.id.nav_host_fragment) as? StateNavHostFragment
                    stateNavHostFragment?.let { host ->
                        getNavigationStartDestination()?.let { startDestination ->
                            host.setGraph(
                                getNavigationResourceId(),
                                getNavigationStartArgs()) { graph ->
                                //graph.startDestination = startDestination
                                graph.setStartDestination(startDestination)
                            }
                            host.setStartDestination(startDestination)
                        } ?: host.setGraph(
                            getNavigationResourceId(),
                            getNavigationStartArgs())
                    }
                }
                setupToolbar()
                showToolbar(true)
                onFinishSetup(actionEvent.needNavGraphSetup)
            }
        }
    }

    open fun onFinishSetup(needsInitialSetup : Boolean) {

    }

    @NavigationRes
    abstract fun getNavigationResourceId(): Int

    abstract fun getNavigationStartArgs(): Bundle?

    /**
     * Set a non null resource id to set as start destination, else it will check from the graph
     */
    @IdRes
    abstract fun getNavigationStartDestination(): Int?

    private fun setupToolbar() {
        val navController = findNavController(R.id.nav_host_fragment)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            destination.label?.let {
                toolbar.title = it
                toolbar.importantForAccessibility = View.IMPORTANT_FOR_ACCESSIBILITY_YES
                toolbar.announceForAccessibility(it)
                toolbar.sendAccessibilityEvent(AccessibilityEvent.TYPE_VIEW_FOCUSED)
            }
            if (controller.previousBackStackEntry == null && destination.id == controller.graph.startDestinationId
                || destination.id == getNavigationStartDestination()) {
                if (shouldShowBackButtonByDefault()) {
                    showBackButton(true)
                }
            }
        }
    }

    open fun shouldShowBackButtonByDefault() = true

    fun findRootNavigationController() = findNavController(R.id.nav_host_fragment)
}