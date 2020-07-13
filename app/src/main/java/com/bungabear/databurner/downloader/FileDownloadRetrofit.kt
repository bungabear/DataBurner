package com.bungabear.databurner.downloader

import com.bungabear.databurner.util.Config
import retrofit2.Retrofit

object FileDownloadRetrofit{
    private val retrofit by lazy { Retrofit.Builder().baseUrl(Config.BurnUrl).build() }
    private val service by lazy { retrofit.create(FileDownloadInterface::class.java)}
    operator fun invoke() = service
}