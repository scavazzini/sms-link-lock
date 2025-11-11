package dev.scavazzini.smslinklock.core

import com.google.i18n.phonenumbers.PhoneNumberUtil
import java.util.Locale

class Address(
    rawAddress: String,
    region: String = Locale.getDefault().country,
) {
    private val phoneNumberUtil = PhoneNumberUtil.getInstance()

    private val address = phoneNumberUtil.parse(
        /* numberToParse = */ rawAddress,
        /* defaultRegion = */ region,
    )

    val internationalFormat: String
        get() = formattedIn(PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL)

    val e164Format: String
        get() = formattedIn(PhoneNumberUtil.PhoneNumberFormat.E164)

    private fun formattedIn(format: PhoneNumberUtil.PhoneNumberFormat): String {
        return phoneNumberUtil.format(
            /* number = */ address,
            /* numberFormat = */ format,
        )
    }
}
