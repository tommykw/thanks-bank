package com.tommykw.api

import com.slack.api.methods.MethodsClient
import com.slack.api.methods.request.chat.ChatPostMessageRequest
import com.tommykw.repository.ThankRepository
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.locations.Location
import io.ktor.locations.post
import io.ktor.response.respond
import io.ktor.routing.Route
import org.kodein.di.generic.instance
import org.kodein.di.ktor.kodein
import java.text.SimpleDateFormat
import java.util.*

@Location("/api/thank/daily")
class ThankDailyApi

private val dateFormat = SimpleDateFormat("HH:mm:dd").apply {
    timeZone = TimeZone.getTimeZone("Asia/Tokyo")
}

fun Route.thankDailyApi(apiClient: MethodsClient) {
    post<ThankDailyApi> {
        val repository by kodein().instance<ThankRepository>()
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
//        Received: ${dateFormat.format(thank.createdAt)}
            val message = """
```
<@${thank.targetSlackUserId}>さんから
<@${thank.slackUserId}>さんへメッセージが届いてるよ！
```
${thank.body}
----✁----✁----
""".trimIndent()

            val request = ChatPostMessageRequest.builder()
                    .channel("#general")
                    .text(message)
                    .build()

            val response = apiClient.chatPostMessage(request)

            if (response.isOk) {
                repository.updateSlackPostId(response.ts, thank)
            } else {
                // TODO エラーコード
            }
        }

        call.response.status(HttpStatusCode.OK)
        call.respond(mapOf("status" to "OK"))
    }
}