package com.radikal.pcnotifications.exceptions

/**
 * Created by tudor on 21.02.2017.
 */
class DeviceNotConnectedException : Exception {
    constructor() : super()
    constructor(message: String?) : super(message)
    constructor(message: String?, cause: Throwable?) : super(message, cause)
}