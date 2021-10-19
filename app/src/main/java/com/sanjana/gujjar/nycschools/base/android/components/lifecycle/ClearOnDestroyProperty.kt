package com.sanjana.gujjar.nycschools.base.android.components.lifecycle

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * Property with nullable backing field that is cleared when the provided [Lifecycle]s [onDestroy] event is triggered.
 *
 * Great for managing your viewBinding object which should be cleared in onDestroyView (viewLifecycle.onDestroy event), e.g.:
 *     class FooFragment: BaseFragment("FooFragment") {
 *         private var binding by ClearOnDestroyProperty<FooFragmentBinding> { viewLifecycleOwner.lifecycle }
 *
 *         override fun onCreateView(inflater: Inflater, container: ViewGroup?, savedInstanceState:Bundle?):View? {
 *             binding = FooFragmentBinding(inflater, container, false)
 *             return binding.root
 *         }
 *     }
 */
class ClearOnDestroyProperty<T>(
    private val lifecycleProvider: () -> Lifecycle
) : ReadWriteProperty<Any, T>, DefaultLifecycleObserver {

    @VisibleForTesting
    internal var field: T? = null

    override fun getValue(thisRef: Any, property: KProperty<*>) =
        field ?: throw UninitializedPropertyAccessException("Field not set")

    override fun setValue(thisRef: Any, property: KProperty<*>, value: T) {
        field = value
        lifecycleProvider().addObserver(this)
    }

    override fun onDestroy(owner: LifecycleOwner) {
        field = null
    }
}
