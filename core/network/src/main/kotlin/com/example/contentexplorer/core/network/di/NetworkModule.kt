package com.example.contentexplorer.core.network.di

import com.example.contentexplorer.core.network.api.ContentApi
import com.example.contentexplorer.core.network.api.ContentApiImpl
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module
class NetworkModule {
    @Single
    fun provideJson(): Json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }

    @Single
    fun provideHttpClient(json: Json): HttpClient = HttpClient(OkHttp) {
        install(ContentNegotiation) { json(json) }
        install(Logging) { level = LogLevel.INFO }
    }

    @Single
    fun provideContentApi(client: HttpClient): ContentApi = ContentApiImpl(client)
}
