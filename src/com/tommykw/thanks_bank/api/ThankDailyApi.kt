package com.tommykw.thanks_bank.api

import com.slack.api.Slack
import com.slack.api.methods.request.chat.ChatPostMessageRequest
import com.tommykw.thanks_bank.repository.ThankRepository
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.locations.Location
import io.ktor.locations.post
import io.ktor.response.respond
import io.ktor.routing.Route
import java.text.SimpleDateFormat
import java.util.*

@Location("/api/thank/daily")
class ThankDailyApi

private val dateFormat = SimpleDateFormat("yyyy/MM/dd")

fun Route.thankDailyApi(repository: ThankRepository) {
    post<ThankDailyApi> {
        val slack = Slack.getInstance()
        val apiClient = slack.methods(System.getenv("SLACK_BOT_TOKEN"))

        val thanks = repository.getThanks()

        if (thanks.isEmpty()) {
            call.response.status(HttpStatusCode.OK)
            call.respond(mapOf("status" to "OK"))
            return@post
        }

        val request = ChatPostMessageRequest.builder()
                .channel("#general")
                .text("全部で${thanks.size}件のメッセージが届いているよ:tada::tada:\nリアクションやお返しをしてみよう！")
                .build()

        apiClient.chatPostMessage(request)

        thanks.forEach { thank ->
            val message = """
```
${dateFormat.format(thank.createdAt.toDate())}
<@${thank.targetSlackUserId}>さんから
<@${thank.slackUserId}>さんへメッセージが届いてるよ！
```
${thank.body}
----✁----✁----
""".trimIndent()

            request.text = message

            val response = apiClient.chatPostMessage(request)

            if (response.isOk) {
                repository.updateSlackPostId(response.ts, thank)
            }
        }

        call.response.status(HttpStatusCode.OK)
        call.respond(mapOf("status" to "OK"))
    }
}