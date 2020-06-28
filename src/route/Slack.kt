package com.tommykw.route

import io.ktor.locations.Location
import io.ktor.routing.Route
import io.ktor.routing.post

@Location("/slack/events")
class Slack

//fun Route.slack1() {
//    post<Slack> {
//        respond(call, app.ru)
//    }
//}