package com.example.contentexplorer.core.network.api

import com.example.contentexplorer.core.network.dto.PageDto
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.json.Json

/**
 * Network API client for fetching content pages.
 */
interface ContentApi {
    /**
     * Downloads and parses pages JSON from the remote endpoint.
     */
    suspend fun fetchPages(): List<PageDto>
}

internal class ContentApiImpl(
    private val client: HttpClient,
    private val json: Json,
) : ContentApi {
    override suspend fun fetchPages(): List<PageDto> =
        json.decodeFromString(
            client
                .get("https://gist.githubusercontent.com/aruana-lumiform/383e1291df3e0cc1d49aeb14d45f5b94/raw/lumiform-android-test.json")
                .bodyAsText(),
        )
}
