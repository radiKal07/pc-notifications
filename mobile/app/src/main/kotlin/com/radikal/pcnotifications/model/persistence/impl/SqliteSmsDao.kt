package com.radikal.pcnotifications.model.persistence.impl

import android.content.ContentResolver
import android.net.Uri
import com.radikal.pcnotifications.model.domain.Sms
import com.radikal.pcnotifications.model.persistence.SmsDao
import com.radikal.pcnotifications.utils.RECEIVED
import com.radikal.pcnotifications.utils.SENT
import java.util.*
import javax.inject.Inject

/**
 * Created by tudor on 25.04.2017.
 */
class SqliteSmsDao @Inject constructor() : SmsDao {
    val smsInboxUri: Uri = Uri.parse("content://sms/inbox")
    val smsSentUri: Uri = Uri.parse("content://sms/sent")

    @Inject
    lateinit var contentResolver: ContentResolver

    override fun getAllReceived(): List<Sms> {
        return getSms(smsInboxUri)
    }

    override fun getAllSent(): List<Sms> {
        return getSms(smsSentUri)
    }

    private fun getSms(uri: Uri): MutableList<Sms> {
        var smsType: Int
        if (uri == smsInboxUri) {
            smsType = RECEIVED
        } else if (uri == smsSentUri) {
            smsType = SENT
        } else {
            throw IllegalArgumentException("Provided URI is invalid")
        }

        var smsList: MutableList<Sms> = ArrayList()
        val cursor = contentResolver.query(uri, null, null, null, null)

        while (cursor.moveToNext()) {
            var sender = cursor.getString(cursor.getColumnIndex("address"))
            var message = cursor.getString(cursor.getColumnIndex("body"))
            var date = cursor.getString(cursor.getColumnIndex("date"))
            smsList.add(Sms(sender, message, Date(date.toLong()), smsType))
        }

        cursor.close()

        return smsList
    }
}