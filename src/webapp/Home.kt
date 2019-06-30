package com.tommykw

import io.ktor.application.call
import io.ktor.freemarker.FreeMarkerContent
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.Route
import io.ktor.routing.get

const val HOME = "/"

fun Route.home() {
    get(HOME) {
        call.respond(FreeMarkerContent("home.ftl", null))
    }
}