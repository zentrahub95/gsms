package org.fossify.messages.activities

import android.os.Bundle
import android.provider.Telephony
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.fossify.commons.extensions.beVisibleIf
import org.fossify.commons.extensions.getProperPrimaryColor
import org.fossify.commons.extensions.getProperTextColor
import org.fossify.commons.extensions.updateTextColors
import org.fossify.commons.extensions.viewBinding
import org.fossify.commons.helpers.NavigationIcon
import org.fossify.commons.helpers.ensureBackgroundThread
import org.fossify.messages.databinding.ActivityKarantinaBinding
import org.fossify.messages.databinding.ItemKarantinaBinding
import org.fossify.messages.extensions.getConversations
import org.fossify.messages.extensions.getThreadId
import org.fossify.messages.extensions.insertNewSMS
import org.fossify.messages.extensions.insertOrUpdateConversation
import org.fossify.messages.extensions.purgeExpiredQuarantine
import org.fossify.messages.extensions.quarantineDB
import org.fossify.messages.helpers.refreshConversations
import org.fossify.messages.models.QuarantinedMessage

class QuarantineActivity : SimpleActivity() {

    private val binding by viewBinding(ActivityKarantinaBinding::inflate)
    private val adapter = QuarantineAdapter(onRestore = ::restore, onDelete = ::delete)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.karantinaList.adapter = adapter
        setupEdgeToEdge(padBottomImeAndSystem = listOf(binding.karantinaList))
        setupMaterialScrollListener(binding.karantinaList, binding.karantinaAppbar)
        updateTextColors(binding.karantinaWrapper)
    }

    override fun onResume() {
        super.onResume()
        setupTopAppBar(binding.karantinaAppbar, NavigationIcon.Arrow)
        load()
    }

    private fun load() {
        ensureBackgroundThread {
            purgeExpiredQuarantine()
            val messages = quarantineDB.getAll()
            runOnUiThread {
                adapter.submit(messages)
                binding.karantinaPlaceholder.beVisibleIf(messages.isEmpty())
            }
        }
    }

    private fun restore(message: QuarantinedMessage) {
        ensureBackgroundThread {
            val threadId = getThreadId(message.address)
            insertNewSMS(
                address = message.address,
                subject = "",
                body = message.body,
                date = message.date,
                read = 0,
                threadId = threadId,
                type = Telephony.Sms.MESSAGE_TYPE_INBOX,
                subscriptionId = -1
            )
            getConversations(threadId).firstOrNull()?.let { runCatching { insertOrUpdateConversation(it) } }
            quarantineDB.delete(message.id)
            refreshConversations()
            load()
        }
    }

    private fun delete(message: QuarantinedMessage) {
        ensureBackgroundThread {
            quarantineDB.delete(message.id)
            load()
        }
    }

    private class QuarantineAdapter(
        private val onRestore: (QuarantinedMessage) -> Unit,
        private val onDelete: (QuarantinedMessage) -> Unit,
    ) : RecyclerView.Adapter<QuarantineAdapter.ViewHolder>() {

        private var items = emptyList<QuarantinedMessage>()

        class ViewHolder(val binding: ItemKarantinaBinding) : RecyclerView.ViewHolder(binding.root)

        fun submit(messages: List<QuarantinedMessage>) {
            items = messages
            notifyDataSetChanged()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            ViewHolder(ItemKarantinaBinding.inflate(LayoutInflater.from(parent.context), parent, false))

        override fun getItemCount() = items.size

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val message = items[position]
            val context = holder.itemView.context
            holder.binding.apply {
                karantinaAddress.text = message.address
                karantinaBody.text = message.body
                karantinaDate.text = DateUtils.formatDateTime(
                    context, message.date, DateUtils.FORMAT_SHOW_DATE or DateUtils.FORMAT_SHOW_TIME
                )
                listOf(karantinaAddress, karantinaDate, karantinaBody).forEach { it.setTextColor(context.getProperTextColor()) }
                listOf(karantinaRestore, karantinaDelete).forEach { it.setTextColor(context.getProperPrimaryColor()) }
                karantinaRestore.setOnClickListener { onRestore(message) }
                karantinaDelete.setOnClickListener { onDelete(message) }
            }
        }
    }
}
