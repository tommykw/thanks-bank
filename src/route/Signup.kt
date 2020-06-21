package com.tommykw.route

import com.tommykw.MIN_PASSWORD_LENGTH
import com.tommykw.MIN_USER_ID_LENGTH
import com.tommykw.model.EPSession
import com.tommykw.model.User
import com.tommykw.redirect
import com.tommykw.repository.PlaygroundRepository
import com.tommykw.userNameValid
import io.ktor.application.application
import io.ktor.application.call
import io.ktor.application.log
import io.ktor.freemarker.FreeMarkerContent
import io.ktor.http.Parameters
import io.ktor.locations.Location
import io.ktor.locations.get
import io.ktor.locations.post
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.sessions.get
import io.ktor.sessions.sessions
import io.ktor.sessions.set
import org.kodein.di.generic.instance
import org.kodein.di.ktor.kodein

const val SIGNUP = "/signup"

@Location(SIGNUP)
data class Signup(
    val userId: String = "",
    val name: String = "",
    val email: String = "",
    val error: String = ""
)

fun Route.signup(hashFunction: (String) -> String) {
    post<Signup> {
        val db by kodein().instance<PlaygroundRepository>()
        val user = call.sessions.get<EPSession>()?.let { db.user(it.userId) }
        if (user != null) return@post call.redirect(Playground())

        val signupParameters = call.receive<Parameters>()
        val userId = signupParameters["userId"] ?: return@post call.redirect(it)
        val password = signupParameters["password"] ?: return@post call.redirect(it)
        val displayName = signupParameters["displayName"] ?: return@post call.redirect(it)
        val email = signupParameters["email"] ?: return@post call.redirect(it)

        val signUpError = Signup(userId, displayName, email)

        when {
            password.length < MIN_PASSWORD_LENGTH ->
                call.redirect(signUpError.copy(error = "Password should be at least $MIN_PASSWORD_LENGTH characters long"))
            userId.length < MIN_USER_ID_LENGTH ->
                call.redirect(signUpError.copy(error = "Username should be at least $MIN_USER_ID_LENGTH characters long"))
            !userNameValid(userId) ->
                call.redirect(signUpError.copy(error = "Username should be consist of digits, letters, dots or underscores"))
            db.user(userId) != null ->
                call.redirect(signUpError.copy(error = "User with the following username is already registered"))
            else -> {
                val hash = hashFunction(password)
                val newUser = User(userId, email, displayName, hash)

                try {
                    db.createUser(newUser)
                } catch (e: Throwable) {
                    when {
                        db.user(userId) != null ->
                            call.redirect(signUpError.copy(error = "User with the following username is already "))
                        db.userByEmail(email) != null ->
                            call.redirect(signUpError.copy(error = "User with the following email $email is already "))
                        else -> {
                            application.log.error("Failed to register user", e)
                            call.redirect(signUpError.copy(error = "Failed to register"))
                        }
                    }
                }

                call.sessions.set(EPSession(newUser.userId))
                call.redirect(Playground())
            }
        }
    }

    get<Signup> {
        val db by kodein().instance<PlaygroundRepository>()
        val user = call.sessions.get<EPSession>()?.let {
            db.user(it.userId)
        }

        if (user != null) {
            call.redirect(Playground())
        } else {
            call.respond(FreeMarkerContent("signup.ftl", mapOf("error" to it.error)))
        }
        call.respond(FreeMarkerContent("signup.ftl", null))
    }
}