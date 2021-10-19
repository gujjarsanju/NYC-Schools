package com.sanjana.gujjar.nycschools.utils

import android.app.Activity
import android.content.DialogInterface
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.sanjana.gujjar.nycschools.R

object DialogUtils {
    data class DialogAction(val text: String, val listener: DialogInterface.OnClickListener? = null)

    fun showDialog(
        activity: Activity,
        title: String? = null,
        message: String,
        isCancelable: Boolean = true,
        listener: DialogInterface.OnClickListener
    ) {
        showDialog(
            activity,
            title,
            message,
            isCancelable,
            DialogAction(activity.getString(R.string.common_ok), listener)
        )
    }

    fun showDialog(
        activity: Activity,
        title: String? = null,
        message: String,
        isCancelable: Boolean = true,
        positiveAction: DialogAction? = null,
        negativeAction: DialogAction? = null,
        dismissListener: DialogInterface.OnDismissListener? = null,
        view: View? = null
    ) {
        val builder: AlertDialog.Builder =
            AlertDialog.Builder(activity).apply {
                title?.let {
                    if (it.isNotBlank()) {
                        this.setTitle(it)
                    }
                }
                setMessage(message)
                setCancelable(isCancelable)
                positiveAction?.let {
                    this.setPositiveButton(it.text, it.listener)
                }
                negativeAction?.let {
                    this.setNegativeButton(it.text, it.listener)
                }
                dismissListener?.let { this.setOnDismissListener(it) }
                view?.let { setView(view) }
            }
        val alertDialog = builder.create()
        alertDialog.show()
    }
}