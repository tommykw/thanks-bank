package com.tommykw.webapp

import com.tommykw.model.Emoji
import com.tommykw.model.User
import com.tommykw.repository.InMemoryRepository
import com.tommykw.repository.Repository
import io.ktor.application.call
import io.ktor.auth.authenticate
import io.ktor.auth.authentication
import io.ktor.freemarker.FreeMarkerContent
import io.ktor.request.receiveParameters
import io.ktor.response.respond
import io.ktor.response.respondRedirect
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.routing.post

const val EMOJIS = "/emojis"

fun Route.emojis(repository: Repository) {

    authenticate("auth") {
        get(EMOJIS) {
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
        post(EMOJIS) {
            val params = call.receiveParameters()
            val action = params["action"] ?: throw IllegalArgumentException("Missing parameter: action")
            when (action) {
                "delete" -> {
                    val id = params["id"] as? Int ?: throw IllegalArgumentException("Missing parameter: id")
                    repository.remove(id)
                }
                "add" -> {
                    val emoji = params["emoji"] ?: throw IllegalArgumentException("Missing parameter: emoji")
                    repository.add(Emoji(emoji))
                }
            }

            call.respondRedirect(EMOJIS)
        }
    }
}