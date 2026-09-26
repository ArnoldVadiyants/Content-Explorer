package com.example.contentexplorer.core.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "response_sets",
    foreignKeys = [
        ForeignKey(
            entity = ContentItemEntity::class,
            parentColumns = ["id"],
            childColumns = ["questionId"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
    indices = [Index("questionId")],
)
data class ResponseSetEntity(
    @PrimaryKey val id: Long,
    val questionId: Long,
    val multipleSelection: Boolean,
)
