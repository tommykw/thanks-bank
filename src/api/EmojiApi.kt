package com.tommykw.api

import com.tommykw.API_VERSION
import com.tommykw.model.Emoji
import com.tommykw.model.Request
import com.tommykw.model.User
import com.tommykw.repository.Repository
import io.ktor.application.call
import io.ktor.auth.authenticate
import io.ktor.auth.authentication
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.post

const val EMOJI_ENDPOINT = "$API_VERSION/emoji"

fun Route.emoji(repository: Repository) {

    authenticate("auth") {
        post(EMOJI_ENDPOINT) {
            val request = call.receive<Request>()
            val emoji = repository.add(request.name)
            call.respond(emoji)
        }
    }
}