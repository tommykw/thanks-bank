package com.tommykw.webapp

import com.tommykw.repository.InMemoryRepository
import com.tommykw.repository.Repository
import io.ktor.application.call
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get

const val EMOJIS = "/emojis"

fun Route.emojis(repository: Repository) {
    get(EMOJIS) {
        val emojis = repository.emojis()
        call.respond(emojis.toArray())
    }
}