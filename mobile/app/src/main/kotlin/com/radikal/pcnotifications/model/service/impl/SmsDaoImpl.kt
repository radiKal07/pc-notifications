package com.radikal.pcnotifications.model.service.impl

import android.content.ContentResolver
import android.net.Uri
import com.radikal.pcnotifications.model.domain.Sms
import com.radikal.pcnotifications.model.service.SmsDao
import java.util.*
import javax.inject.Inject

/**
 * Created by tudor on 25.04.2017.
 */
class SmsDaoImpl @Inject constructor() : SmsDao {
    val smsUri: Uri = Uri.parse("content://sms/inbox")

    @Inject
    lateinit var contentResolver: ContentResolver

    override fun getAll(): List<Sms> {
        var smsList: MutableList<Sms> = ArrayList()
        val cursor = contentResolver.query(smsUri, null, null, null, null)

        while (cursor.moveToNext()) {
            var sender = cursor.getString(cursor.getColumnIndex("address"))
            var message = cursor.getString(cursor.getColumnIndex("body"))
            smsList.add(Sms(sender, message))
        }

        cursor.close()

        return smsList
    }
}