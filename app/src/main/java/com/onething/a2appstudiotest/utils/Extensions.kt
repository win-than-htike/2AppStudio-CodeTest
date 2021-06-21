package com.onething.a2appstudiotest.utils

fun String.removeLastSlash() : String {
    return if (this.endsWith("/")) {
        this.dropLast(1)
    } else {
        this
    }
}

fun String.removeFirstSlash() : String {
    return if (this.startsWith("/")) {
        this.drop(1)
    } else {
        this
    }
}