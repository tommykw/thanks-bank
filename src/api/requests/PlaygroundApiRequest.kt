package com.tommykw.api.requests

data class PlaygroundApiRequest(
    val id: Int?,
    val name: String,
    val code: String
)