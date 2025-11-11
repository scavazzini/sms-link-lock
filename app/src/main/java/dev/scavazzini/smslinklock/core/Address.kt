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

    fun isEqualTo(anotherAddress: Address): Boolean {
        return phoneNumberUtil.isNumberMatch(
            /* firstNumber = */ this.e164Format,
            /* secondNumber = */ anotherAddress.e164Format,
        ) != PhoneNumberUtil.MatchType.NO_MATCH
    }
}
