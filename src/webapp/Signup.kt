package com.tommykw.webapp

import com.tommykw.repository.Repository
import io.ktor.application.call
import io.ktor.freemarker.FreeMarkerContent
import io.ktor.locations.Location
import io.ktor.locations.get
import io.ktor.response.respond
import io.ktor.routing.Route

const val SIGNUP = "/signup"

@Location(SIGNUP)
data class Signup(
    val userId: String = "",
    val name: String = "",
    val email: String = "",
    val error: String = ""
)

fun Route.signup(db: Repository, hashFunction: (String) -> String) {
    get<Signup> {
        call.respond(FreeMarkerContent("signup.ftl", null))
    }
}