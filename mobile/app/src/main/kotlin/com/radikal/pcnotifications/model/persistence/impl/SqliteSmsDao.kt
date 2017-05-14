package com.radikal.pcnotifications.model.persistence.impl

import android.content.ContentResolver
import android.net.Uri
import android.telephony.TelephonyManager
import android.util.Log
import com.radikal.pcnotifications.model.domain.Sms
import com.radikal.pcnotifications.model.persistence.SmsDao
import com.radikal.pcnotifications.utils.RECEIVED
import com.radikal.pcnotifications.utils.SENT
import java.util.*
import javax.inject.Inject
import com.github.tamir7.contacts.Contact
import com.github.tamir7.contacts.Contacts


/**
 * Created by tudor on 25.04.2017.
 */
class SqliteSmsDao @Inject constructor() : SmsDao {
    val TAG: String = javaClass.simpleName

    val smsInboxUri: Uri = Uri.parse("content://sms/inbox")
    val smsSentUri: Uri = Uri.parse("content://sms/sent")

    @Inject
    lateinit var contentResolver: ContentResolver

    @Inject
    lateinit var telephonyManager: TelephonyManager

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

        val allContacts = Contacts.getQuery().find()

        while (cursor.moveToNext()) {
            var sender = cursor.getString(cursor.getColumnIndex("address")).replace(" ", "")

            val contactsByPhoneNumber = Contacts.getQuery()
            contactsByPhoneNumber.whereEqualTo(Contact.Field.PhoneNumber, sender)
            val contacts = contactsByPhoneNumber.find()

            var displayName = sender
            if (contacts.size == 0) {
                Log.e(TAG, "Contact not found for number: " + sender)
                allContacts.forEach {
                    val contact = it
                    it.phoneNumbers.forEach {
                        if (it.normalizedNumber?.replace(" ", "") == sender || it.number?.replace(" ", "") == sender) {
                            Log.v(TAG, "Found contact for number $sender and display name: ${contact.displayName}")
                            displayName = contact.displayName
                        }
                    }
                }
            } else {
                displayName = contacts[0].displayName
            }

            var message = cursor.getString(cursor.getColumnIndex("body"))
            var date = cursor.getString(cursor.getColumnIndex("date"))
            smsList.add(Sms(com.radikal.pcnotifications.model.domain.Contact(displayName, sender), message, Date(date.toLong()), smsType))
        }

        cursor.close()

        return smsList
    }
}