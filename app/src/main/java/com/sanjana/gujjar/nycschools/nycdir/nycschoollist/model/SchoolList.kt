package com.sanjana.gujjar.nycschools.nycdir.nycschoollist.model

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


@Parcelize
data class School(
    val dbn: String,
    @SerializedName("school_name")
    @Expose
    val schoolName: String,
    @SerializedName("phone_number")
    @Expose
    val phoneNumber: String?,
    @SerializedName("overview_paragraph")
    @Expose
    val overviewParagraph: String?,
    val location: String?,
    @SerializedName("school_email")
    @Expose
    val emailAddress: String?,
    val website: String?,
    @SerializedName("fax_number")
    @Expose
    val faxNumber: String?
) : Parcelable