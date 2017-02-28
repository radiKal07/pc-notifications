package com.radikal.pcnotifications.model.service

/**
 * Created by tudor on 28.02.2017.
 */
interface DataSerializer {
    fun <T> serialize(item: T): String
    fun <T> deserialize(item: String, clazz: Class<T>): T
}