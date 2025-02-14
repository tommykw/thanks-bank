package com.tommykw.thanks_bank

import com.slack.api.bolt.App
import com.slack.api.bolt.AppConfig
import com.slack.api.bolt.util.SlackRequestParser
import com.tommykw.thanks_bank.module.*
import com.tommykw.thanks_bank.repository.DatabaseFactory
import com.tommykw.thanks_bank.repository.ThankRepository
import com.tommykw.thanks_bank.repository.UserRepository
import com.tommykw.thanks_bank.route.*
import com.tommykw.thanks_bank.util.Every
import com.tommykw.thanks_bank.util.TaskScheduler
import freemarker.cache.ClassTemplateLoader
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.ContentNegotiation
import io.ktor.features.DefaultHeaders
import io.ktor.features.StatusPages
import io.ktor.freemarker.FreeMarker
import io.ktor.gson.gson
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.resources
import io.ktor.http.content.static
import io.ktor.locations.Locations
import io.ktor.response.respondText
import io.ktor.routing.routing
import java.util.concurrent.TimeUnit

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    install(DefaultHeaders)

    install(StatusPages) {
        exception<Throwable> { e ->
            call.respondText(
                e.localizedMessage,
                ContentType.Text.Plain, HttpStatusCode.InternalServerError
            )
        }
    }

    install(ContentNegotiation) {
        gson()
    }

    install(FreeMarker) {
        templateLoader = ClassTemplateLoader(this::class.java.classLoader, "templates")
    }

    install(Locations)

    DatabaseFactory.init()
    val thankRepository = ThankRepository()
    val userRepository = UserRepository()

    val slackAppConfig = AppConfig()
    val slackApp = App(slackAppConfig)

    slackReactionEvent(slackApp, thankRepository)
    slackMessageEvent(slackApp, thankRepository, userRepository)
    slackCommand(slackApp)
    slackViewSubmission(slackApp, thankRepository, userRepository)

    TaskScheduler {
        sendPostThanksMessages(thankRepository)
    }.start(
        Every(5, TimeUnit.MINUTES)
    )

    routing {
        static("/static") {
            resources("css")
        }

        homeRouting()
        thanksRouting(thankRepository)
        thanksDetailRouting(thankRepository)
        slackEventRouting(slackApp, SlackRequestParser(slackAppConfig))
    }
}