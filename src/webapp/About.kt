package com.tommykw.webapp

import io.ktor.application.call
import io.ktor.freemarker.FreeMarkerContent
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.Route
import io.ktor.routing.get

const val ABOUT = "/about"

fun Route.about() {
    get(ABOUT) {
        call.respond(FreeMarkerContent("about.ftl", null))
    }
}