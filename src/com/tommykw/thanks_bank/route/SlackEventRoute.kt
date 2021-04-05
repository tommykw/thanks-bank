package com.tommykw.thanks_bank.route

import com.slack.api.bolt.App
import com.slack.api.bolt.ktor.respond
import com.slack.api.bolt.ktor.toBoltRequest
import com.slack.api.bolt.util.SlackRequestParser
import io.ktor.application.call
import io.ktor.locations.Location
import io.ktor.locations.post
import io.ktor.routing.Route

@Location("/slack/events")
class SlackEventRoute

fun Route.slackEventRouting(app: App, requestParser: SlackRequestParser) {
    post<SlackEventRoute> {
        respond(call, app.run(toBoltRequest(call, requestParser)))
    }
}