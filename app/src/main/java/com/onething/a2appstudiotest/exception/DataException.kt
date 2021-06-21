package com.onething.a2appstudiotest.exception

import java.lang.RuntimeException


sealed class DataException : RuntimeException {

    constructor() : super()

    constructor(msg: String) : super(msg)

    object Network: DataException()

    object Conversion: DataException()

}