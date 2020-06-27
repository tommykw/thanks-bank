package com.tommykw.route

import com.tommykw.auth.GoogleCalendarClient
import com.tommykw.auth.Messaging
import io.ktor.application.call
import io.ktor.http.ContentType
import io.ktor.response.respondText
import io.ktor.locations.Location
import io.ktor.routing.Route
import io.ktor.locations.post
import io.ktor.locations.get

@Location("/google_calender")
class GoogleCalender

fun Route.googleCalender() {
    get<GoogleCalender> {
        val events = GoogleCalendarClient.getEvents()
        val messaging = Messaging()
        val json = messaging.createJson(events)

        call.respondText(json, ContentType.Application.Json)
    }
    post<GoogleCalender> {
        val events = GoogleCalendarClient.getEvents()
        val messaging = Messaging()
        val json = messaging.createJson(events)

        call.respondText(json, ContentType.Application.Json)
    }
}