package com.tommykw.api

import com.tommykw.API_VERSION
import com.tommykw.api.requests.EmojiApiRequest
import com.tommykw.apiuser
import com.tommykw.repository.Repository
import io.ktor.application.call
import io.ktor.auth.authenticate
import io.ktor.http.HttpStatusCode
import io.ktor.locations.Location
import io.ktor.locations.get
import io.ktor.locations.post
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.Route

const val EMOJI_ENDPOINT = "$API_VERSION/emoji"

@Location(EMOJI_ENDPOINT)
class EmojiApi

fun Route.emojiApi(repository: Repository) {

    authenticate("jwt") {
        get<EmojiApi> {
            call.respond(repository.emojis())
        }

        post<EmojiApi> {
            val user = call.apiuser!!

            try {
                val request = call.receive<EmojiApiRequest>()
                val emojis = repository.add(user.userId, request.emoji)
                if (emojis != null) {
                    call.respond(emojis)
                } else {
                    call.respondText("Invalid data received", status = HttpStatusCode.InternalServerError)
                }
            } catch (e: Throwable) {
                call.respondText("Invalid data received", status = HttpStatusCode.BadRequest)
            }
        }
    }
}