package dev.scavazzini.smslinklock.feature.inbox

import android.content.Context
import android.provider.Telephony
import android.provider.Telephony.Sms.DEFAULT_SORT_ORDER
import android.provider.Telephony.TextBasedSmsColumns.ADDRESS
import android.provider.Telephony.TextBasedSmsColumns.BODY
import android.provider.Telephony.TextBasedSmsColumns.READ
import android.provider.Telephony.TextBasedSmsColumns.THREAD_ID

class GetConversationsUseCase {
    operator fun invoke(context: Context): Map<String, Conversation> {
        val projection = arrayOf(
            THREAD_ID,
            ADDRESS,
            BODY,
            READ,
        )

        val selection = "$THREAD_ID NOT NULL"

        val cursor = context.contentResolver.query(
            /* uri = */ Telephony.Sms.CONTENT_URI,
            /* projection = */ projection,
            /* selection = */ selection,
            /* selectionArgs = */ null,
            /* sortOrder = */ DEFAULT_SORT_ORDER,
        )

        if (cursor == null || !cursor.moveToFirst()) {
            cursor?.close()
            return emptyMap()
        }

        val conversations = mutableMapOf<String, Conversation>()
        val unreadCount = mutableMapOf<String, Int>()

        do {
            val threadIdColumnIndex = cursor.getColumnIndex(THREAD_ID)
            val threadId = cursor.getString(threadIdColumnIndex)

            val readColumnIndex = cursor.getColumnIndex(READ)
            val read = cursor.getInt(readColumnIndex)

            if (read == 0) {
                unreadCount.put(threadId, unreadCount.getOrDefault(threadId, 0) + 1)
            }

            if (conversations.contains(threadId)) {
                continue
            }

            val addressColumnIndex = cursor.getColumnIndex(ADDRESS)
            val bodyColumnIndex = cursor.getColumnIndex(BODY)

            val conversation = Conversation(
                id = threadId,
                address = cursor.getString(addressColumnIndex),
                snippet = cursor.getString(bodyColumnIndex),
                unreadCount = 0,
            )

            conversations.put(threadId, conversation)

        } while (cursor.moveToNext())

        cursor.close()

        return conversations
            .map {
                it.key to it.value.copy(
                    unreadCount = unreadCount.getOrDefault(it.value.id, 0),
                )
            }
            .toMap()
    }
}
