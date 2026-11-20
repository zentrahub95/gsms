package org.fossify.messages.interfaces

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import org.fossify.messages.models.QuarantinedMessage

@Dao
interface QuarantineDao {
    @Insert
    fun insert(message: QuarantinedMessage): Long

    @Query("SELECT * FROM quarantined_messages ORDER BY date DESC")
    fun getAll(): List<QuarantinedMessage>

    @Query("DELETE FROM quarantined_messages WHERE id = :id")
    fun delete(id: Long)

    @Query("DELETE FROM quarantined_messages WHERE date < :minDate")
    fun deleteOlderThan(minDate: Long)
}
