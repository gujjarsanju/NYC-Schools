package com.sanjana.gujjar.nycschools.base.view

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.annotation.NavigationRes
import androidx.navigation.NavDirections
import androidx.navigation.NavGraph
import androidx.navigation.NavInflater
import androidx.navigation.NavOptions

/**
 * A GraphInflater describes how to inflate a graph and navigate to a start-destination.
 */
interface GraphInflater {
    @get:NavigationRes
    val graphId: Int

    fun inflate(inflater: NavInflater): NavGraph
    val startDestination: NavDirections
    val navOptions: NavOptions?
}

/**
 * Creates a default GraphInflater
 */
fun createGraphInflater(
    @NavigationRes graphId: Int,
    @IdRes destinationResId: Int,
    destinationArgs: Bundle? = null,
    navOptions: NavOptions? = null
) = object : GraphInflater {

    override val graphId: Int
        get() = graphId

    override fun inflate(inflater: NavInflater): NavGraph = inflater.inflate(graphId)

    override val startDestination: NavDirections
        get() = object : NavDirections {
            override val actionId: Int = destinationResId

            override val arguments: Bundle
                get() = destinationArgs ?:  Bundle()
        }

    override val navOptions: NavOptions?
        get() = navOptions
}
