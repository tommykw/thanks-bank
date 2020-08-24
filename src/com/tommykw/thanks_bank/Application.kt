package com.tommykw.thanks_bank

import com.slack.api.bolt.App
import com.slack.api.bolt.AppConfig
import com.slack.api.bolt.util.SlackRequestParser
import com.tommykw.thanks_bank.api.thankDailyApi
import com.tommykw.thanks_bank.module.slackCommand
import com.tommykw.thanks_bank.module.slackMessageEvent
import com.tommykw.thanks_bank.module.slackReactionEvent
import com.tommykw.thanks_bank.module.slackViewSubmission
import com.tommykw.thanks_bank.repository.DatabaseFactory
import com.tommykw.thanks_bank.repository.ThankRepository
import com.tommykw.thanks_bank.route.homeRoute
import com.tommykw.thanks_bank.route.slackEvent
import com.tommykw.thanks_bank.route.thanks
import com.tommykw.thanks_bank.route.thanksDetail
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
    val repository = ThankRepository()

    val slackAppConfig = AppConfig()
    val slackApp = App(slackAppConfig)

    slackReactionEvent(slackApp, repository)
    slackMessageEvent(slackApp, repository)
    slackCommand(slackApp)
    slackViewSubmission(slackApp, repository)

    routing {
        static("/static") {
            resources("css")
        }

        homeRoute()
        thanks(repository)
        thanksDetail(repository)
        slackEvent(slackApp, SlackRequestParser(slackAppConfig))

        thankDailyApi(repository)
    }
}