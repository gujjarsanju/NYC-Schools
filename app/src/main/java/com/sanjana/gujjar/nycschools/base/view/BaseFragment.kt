package com.sanjana.gujjar.nycschools.base.view

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.activity.OnBackPressedCallback
import androidx.annotation.LayoutRes
import androidx.annotation.RequiresApi
import androidx.annotation.StringRes
import androidx.annotation.VisibleForTesting
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.MenuItemCompat
import androidx.fragment.app.Fragment
import com.sanjana.gujjar.nycschools.R

/**
 * Base fragment for App
 */

abstract class BaseFragment(TAG: String, @LayoutRes contentLayoutId: Int = 0) :
    Fragment(contentLayoutId) {


    private var shouldShowCancelButton: Boolean = false
    private var shouldShowWhiteToolbar: Boolean = false

    private val backPressedCallback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            isEnabled = false
            if (!onHandleBackPress()) {
                requireActivity().onBackPressed()
            }
            isEnabled = true
        }
    }

    protected open fun onHandleBackPress(): Boolean {
        return false
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        requireActivity().onBackPressedDispatcher.addCallback(this, backPressedCallback)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.setHasOptionsMenu(true)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        if (shouldShowCancelButton) {
            inflater.inflate(R.menu.common_main_menu, menu)
            val icon =
                ResourcesCompat.getDrawable(resources, android.R.drawable.ic_menu_close_clear_cancel, null)
                    ?.mutate()
            if (!shouldShowWhiteToolbar) {
                icon?.setTint(resources.getColor(R.color.white, null))
            }
            menu.findItem(R.id.closeButton).icon = icon
            MenuItemCompat.setContentDescription(
                menu.findItem(R.id.closeButton),
                resources.getString(R.string.common_close_button)
            )
        }
        super.onCreateOptionsMenu(menu, inflater)
    }

    /*
     Should be called in onView created, so that while onCreateOptionsMenu is called, the fragment will know whether cancel should be shown on not
     */
    fun enableCancelButton() {
        shouldShowCancelButton = true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.closeButton -> {
                if (!onCancelPage()) {
                    requireActivity().onBackPressed()
                }
                return true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    /*
    To be overriden to cancel the page
     */
    protected open fun onCancelPage(): Boolean {
        return false
    }

    fun showBackButton(showBackButton: Boolean) {
        val thisActivity = requireActivity()
        if (thisActivity is BaseActivity) {
            thisActivity.showBackButton(showBackButton)
        }
    }

    fun setTitle(@StringRes title: Int) {
        setFragmentTitle(getString(title))
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PROTECTED)
    fun setFragmentTitle(title: String) {
        var parentFragment = parentFragment
        while (parentFragment != null) {
            parentFragment = parentFragment.parentFragment
        }
        activity?.title = title
    }

}