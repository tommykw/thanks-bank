package com.tommykw.thanks_bank.route

import io.ktor.application.*
import io.ktor.locations.*
import io.ktor.response.*
import io.ktor.routing.*

@Location("/")
class HomeRoute

fun Route.homeRouting() {
    get<HomeRoute> {
        call.respondRedirect("/thanks")
    }
}
