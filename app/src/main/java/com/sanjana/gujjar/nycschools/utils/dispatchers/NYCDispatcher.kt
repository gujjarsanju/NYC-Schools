package com.sanjana.gujjar.nycschools.utils.dispatchers

import android.util.Log
import androidx.annotation.VisibleForTesting
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.MainCoroutineDispatcher

/**
 * Provides [CoroutineDispatcher]s and allows alternative implementations to be returned under test.
 *
 * Call [setDispatcher] under test with an alternative implementation of [InternalDispatcherSource].
 *
 * @see kotlin.coroutines.Dispatchers as this class is intended to replace
 */
object NYCDispatcher {

    private const val TAG = "WalmartDispatchers"

    private val DEFAULT = object : InternalDispatcherSource { }

    private var src: InternalDispatcherSource = DEFAULT

    /**
     * Returns a dispatcherSrc that should be used in place of [kotlinx.coroutines.Dispatchers.IO]
     *
     * @see kotlinx.coroutines.Dispatchers.IO
     */
    val IO: CoroutineDispatcher get() = src.io()

    /**
     * Returns a dispatcherSrc that should be used in place of [kotlinx.coroutines.Dispatchers.Main]
     *
     * @see kotlinx.coroutines.Dispatchers.Main
     */
    val Main: MainCoroutineDispatcher get() = src.main()

    /**
     * Returns a dispatcherSrc that should be used in place of [kotlinx.coroutines.Dispatchers.Default]
     *
     * @see kotlinx.coroutines.Dispatchers.Default
     */
    val Default: CoroutineDispatcher get() = src.default()

    /**
     * Returns a dispatcherSrc that should be used in place of [kotlinx.coroutines.Dispatchers.Unconfined]
     *
     * @see kotlinx.coroutines.Dispatchers.Unconfined
     */
    val Unconfined: CoroutineDispatcher get() = src.unconfined()

    /**
     * Sets the [InternalDispatcherSource] which provides CoroutineDispatchers
     *
     * @param replacement Either set a replacement of pass null to revert to the [DEFAULT] implementation of [InternalDispatcherSource]
     */
    @VisibleForTesting(otherwise = VisibleForTesting.NONE)
    fun setDispatcher(replacement: InternalDispatcherSource?) {
        src = replacement ?: DEFAULT
        Log.d(TAG, "setDispatcher($replacement), dispatcherSrc = $src")
    }
}
