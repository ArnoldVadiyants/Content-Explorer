package com.example.contentexplorer.core.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "responses",
    foreignKeys = [
        ForeignKey(
            entity = ResponseSetEntity::class,
            parentColumns = ["id"],
            childColumns = ["responseSetId"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
    indices = [Index("responseSetId")],
)
data class ResponseEntity(
    @PrimaryKey val id: Long,
    val responseSetId: Long,
    val label: String,
    val score: Int? = null,
)
