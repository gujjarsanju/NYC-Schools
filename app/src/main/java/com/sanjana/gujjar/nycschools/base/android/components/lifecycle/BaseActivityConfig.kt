package com.sanjana.gujjar.nycschools.base.android.components.lifecycle

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

/**
 * This config object represents the customization options that are provided when utilizing the
 * BaseActivity.  Return a BaseActivityConfig with the desired options in
 * BaseActivity#baseActivityConfig() to achieve a non-default BaseActivity configuration
 */
class BaseActivityConfig {
    /**
     * This function provides a root fragment that gets injected into the activity layout.  Custom
     * UX for a particular feature or user journey belong in the root fragment. Null value means
     * no fragment gets injected
     */
    var rootFragmentFactory: (() -> Fragment)? = null

    /**
     * This flag instructs the BaseActivity to show a [BottomNavigationView] at the bottom of the
     */
    var shouldShowBottomNavBar: Boolean = false

    /**
     * This flag instructs the activity to hide all toolbar / bottom bar.  Further more, it stretches content
     * over the top part of the screen (under the status bar)
     */
    var isFullScreen: Boolean = false

    /**
     * A view that will be placed on top of the toolbar, allowing the super class to provide a non-standard
     * toolbar layout (e.g. left aligned action, centered icon...etc)
     */
    var toolbarOverlayFactory: ((context: Context, overlayContainer: ViewGroup?) -> View)? = null
}