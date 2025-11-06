package dev.scavazzini.smslinklock.core

import android.content.Context
import android.provider.Telephony
import androidx.core.database.getStringOrNull

data class SmsMessage(
    val threadId: String,
    val address: String,
    val date: Long,
    val body: String,
    val creator: String,
    val subject: String?,
)

class GetSmsMessagesUseCase {
    operator fun invoke(context: Context): Map<String, List<SmsMessage>> {
        val projection = arrayOf(
            Telephony.TextBasedSmsColumns.THREAD_ID,
            Telephony.TextBasedSmsColumns.ADDRESS,
            Telephony.TextBasedSmsColumns.DATE,
            Telephony.TextBasedSmsColumns.BODY,
            Telephony.TextBasedSmsColumns.CREATOR,
            Telephony.TextBasedSmsColumns.SUBJECT,
        )

        val selection = "${Telephony.Sms.THREAD_ID} NOT NULL"

        val cursor = context.contentResolver.query(
            /* uri = */ Telephony.Sms.CONTENT_URI,
            /* projection = */ projection,
            /* selection = */ selection,
            /* selectionArgs = */ null,
            /* sortOrder = */ Telephony.Sms.DEFAULT_SORT_ORDER,
        )

        if (cursor == null || !cursor.moveToFirst()) {
            cursor?.close()
            return emptyMap()
        }

        val messages = mutableMapOf<String, MutableList<SmsMessage>>()

        do {
            val threadIdColumnIndex =
                cursor.getColumnIndex(Telephony.TextBasedSmsColumns.THREAD_ID)
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
                address = cursor.getString(addressColumnIndex),
                date = cursor.getLong(dateColumnIndex),
                body = cursor.getString(bodyColumnIndex),
                creator = cursor.getString(creatorColumnIndex),
                subject = cursor.getStringOrNull(subjectColumnIndex),
            )

            val updatedMessages = messages.getOrDefault(
                key = smsMessage.threadId,
                defaultValue = mutableListOf(),
            ).apply { add(0, smsMessage) }

            messages.put(smsMessage.threadId, updatedMessages)

        } while (cursor.moveToNext())

        cursor.close()
        return messages
    }
}
