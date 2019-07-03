package com.tommykw.webapp

import com.tommykw.model.User
import com.tommykw.redirect
import com.tommykw.repository.Repository
import io.ktor.application.call
import io.ktor.auth.authenticate
import io.ktor.auth.authentication
import io.ktor.freemarker.FreeMarkerContent
import io.ktor.locations.Location
import io.ktor.locations.get
import io.ktor.locations.post
import io.ktor.request.receiveParameters
import io.ktor.response.respond
import io.ktor.routing.Route

const val EMOJIS = "/emojis"

@Location(EMOJIS)
class Emojis

fun Route.emojis(repository: Repository) {

    authenticate("auth") {
        get<Emojis> {
            val user = call.authentication.principal as User
            val emojis = repository.emojis()
            call.respond(
                FreeMarkerContent(
                    "emojis.ftl",
                    mapOf(
                        "emojis" to emojis,
                        "name" to user.name
                    )
                )
            )
        }
        post<Emojis> {
            val params = call.receiveParameters()
            val action = params["action"] ?: throw IllegalArgumentException("Missing parameter: action")
            when (action) {
                "delete" -> {
                    val id = params["id"] ?: throw IllegalArgumentException("Missing parameter: id")
                    repository.remove(id.toInt())
                }
                "add" -> {
                    val emoji = params["emoji"] ?: throw IllegalArgumentException("Missing parameter: emoji")
                    repository.add(emoji)
                }
            }

            call.redirect(Emojis())
        }
    }
}