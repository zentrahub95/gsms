package org.fossify.messages.helpers

import org.fossify.messages.helpers.PromoFilter.Verdict
import org.junit.Assert.assertEquals
import org.junit.Test

class PromoFilterTest {

    private val promoSenders = setOf("STARBUCKS", "777")

    private fun classify(address: String, body: String) = PromoFilter.classify(address, body, promoSenders)

    @Test
    fun otpPasses() {
        assertEquals(Verdict.PASS, classify("CHATIME", "Kode OTP Anda 123456. Jangan berikan kepada siapapun."))
    }

    @Test
    fun alphanumericPromoIsQuarantined() {
        assertEquals(Verdict.QUARANTINE, classify("GENKI SUSHI", "Diskon 50% semua menu, cek https://bit.ly/abc"))
    }

    @Test
    fun ordinaryNumberPasses() {
        assertEquals(Verdict.PASS, classify("+628123456789", "Gratis ongkir kalau kamu mau mampir, promo katanya"))
    }

    @Test
    fun promoCodeIsKeptByWhitelist() {
        assertEquals(Verdict.PASS, classify("CHATIME", "Gratis 1 cup! Kode promo: ABC123"))
    }

    @Test
    fun listedSenderIsQuarantinedWithoutKeyword() {
        assertEquals(Verdict.QUARANTINE, classify("starbucks", "Kami tunggu kedatanganmu minggu ini"))
        assertEquals(Verdict.QUARANTINE, classify("777", "Selamat bergabung"))
    }

    @Test
    fun alphanumericWithoutPromoKeywordPasses() {
        assertEquals(Verdict.PASS, classify("BCA", "Saldo Anda Rp1.000.000"))
    }

    @Test
    fun pinMatchesWholeWordOnly() {
        assertEquals(Verdict.QUARANTINE, classify("CHATIME", "Promo shopping hari ini, gratis ongkir"))
        assertEquals(Verdict.PASS, classify("CHATIME", "Promo PIN baru Anda 1234"))
    }
}
