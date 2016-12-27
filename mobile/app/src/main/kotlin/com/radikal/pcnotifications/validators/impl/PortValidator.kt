package com.radikal.pcnotifications.validators.impl

import com.radikal.pcnotifications.validators.Validator
import org.apache.commons.lang3.StringUtils
import javax.inject.Inject


/**
 * Created by tudor on 27.12.2016.
 */
class PortValidator @Inject constructor() : Validator<String?> {
    override fun isValid(obj: String?): Boolean {
        if (obj == null || obj.isBlank() || !StringUtils.isNumeric(obj)){
            return false
        }
        return true
    }
}