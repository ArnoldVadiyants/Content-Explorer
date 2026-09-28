package com.example.contentexplorer.core.data.repository

import com.example.contentexplorer.core.database.dao.ContentDao
import com.example.contentexplorer.core.database.entity.PageEntity
import com.example.contentexplorer.core.network.api.ContentApi
import com.example.contentexplorer.core.network.dto.PageDto
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.io.IOException

@ExtendWith(MockKExtension::class)
class ContentRepositoryImplTest {

    @MockK
    private lateinit var contentApi: ContentApi

    @MockK(relaxUnitFun = true)
    private lateinit var contentDao: ContentDao

    @Test
    fun `refresh persists a successfully fetched snapshot`() = runTest {
        coEvery { contentApi.fetchPages() } returns listOf(
            PageDto(id = 1, type = "page", title = "Page", items = emptyList()),
        )

        val result = ContentRepositoryImpl(contentApi, contentDao).refresh()

        assertTrue(result.isSuccess)
        coVerify {
            contentDao.replaceAll(
                pages = listOf(PageEntity(id = 1, title = "Page")),
                items = emptyList(),
                responseSets = emptyList(),
                responses = emptyList(),
            )
        }
    }

    @Test
    fun `refresh failure leaves the existing snapshot untouched`() = runTest {
        coEvery { contentApi.fetchPages() } throws IOException("offline")

        val result = ContentRepositoryImpl(contentApi, contentDao).refresh()

        assertTrue(result.isFailure)
        coVerify(exactly = 0) { contentDao.replaceAll(any(), any(), any(), any()) }
    }
}
