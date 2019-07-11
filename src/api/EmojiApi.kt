package com.tommykw.api

import com.tommykw.API_VERSION
import com.tommykw.repository.Repository
import io.ktor.application.call
import io.ktor.auth.authenticate
import io.ktor.locations.Location
import io.ktor.locations.get
import io.ktor.response.respond
import io.ktor.routing.Route

const val EMOJI_ENDPOINT = "$API_VERSION/emoji"

@Location(EMOJI_ENDPOINT)
class EmojiApi

fun Route.emojiApi(repository: Repository) {

    authenticate("jwt") {
        get<EmojiApi> {
            call.respond(repository.emojis())
        }
    }
}