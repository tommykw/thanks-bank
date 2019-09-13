package com.tommykw.api

import com.tommykw.API_VERSION
import com.tommykw.api.requests.PlaygroundApiRequest
import com.tommykw.apiuser
import com.tommykw.repository.PlaygroundRepository
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.locations.Location
import io.ktor.locations.get
import io.ktor.locations.post
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.Route
import org.kodein.di.generic.instance
import org.kodein.di.ktor.kodein

@Location("$API_VERSION/playground")
class PlaygroundApi

fun Route.playgroundApi() {
    get<PlaygroundApi> {
        val repository by kodein().instance<PlaygroundRepository>()
        call.respond(repository.playgrounds())
    }

    post<PlaygroundApi> {
        val repository by kodein().instance<PlaygroundRepository>()
        //val user = call.apiuser!!

        try {
            val request = call.receive<PlaygroundApiRequest>()
            val playgrounds = repository.addPlayground(request.name, request.code)
            if (playgrounds != null) {
                call.respond(playgrounds)
            } else {
                call.respondText("Invalid data received", status = HttpStatusCode.InternalServerError)
            }
        } catch (e: Throwable) {
            call.respondText("Invalid data received", status = HttpStatusCode.BadRequest)
        }
    }

//    authenticate("jwt") {
//    }
}