package com.tommykw.route

import com.slack.api.bolt.request.Request
import com.slack.api.bolt.request.RequestHeaders
import com.slack.api.bolt.response.Response
import com.slack.api.bolt.util.QueryStringParser
import com.slack.api.bolt.util.SlackRequestParser
import com.tommykw.app
import com.tommykw.requestParser
import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.features.origin
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.TextContent
import io.ktor.locations.Location
import io.ktor.locations.post
import io.ktor.request.queryString
import io.ktor.request.receiveText
import io.ktor.request.uri
import io.ktor.response.header
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.util.toMap

@Location("/slack/events")
class SlackEventRoute

fun Route.slackEvent() {
    post<SlackEventRoute> {
        respond(call, app.run(parseRequest(call)))
    }
}

suspend fun parseRequest(call: ApplicationCall): Request<*> {
    val requestBody = call.receiveText()
    val queryString = QueryStringParser.toMap(call.request.queryString())
    val headers = RequestHeaders(call.request.headers.toMap())

    return requestParser.parse(
            SlackRequestParser.HttpRequest.builder()
                    .requestUri(call.request.uri)
                    .queryString(queryString)
                    .requestBody(requestBody)
                    .headers(headers)
                    .remoteAddress(call.request.origin.remoteHost)
                    .build()
    )
}

suspend fun respond(call: ApplicationCall, slackResp: Response) {
    for (header in slackResp.headers) {
        for (value in header.value) {
            call.response.header(header.key, value)
        }
    }

    call.response.status(HttpStatusCode.fromValue(slackResp.statusCode))
    if (slackResp.body != null) {
        call.respond(TextContent(slackResp.body, ContentType.parse(slackResp.contentType)))
    }
}