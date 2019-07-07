package com.tommykw

import com.ryanharter.ktor.moshi.moshi
import com.tommykw.api.emoji
import com.tommykw.model.User
import com.tommykw.repository.DatabaseFactory
import com.tommykw.repository.EmojiRepository
import com.tommykw.webapp.*
import freemarker.cache.ClassTemplateLoader
import io.ktor.application.*
import io.ktor.auth.Authentication
import io.ktor.auth.basic
import io.ktor.features.ContentNegotiation
import io.ktor.features.DefaultHeaders
import io.ktor.features.StatusPages
import io.ktor.freemarker.FreeMarker
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.resources
import io.ktor.http.content.static
import io.ktor.locations.Locations
import io.ktor.locations.locations
import io.ktor.response.*
import io.ktor.routing.routing

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {

    install(DefaultHeaders) {}

    install(StatusPages) {
        exception<Throwable> { e ->
            call.respondText(
                e.localizedMessage,
                ContentType.Text.Plain, HttpStatusCode.InternalServerError
            )
        }
    }

    install(ContentNegotiation) {
        moshi()
    }

    install(FreeMarker) {
        templateLoader = ClassTemplateLoader(this::class.java.classLoader, "templates")
    }

    install(Locations)

    val hashFunction = { s: String -> hash(s) }

    DatabaseFactory.init()

    val repository = EmojiRepository()

    routing {
        static("/static") {
            resources("images")
        }

        home()
        about()
        emojis(repository)
        signin(repository, hashFunction)
        signout()
        signup(repository, hashFunction)

        emoji(repository)
    }
}

const val API_VERSION = "/api/v1"

suspend fun ApplicationCall.redirect(location: Any) {
    respondRedirect(application.locations.href(location))
}