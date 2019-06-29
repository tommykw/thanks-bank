package com.tommykw.webapp

import com.tommykw.model.User
import com.tommykw.repository.InMemoryRepository
import com.tommykw.repository.Repository
import io.ktor.application.call
import io.ktor.auth.authenticate
import io.ktor.auth.authentication
import io.ktor.freemarker.FreeMarkerContent
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get

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
    }
}