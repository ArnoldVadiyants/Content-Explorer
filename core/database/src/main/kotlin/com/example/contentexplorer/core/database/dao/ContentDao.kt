package com.example.contentexplorer.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.contentexplorer.core.database.entity.ContentItemEntity
import com.example.contentexplorer.core.database.entity.PageEntity
import com.example.contentexplorer.core.database.entity.ResponseEntity
import com.example.contentexplorer.core.database.entity.ResponseSetEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for local database operations.
 */
@Dao
interface ContentDao {
    @Query("SELECT * FROM pages ORDER BY id ASC")
    fun observePages(): Flow<List<PageEntity>>

    @Query("SELECT * FROM content_items WHERE pageId = :pageId ORDER BY id ASC")
    suspend fun getItemsForPage(pageId: Long): List<ContentItemEntity>

    @Query("SELECT * FROM content_items WHERE id = :id")
    suspend fun getItemById(id: Long): ContentItemEntity?

    @Query("SELECT * FROM response_sets WHERE questionId = :questionId")
    suspend fun getResponseSet(questionId: Long): ResponseSetEntity?

    @Query("SELECT * FROM responses WHERE responseSetId = :responseSetId ORDER BY id ASC")
    suspend fun getResponses(responseSetId: Long): List<ResponseEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPages(pages: List<PageEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItems(items: List<ContentItemEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertResponseSets(responseSets: List<ResponseSetEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertResponses(responses: List<ResponseEntity>)

    @Query("DELETE FROM responses")
    suspend fun deleteAllResponses()

    @Query("DELETE FROM response_sets")
    suspend fun deleteAllResponseSets()

    @Query("DELETE FROM content_items")
    suspend fun deleteAllItems()

    @Query("DELETE FROM pages")
    suspend fun deleteAllPages()

    /**
     * Atomically replaces all database records in a single transaction.
     */
    @Transaction
    suspend fun replaceAll(
        pages: List<PageEntity>,
        items: List<ContentItemEntity>,
        responseSets: List<ResponseSetEntity>,
        responses: List<ResponseEntity>,
    ) {
        // Clear old database entries before inserting new data
        deleteAllResponses()
        deleteAllResponseSets()
        deleteAllItems()
        deleteAllPages()
        insertPages(pages)
        insertItems(items)
        insertResponseSets(responseSets)
        insertResponses(responses)
    }
}
