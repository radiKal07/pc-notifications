package com.radikal.pcnotifications.model.domain

import java.util.*

/**
 * Created by tudor on 21.02.2017.
 */
data class Sms(val address: String, val message: String, val date: Date, val type: Int)