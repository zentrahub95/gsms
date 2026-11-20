package org.fossify.messages.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "quarantined_messages")
data class QuarantinedMessage(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val id: Long = 0,
    @ColumnInfo(name = "address") val address: String,
    @ColumnInfo(name = "body") val body: String,
    @ColumnInfo(name = "date") val date: Long,
)
