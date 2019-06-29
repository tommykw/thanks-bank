package com.tommykw.webapp

import io.ktor.application.call
import io.ktor.response.respondText
import io.ktor.routing.Route
import io.ktor.routing.get

const val ABOUT = "/about"

fun Route.about() {
    get(ABOUT) {
        call.respondText("About Page.")
    }
}