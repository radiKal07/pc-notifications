package com.radikal.pcnotifications.validators

/**
 * Created by tudor on 27.12.2016.
 */
interface Validator<in T> {
    fun isValid(obj: T): Boolean
}