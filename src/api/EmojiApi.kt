package com.tommykw.api

import com.tommykw.API_VERSION
import com.tommykw.model.Emoji
import com.tommykw.model.Request
import com.tommykw.repository.Repository
import io.ktor.application.call
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.post

const val EMOJI_ENDPOINT = "$API_VERSION/emoji"

fun Route.emoji(repository: Repository) {
    post(EMOJI_ENDPOINT) {
        val request = call.receive<Request>()
        val emoji = repository.add(Emoji(request.name))
        call.respond(emoji)
    }
}