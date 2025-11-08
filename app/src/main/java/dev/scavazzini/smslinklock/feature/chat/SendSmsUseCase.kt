package dev.scavazzini.smslinklock.feature.chat

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.telephony.SmsManager
import java.util.ArrayList
import kotlin.random.Random

class SendSmsUseCase(private val smsManager: SmsManager) {
    companion object {
        const val INTENT_SENT_ACTION = "SMS_SENT_ACTION"
        const val MESSAGE_EXTRA = "message"
    }

    operator fun invoke(
        message: String,
        address: String,
        context: Context,
    ) {
        val pendingIntent = createPendingIntent(message, context)
        val messageFragments = smsManager.divideMessage(message)

        if (messageFragments.size <= 1) {
            return sendSms(address, message, pendingIntent)
        }

        return sendFragmentedSms(address, messageFragments, pendingIntent)
    }

    private fun createPendingIntent(message: String, context: Context): PendingIntent {
        val sentIntent = Intent(INTENT_SENT_ACTION).apply {
            putExtra(MESSAGE_EXTRA, message)
        }

        return PendingIntent.getBroadcast(
            /* context = */ context,
            /* requestCode = */ Random.nextInt(),
            /* intent = */ sentIntent,
            /* flags = */ PendingIntent.FLAG_IMMUTABLE,
        )
    }

    private fun sendSms(
        address: String,
        message: String,
        pendingIntent: PendingIntent,
    ) {
        smsManager.sendTextMessage(
            /* destinationAddress = */ address,
            /* scAddress = */ null,
            /* text = */ message,
            /* sentIntent = */ pendingIntent,
            /* deliveryIntent = */ null,
        )
    }

    private fun sendFragmentedSms(
        address: String,
        messageFragments: ArrayList<String>,
        pendingIntent: PendingIntent,
    ) {
        val intents = ArrayList<PendingIntent>(
            MutableList(messageFragments.size - 1, { null })
        ).apply { add(0, pendingIntent) }

        smsManager.sendMultipartTextMessage(
            /* destinationAddress = */ address,
            /* scAddress = */ null,
            /* parts = */ messageFragments,
            /* sentIntents = */ intents,
            /* deliveryIntents = */ null,
        )
    }
}
