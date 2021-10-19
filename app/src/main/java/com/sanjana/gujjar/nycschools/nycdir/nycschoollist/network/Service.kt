package com.sanjana.gujjar.nycschools.nycdir.nycschoollist.network

import androidx.annotation.VisibleForTesting
import com.sanjana.gujjar.nycschools.nycdir.detailsView.model.SchoolDetail
import com.sanjana.gujjar.nycschools.nycdir.nycschoollist.model.School
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit


interface Service {
    @GET("s3k6-pzi2.json")
    fun getSchoolList(): Call<List<School>>

    @GET("f9bf-2cp4.json")
    fun getSchoolDetails(): Call<List<SchoolDetail>>

    companion object {

        private var retrofitService: Service? = null
        private var BASE_URL = "https://data.cityofnewyork.us/resource/"

        @VisibleForTesting
        fun getInstance() : Service {

            if (retrofitService == null) {
                val okHttpClient = OkHttpClient.Builder()
                    .readTimeout(60, TimeUnit.SECONDS)
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .build()
                val retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient)
                    .build()
                retrofitService = retrofit.create(Service::class.java)
            }
            return retrofitService!!
        }
    }
}