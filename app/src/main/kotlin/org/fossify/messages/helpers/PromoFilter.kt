package org.fossify.messages.helpers

import android.content.Context
import kotlinx.serialization.json.Json

object PromoFilter {

    enum class Verdict { PASS, QUARANTINE }

    private val whitelist = Regex(
        "kode|verifikasi|jangan berikan|tagihan|transaksi|\\b(otp|pin|password)\\b",
        RegexOption.IGNORE_CASE
    )

    private val promoKeywords = Regex(
        "gratis|promo|diskon|undian|hadiah|cashback|kupon|beli 1|s&k|tukar|https?://|bit\\.ly",
        RegexOption.IGNORE_CASE
    )

    private var promoSenders: Set<String>? = null

    fun classify(address: String, body: String, promoSenders: Set<String>): Verdict {
        if (whitelist.containsMatchIn(body)) return Verdict.PASS

        val sender = address.trim().uppercase()
        if (sender in promoSenders) return Verdict.QUARANTINE

        val isAlphanumericSender = sender.any { it.isLetter() }
        return if (isAlphanumericSender && promoKeywords.containsMatchIn(body)) {
            Verdict.QUARANTINE
        } else {
            Verdict.PASS
        }
    }

    fun classify(context: Context, address: String, body: String) =
        classify(address, body, loadPromoSenders(context))

    private fun loadPromoSenders(context: Context): Set<String> {
        promoSenders?.let { return it }
        val json = context.assets.open("promo_senders.json").bufferedReader().use { it.readText() }
        return Json.decodeFromString<List<String>>(json).map { it.uppercase() }.toSet()
            .also { promoSenders = it }
    }
}
