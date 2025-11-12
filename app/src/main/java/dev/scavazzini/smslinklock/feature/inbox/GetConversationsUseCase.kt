package dev.scavazzini.smslinklock.feature.inbox

import android.content.Context
import android.provider.Telephony
import android.provider.Telephony.Sms.DEFAULT_SORT_ORDER
import android.provider.Telephony.TextBasedSmsColumns.ADDRESS
import android.provider.Telephony.TextBasedSmsColumns.BODY
import android.provider.Telephony.TextBasedSmsColumns.DATE
import android.provider.Telephony.TextBasedSmsColumns.READ
import android.provider.Telephony.TextBasedSmsColumns.THREAD_ID
import dev.scavazzini.smslinklock.core.Address
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

class GetConversationsUseCase {
    operator fun invoke(context: Context): List<Conversation> {
        val projection = arrayOf(
            THREAD_ID,
            ADDRESS,
            BODY,
            READ,
            DATE,
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
            return emptyList()
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
            val dateColumnIndex = cursor.getColumnIndex(DATE)

            val conversation = Conversation(
                id = threadId,
                address = Address(cursor.getString(addressColumnIndex)),
                snippet = cursor.getString(bodyColumnIndex),
                unreadCount = 0,
                lastMessageDate = LocalDateTime.ofInstant(
                    /* instant = */ Instant.ofEpochMilli(cursor.getLong(dateColumnIndex)),
                    /* zone = */ ZoneId.systemDefault(),
                ),
            )

            conversations.put(threadId, conversation)

        } while (cursor.moveToNext())

        cursor.close()

        return conversations.values.map { conversation ->
            conversation.copy(
                unreadCount = unreadCount.getOrDefault(conversation.id, 0),
            )
        }
    }
}
