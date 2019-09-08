package com.tommykw.webapp

import com.tommykw.model.EPSession
import com.tommykw.redirect
import com.tommykw.repository.EmojiRepository
import com.tommykw.securityCode
import com.tommykw.verifyCode
import io.ktor.application.call
import io.ktor.freemarker.FreeMarkerContent
import io.ktor.locations.Location
import io.ktor.locations.get
import io.ktor.locations.post
import io.ktor.request.receiveParameters
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.sessions.get
import io.ktor.sessions.sessions
import org.kodein.di.generic.instance
import org.kodein.di.ktor.kodein

const val EMOJIS = "/emojis"

@Location(EMOJIS)
class Emojis

fun Route.emojis(hashFunction: (String) -> String) {
    get<Emojis> {
        val repository by kodein().instance<EmojiRepository>()

        val user = call.sessions.get<EPSession>()?.let { repository.user(it.userId) }

        if (user == null) {
            call.redirect(Signin())
        } else {
            val emojis = repository.emojis()
            val date = System.currentTimeMillis()
            val code = call.securityCode(date, user, hashFunction)

            call.respond(
                FreeMarkerContent(
                    "emojis.ftl",
                    mapOf(
                        "emojis" to emojis,
                        "name" to user.displayName,
                        "date" to date,
                        "code" to code
                    ), user.userId
                )
            )
        }
    }

    post<Emojis> {
        val repository by kodein().instance<EmojiRepository>()
        val user = call.sessions.get<EPSession>()?.let { repository.user(it.userId) }

        val params = call.receiveParameters()
        val date = params["date"]?.toLongOrNull() ?: return@post call.redirect(it)
        val code = params["code"] ?: return@post call.redirect(it)
        val action = params["action"] ?: throw IllegalArgumentException("Missing parameter: action")

        if (user == null || !call.verifyCode(date, user, code, hashFunction)) {
            call.redirect(Signin())
        }

        when (action) {
            "delete" -> {
                val id = params["id"] ?: throw IllegalArgumentException("Missing parameter: id")
                repository.remove(id.toInt())
            }
            "add" -> {
                val emoji = params["emoji"] ?: throw IllegalArgumentException("Missing parameter: emoji")
                repository.add(user!!.userId, emoji)
            }
        }

        call.redirect(Emojis())
    }
}