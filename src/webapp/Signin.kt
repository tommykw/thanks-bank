package com.tommykw.webapp

import com.tommykw.repository.Repository
import io.ktor.application.call
import io.ktor.freemarker.FreeMarkerContent
import io.ktor.locations.Location
import io.ktor.locations.get
import io.ktor.response.respond
import io.ktor.routing.Route

const val SIGNIN = "/signin"

@Location(SIGNIN)
data class Signin(val userId: String = "", val error: String = "")

fun Route.signin(db: Repository, hashFunction: (String) -> String) {
    get<Signin> {
        call.respond(FreeMarkerContent("signin.ftl", null))
    }
}