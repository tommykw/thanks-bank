package com.tommykw.route

import com.tommykw.model.EPSession
import com.tommykw.repository.PlaygroundRepository
import io.ktor.application.call
import io.ktor.freemarker.FreeMarkerContent
import io.ktor.locations.Location
import io.ktor.locations.get
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.sessions.get
import io.ktor.sessions.sessions
import org.kodein.di.generic.instance
import org.kodein.di.ktor.kodein

@Location("/about")
class About

fun Route.about() {
    get<About> {
        val repository by kodein().instance<PlaygroundRepository>()
        val user = call.sessions.get<EPSession>()?.let { repository.user(it.userId) }
        call.respond(FreeMarkerContent("about.ftl", mapOf("user" to user)))
    }
}