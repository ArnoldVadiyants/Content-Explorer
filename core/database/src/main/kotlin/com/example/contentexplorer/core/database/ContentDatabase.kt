package com.example.contentexplorer.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.contentexplorer.core.database.dao.ContentDao
import com.example.contentexplorer.core.database.entity.ContentItemEntity
import com.example.contentexplorer.core.database.entity.PageEntity
import com.example.contentexplorer.core.database.entity.ResponseEntity
import com.example.contentexplorer.core.database.entity.ResponseSetEntity

@Database(
    entities = [
        PageEntity::class,
        ContentItemEntity::class,
        ResponseSetEntity::class,
        ResponseEntity::class,
    ],
    version = 1,
    exportSchema = true,
)
abstract class ContentDatabase : RoomDatabase() {
    abstract fun contentDao(): ContentDao
}
