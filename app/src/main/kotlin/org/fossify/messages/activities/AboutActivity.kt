package org.fossify.messages.activities

import android.os.Bundle
import org.fossify.commons.extensions.updateTextColors
import org.fossify.commons.extensions.viewBinding
import org.fossify.commons.helpers.NavigationIcon
import org.fossify.messages.BuildConfig
import org.fossify.messages.R
import org.fossify.messages.databinding.ActivityAboutBinding

class AboutActivity : SimpleActivity() {

    private val binding by viewBinding(ActivityAboutBinding::inflate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupEdgeToEdge(padBottomImeAndSystem = listOf(binding.aboutScroll))
        setupMaterialScrollListener(binding.aboutScroll, binding.aboutAppbar)
        binding.aboutVersion.text = getString(R.string.about_version, BuildConfig.VERSION_NAME)
        updateTextColors(binding.aboutWrapper)
    }

    override fun onResume() {
        super.onResume()
        setupTopAppBar(binding.aboutAppbar, NavigationIcon.Arrow)
    }
}
