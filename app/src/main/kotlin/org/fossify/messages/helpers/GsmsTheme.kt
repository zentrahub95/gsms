package org.fossify.messages.helpers

import android.content.Context
import android.content.res.Configuration
import android.graphics.Color
import org.fossify.commons.extensions.baseConfig
import org.fossify.messages.extensions.config

object GsmsTheme {
    private val accent = Color.parseColor("#2563EB")

    fun apply(context: Context) {
        val isDark = when (context.config.themeMode) {
            THEME_MODE_DARK -> true
            THEME_MODE_LIGHT -> false
            else -> context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK ==
                Configuration.UI_MODE_NIGHT_YES
        }

        context.baseConfig.apply {
            isSystemThemeEnabled = false
            isGlobalThemeEnabled = false
            textColor = Color.parseColor(if (isDark) "#F0F4FF" else "#111827")
            backgroundColor = Color.parseColor(if (isDark) "#060B14" else "#EEF1F7")
            primaryColor = accent
            accentColor = accent
        }
    }
}
