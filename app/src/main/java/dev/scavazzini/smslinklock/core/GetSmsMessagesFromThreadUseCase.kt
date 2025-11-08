package dev.scavazzini.smslinklock.core

import android.content.Context
import android.provider.Telephony
import androidx.core.database.getStringOrNull

data class SmsMessage(
    val threadId: String,
    val type: Int,
    val address: String,
    val date: Long,
    val body: String,
    val creator: String,
    val subject: String?,
)

class GetSmsMessagesFromThreadUseCase {
    operator fun invoke(threadId: String, context: Context): List<SmsMessage> {
        val projection = arrayOf(
            Telephony.TextBasedSmsColumns.THREAD_ID,
            Telephony.TextBasedSmsColumns.TYPE,
            Telephony.TextBasedSmsColumns.ADDRESS,
            Telephony.TextBasedSmsColumns.DATE,
            Telephony.TextBasedSmsColumns.BODY,
            Telephony.TextBasedSmsColumns.CREATOR,
            Telephony.TextBasedSmsColumns.SUBJECT,
        )

        val selection = "${Telephony.Sms.THREAD_ID} = ?"

        val cursor = context.contentResolver.query(
            /* uri = */ Telephony.Sms.CONTENT_URI,
            /* projection = */ projection,
            /* selection = */ selection,
            /* selectionArgs = */ arrayOf(threadId),
            /* sortOrder = */ Telephony.Sms.DEFAULT_SORT_ORDER,
        )

        if (cursor == null || !cursor.moveToFirst()) {
            cursor?.close()
            return emptyList()
        }

        val messages = mutableListOf<SmsMessage>()

        do {
            val threadIdColumnIndex =
                cursor.getColumnIndex(Telephony.TextBasedSmsColumns.THREAD_ID)
            val typeColumnIndex =
                cursor.getColumnIndex(Telephony.TextBasedSmsColumns.TYPE)
            val addressColumnIndex =
                cursor.getColumnIndex(Telephony.TextBasedSmsColumns.ADDRESS)
            val dateColumnIndex =
                cursor.getColumnIndex(Telephony.TextBasedSmsColumns.DATE)
            val bodyColumnIndex =
                cursor.getColumnIndex(Telephony.TextBasedSmsColumns.BODY)
            val subjectColumnIndex =
                cursor.getColumnIndex(Telephony.TextBasedSmsColumns.SUBJECT)
            val creatorColumnIndex =
                cursor.getColumnIndex(Telephony.TextBasedSmsColumns.CREATOR)

            val smsMessage = SmsMessage(
                threadId = cursor.getString(threadIdColumnIndex),
                type = cursor.getInt(typeColumnIndex),
                address = cursor.getString(addressColumnIndex),
                date = cursor.getLong(dateColumnIndex),
                body = cursor.getString(bodyColumnIndex),
                creator = cursor.getString(creatorColumnIndex),
                subject = cursor.getStringOrNull(subjectColumnIndex),
            )

            messages.add(smsMessage)

        } while (cursor.moveToNext())

        cursor.close()
        return messages
    }
}
