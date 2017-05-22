package com.radikal.pcnotifications.model.domain

import java.io.Serializable

/**
 * Created by tudor on 26.02.2017.
 */
data class ServerDetails(var hostname: String, var ip: String, var port: Int) : Serializable