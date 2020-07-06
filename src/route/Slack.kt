package com.tommykw.route

import com.tommykw.repository.PlaygroundRepository
import io.ktor.application.call
import io.ktor.freemarker.FreeMarkerContent
import io.ktor.locations.Location
import io.ktor.locations.get
import io.ktor.response.respond
import io.ktor.routing.Route
import org.kodein.di.generic.instance
import org.kodein.di.ktor.kodein

@Location("/slack")
class Slack

fun Route.slack() {
    get<Slack> {
        val repository by kodein().instance<PlaygroundRepository>()
        val slackMessages = repository.slackMessages()

        call.respond(
            FreeMarkerContent(
                    "slack_message.ftl",
                mapOf(
                    "slack_messages" to slackMessages
                )
            )
        )
    }
}