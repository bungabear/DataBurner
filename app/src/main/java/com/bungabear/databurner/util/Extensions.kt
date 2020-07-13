package com.bungabear.databurner.util

fun Double.cut(offset: Int) : String {
    val formatStr = StringBuilder("%")
    if (offset > 0) {
        formatStr.append(".$offset")
    }
    formatStr.append("f")
    return formatStr.toString().format(this)
}