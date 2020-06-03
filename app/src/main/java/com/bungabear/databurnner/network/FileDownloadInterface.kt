package com.bungabear.databurnner.network

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface FileDownloadInterface{
    @GET("dummy")
    fun download(@Query("size") size: Long) : Call<ResponseBody>
}