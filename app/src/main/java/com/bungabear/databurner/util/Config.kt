package com.bungabear.databurner.util

import androidx.lifecycle.MutableLiveData

object Config {
    const val BurnUrl = "https://dummyfileserver.herokuapp.com/"

    val DataUsage = MutableLiveData<Long>()
    val WifiStatus = MutableLiveData<Int>()
}