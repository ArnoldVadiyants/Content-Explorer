package com.example.contentexplorer.core.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "content_items",
    foreignKeys = [
        ForeignKey(
            entity = PageEntity::class,
            parentColumns = ["id"],
            childColumns = ["pageId"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
    indices = [Index("pageId"), Index("parentId")],
)
data class ContentItemEntity(
    @PrimaryKey val id: Long,
    val type: String,
    val title: String?,
    val content: String?,
    val imageUrl: String?,
    val parentId: Long?,
    val pageId: Long,
)
