package com.tommykw

import com.ryanharter.ktor.moshi.moshi
import com.tommykw.api.emoji
import com.tommykw.repository.InMemoryRepository
import com.tommykw.webapp.about
import com.tommykw.webapp.emojis
import freemarker.cache.ClassTemplateLoader
import io.ktor.application.*
import io.ktor.features.ContentNegotiation
import io.ktor.features.DefaultHeaders
import io.ktor.features.StatusPages
import io.ktor.freemarker.FreeMarker
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
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

    val repository = InMemoryRepository()

    routing {
        home()
        about()
        emojis(repository)

        emoji(repository)
    }
}

const val API_VERSION = "/api/v1"