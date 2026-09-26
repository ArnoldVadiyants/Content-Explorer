package com.example.contentexplorer.core.network.api

import com.example.contentexplorer.core.network.dto.PageDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

interface ContentApi {
    suspend fun fetchPages(): List<PageDto>
}

internal class ContentApiImpl(private val client: HttpClient) : ContentApi {
    override suspend fun fetchPages(): List<PageDto> =
        client.get("https://gist.githubusercontent.com/aruana-lumiform/383e1291df3e0cc1d49aeb14d45f5b94/raw/lumiform-android-test.json").body()
}
