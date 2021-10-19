package com.sanjana.gujjar.nycschools.nycdir.detailsView.model

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SchoolDetail(
    val dbn: String,
    @SerializedName("school_name")
    @Expose
    val schoolName: String,
    @SerializedName("num_of_sat_test_takers")
    @Expose
    val totalSatTakers: String?,
    @SerializedName("sat_critical_reading_avg_score")
    @Expose
    val satAvgRedersScore: String?,
    @SerializedName("sat_math_avg_score")
    @Expose
    val satMathScore: String?,
    @SerializedName("sat_writing_avg_score")
    @Expose
    val satAvgWritingScore: String?

) : Parcelable