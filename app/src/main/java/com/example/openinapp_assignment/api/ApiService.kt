package com.example.openinapp_assignment.api

import com.example.openinapp_assignment.model.MainDataClass
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header

interface ApiService {
    @GET("api/v1/dashboardNew")
    fun getData(@Header("Authorization") token: String): Call<MainDataClass>
}