package com.sanjana.gujjar.nycschools.base.android.components.lifecycle

/**
 * Interface, intended to by implemented by delegation to [Tag], that tags a class with a human readable name.
 */
interface Tagged {
    val TAG: String
}

/**
 * See [Tagged]
 */
class Tag(override val TAG: String) : Tagged