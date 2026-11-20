package org.fossify.messages

import android.app.LocaleManager
import android.database.ContentObserver
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.os.LocaleList
import android.provider.ContactsContract
import org.fossify.commons.FossifyApp
import org.fossify.commons.extensions.hasPermission
import org.fossify.commons.helpers.PERMISSION_READ_CONTACTS
import org.fossify.commons.helpers.isTiramisuPlus
import org.fossify.commons.helpers.letterBackgroundColors
import org.fossify.commons.helpers.ensureBackgroundThread
import org.fossify.messages.extensions.config
import org.fossify.messages.extensions.rescheduleAllScheduledMessages
import org.fossify.messages.helpers.MessagingCache

class App : FossifyApp() {
    override val isAppLockFeatureAvailable = true

    override fun onCreate() {
        super.onCreate()
        applyBluePalette()
        applyDefaultLanguage()
        if (hasPermission(PERMISSION_READ_CONTACTS)) {
            listOf(
                ContactsContract.Contacts.CONTENT_URI,
                ContactsContract.Data.CONTENT_URI,
                ContactsContract.DisplayPhoto.CONTENT_URI
            ).forEach {
                try {
                    contentResolver.registerContentObserver(it, true, contactsObserver)
                } catch (_: Exception) {
                }
            }
        }

        ensureBackgroundThread {
            rescheduleAllScheduledMessages()
        }
    }

    private fun applyBluePalette() {
        letterBackgroundColors.clear()
        letterBackgroundColors.addAll(
            listOf(0xFF2563EB, 0xFF1D4ED8, 0xFF0E7490, 0xFF1E40AF, 0xFF0369A1, 0xFF1E3A8A)
        )
    }

    private fun applyDefaultLanguage() {
        if (!isTiramisuPlus() || config.defaultLanguageApplied) return
        getSystemService(LocaleManager::class.java).applicationLocales = LocaleList.forLanguageTags("id")
        config.defaultLanguageApplied = true
    }

    private val contactsObserver = object : ContentObserver(Handler(Looper.getMainLooper())) {
        override fun onChange(selfChange: Boolean, uri: Uri?) {
            MessagingCache.namePhoto.evictAll()
            MessagingCache.participantsCache.evictAll()
        }
    }
}
