package com.radikal.pcnotifications.model.service.impl

import com.fasterxml.jackson.databind.ObjectMapper
import com.radikal.pcnotifications.model.service.DataSerializer
import javax.inject.Inject

/**
 * Created by tudor on 28.02.2017.
 */
class JSONDataSerializer @Inject constructor(var objectMapper: ObjectMapper) : DataSerializer {

    override fun <T> serialize(item: T): String {
        return objectMapper.writeValueAsString(item)
    }

    override fun <T> deserialize(item: String, clazz: Class<T>): T {
        return objectMapper.readValue(item, clazz)
    }
}