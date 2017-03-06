package com.radikal.pcnotifications.model.service

import com.radikal.pcnotifications.model.domain.ServerDetails

/**
 * Created by tudor on 26.02.2017.
 */
interface ServerDetailsDao {
    fun save(serverDetails: ServerDetails)
    fun retrieve(): ServerDetails
    fun delete()
}