package com.tommykw.thanks_bank.route

import io.ktor.application.*
import io.ktor.locations.*
import io.ktor.response.*
import io.ktor.routing.*

@Location("/")
class HomeRoute

fun Route.homeRoute() {
    get {
        call.respondRedirect("/thanks")
    }
}
