package com.radikal.pcnotifications.exceptions

/**
 * Created by tudor on 26.02.2017.
 */
class ServerDetailsNotFoundException : Exception {
    constructor(message: String?) : super(message)
    constructor(message: String?, cause: Throwable?) : super(message, cause)
    constructor(cause: Throwable?) : super(cause)
}