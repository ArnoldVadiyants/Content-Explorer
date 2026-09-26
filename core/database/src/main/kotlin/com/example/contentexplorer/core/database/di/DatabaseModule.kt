package com.example.contentexplorer.core.database.di

import android.content.Context
import androidx.room.Room
import com.example.contentexplorer.core.database.ContentDatabase
import com.example.contentexplorer.core.database.dao.ContentDao
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module
class DatabaseModule {
    @Single
    fun provideContentDatabase(context: Context): ContentDatabase =
        Room.databaseBuilder(context, ContentDatabase::class.java, "content_explorer.db").build()

    @Single
    fun provideContentDao(db: ContentDatabase): ContentDao = db.contentDao()
}
