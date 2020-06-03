package com.bungabear.databurnner.network

import com.bungabear.databurnner.Config
import retrofit2.Retrofit

object FileDownloadRetrofit{
    private val retrofit by lazy { Retrofit.Builder().baseUrl(Config.BurnUrl).build() }
    private val service by lazy { retrofit.create(FileDownloadInterface::class.java)}
    operator fun invoke() = service
}