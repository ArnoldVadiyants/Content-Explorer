package com.example.contentexplorer.core.domain.model

data class ResponseSet(
    val id: Long,
    val multipleSelection: Boolean,
    val responses: List<Response>,
)
