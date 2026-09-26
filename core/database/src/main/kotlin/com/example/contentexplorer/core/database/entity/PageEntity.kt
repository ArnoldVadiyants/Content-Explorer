package com.example.contentexplorer.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pages")
data class PageEntity(
    @PrimaryKey val id: Long,
    val title: String,
)
