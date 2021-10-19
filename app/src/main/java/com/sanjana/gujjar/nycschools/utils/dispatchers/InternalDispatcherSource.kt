package com.sanjana.gujjar.nycschools.utils.dispatchers

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainCoroutineDispatcher

/**
 * Provides [kotlinx.coroutines.CoroutineDispatcher] of the same name and purpose as those [Dispatchers].
 *
 * Instances of this class should not be accessed directly
 */
interface InternalDispatcherSource {

    fun io() = Dispatchers.IO
    fun main(): MainCoroutineDispatcher = Dispatchers.Main
    fun default() = Dispatchers.Default
    fun unconfined() = Dispatchers.Unconfined
}