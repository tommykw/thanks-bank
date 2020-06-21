package com.tommykw.webapp

import com.tommykw.model.EPSession
import com.tommykw.redirect
import com.tommykw.repository.PlaygroundRepository
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

@Location("/playground")
class Playground

fun Route.playground(hashFunction: (String) -> String) {
    get<Playground> {
        val userRepository by kodein().instance<PlaygroundRepository>()
        val repository by kodein().instance<PlaygroundRepository>()

        val user = call.sessions.get<EPSession>()?.let { userRepository.user(it.userId) }

        if (user == null) {
            call.redirect(Signin())
        } else {
            val playgrounds = repository.playgrounds()
            val date = System.currentTimeMillis()
            val code = call.securityCode(date, user, hashFunction)

            call.respond(
                FreeMarkerContent(
                    "playground.ftl",
                    mapOf(
                        "playgrounds" to playgrounds,
                        "name" to user.displayName,
                        "date" to date,
                        "code" to code
                    ), user.userId
                )
            )
        }
    }

    post<Playground> {
        val repository by kodein().instance<PlaygroundRepository>()
        val playgroundRepository by kodein().instance<PlaygroundRepository>()
        val user = call.sessions.get<EPSession>()?.let { repository.user(it.userId) }

        val params = call.receiveParameters()
        //val date = params["date"]?.toLongOrNull() ?: return@post call.redirect(it)
        val action = params["action"] ?: throw IllegalArgumentException("Missing parameter: action")

//        if (user == null || !call.verifyCode(date, user, code, hashFunction)) {
//            call.redirect(Signin())
//        }

        when (action) {
            "delete" -> {
                val id = params["id"] ?: throw IllegalArgumentException("Missing parameter: id")
                playgroundRepository.removePlayground(id.toInt())
            }
            "add" -> {
                val code = params["code"] ?: return@post call.redirect(it)
                val name = params["name"] ?: return@post call.redirect(it)
                playgroundRepository.addPlayground(name, code)
            }
        }

        call.redirect(Playground())
    }
}