package com.sanjana.gujjar.nycschools.utils

import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.navigation.fragment.findNavController
import com.sanjana.gujjar.nycschools.R

fun View.shouldShow(show: Boolean, beInvisible: Boolean = false) {
    visibility = when (show) {
        true -> View.VISIBLE
        false -> when (beInvisible) {
            true -> View.INVISIBLE
            else -> View.GONE
        }
    }
}

const val VIEW_MODEL_KEY: String = "view_model_contract_key"

fun <T> registerSharedViewModel(clazz: Class<T>): Bundle = Bundle().apply {
    putSerializable(
        VIEW_MODEL_KEY, clazz
    )
}

fun <T> registerSharedViewModel(bundle: Bundle): Bundle = Bundle().apply {
    bundle.getSerializable(VIEW_MODEL_KEY)?.let {
        putSerializable(VIEW_MODEL_KEY, it as Class<T>)
    } ?: throw IllegalArgumentException("VIEW_MODEL_KEY not present")
}

inline fun <T> getSharedViewModel(bundle: Bundle): Class<T> = bundle.getSerializable(VIEW_MODEL_KEY) as Class<T>

fun <T> Fragment.viewModelContract(ownerProducer: () -> ViewModelStoreOwner = { requireActivity() }) = lazy {
    val viewModelClass: Class<ViewModel> =
        arguments?.getSerializable(VIEW_MODEL_KEY) as Class<ViewModel>
    ViewModelProvider(ownerProducer()).get(viewModelClass) as T
}

fun Fragment.hasNavController() = try {
    findNavController()
    true
} catch (e: IllegalStateException) {
    false
}

fun TextView.colorSpannableStringWithUnderLineOne(
    prefixString: String,
    postfixString: String,
    callback: (Int) -> Unit
) {
    val spanTxt = SpannableStringBuilder()
    spanTxt.append("$prefixString ")
    spanTxt.append(" $postfixString")
    spanTxt.setSpan(object : ClickableSpan() {
        override fun onClick(widget: View) {
            callback(0)
            widget.invalidate()
        }

        override fun updateDrawState(ds: TextPaint) {
            ds.color = ContextCompat.getColor(context, R.color.highlight)
            ds.isUnderlineText = true
        }
    }, prefixString.length, spanTxt.length, 0)
    this.movementMethod = LinkMovementMethod.getInstance()
    this.setText(spanTxt, TextView.BufferType.SPANNABLE)
}

