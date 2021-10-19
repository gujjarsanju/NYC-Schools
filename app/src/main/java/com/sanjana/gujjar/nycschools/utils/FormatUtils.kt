package com.sanjana.gujjar.nycschools.utils

import android.telephony.PhoneNumberUtils
import java.util.*

class FormatUtils {
    companion object {
        internal fun formatNumber(value: String?): String? {
            return if (value == null) {
                ""
            } else PhoneNumberUtils.formatNumber(value, Locale.US.country)
        }
    }
}