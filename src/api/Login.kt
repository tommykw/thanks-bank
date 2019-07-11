package com.tommykw.api

import com.tommykw.JwtService
import com.tommykw.hash
import com.tommykw.redirect
import com.tommykw.repository.Repository
import io.ktor.application.call
import io.ktor.http.Parameters
import io.ktor.locations.Location
import io.ktor.locations.post
import io.ktor.request.receive
import io.ktor.response.respondText
import io.ktor.routing.Route

const val LOGIN_ENDPOINT = "/login"

@Location(LOGIN_ENDPOINT)
class Login

fun Route.login(repository: Repository, jwtService: JwtService) {
    post<Login> {
        val params = call.receive<Parameters>()
        val userId = params["userId"] ?: return@post call.redirect(it)
        val password = params["password"] ?: return@post call.redirect(it)

        val user = repository.user(userId, hash(password))
        if (user != null) {
            val token = jwtService.generateToken(user)
            call.respondText(token)
        } else {
            call.respondText("Invalid user")
        }
    }
}