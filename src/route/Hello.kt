package com.tommykw.route

import io.ktor.application.call
import io.ktor.locations.Location
import io.ktor.locations.post
import io.ktor.response.respond
import io.ktor.routing.Route

@Location("/hello")
class Hello

fun Route.hello() {
    post<Hello> {
        val response = SlackResponse(
            response_type = "in_channel",
            text = "hello"
        )

        call.respond(response)
    }
}

data class SlackResponse(
    val response_type: String,
    val text: String
)