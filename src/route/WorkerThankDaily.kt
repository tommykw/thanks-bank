package com.tommykw.route

import com.slack.api.methods.MethodsClient
import com.slack.api.methods.request.chat.ChatPostMessageRequest
import com.tommykw.repository.PlaygroundRepository
import io.ktor.locations.Location
import io.ktor.locations.get
import io.ktor.routing.Route
import org.kodein.di.generic.instance
import org.kodein.di.ktor.kodein

@Location("/worker/thank/daily")
class WorkerThankDaily

fun Route.workerThankDaily(apiClient: MethodsClient) {
    get<WorkerThankDaily> {
        val repository by kodein().instance<PlaygroundRepository>()
        val thanks = repository.getThanks()

        if (thanks.isEmpty()) {
            return@get
        }

        val request = ChatPostMessageRequest.builder()
                .channel("#general")
                .text("全部で${thanks.size}件のメッセージが届いているよ！")
                .build()

        apiClient.chatPostMessage(request)

        thanks.forEach { thank ->
            val message = """
```
Received: XXXX/XX/XX
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
            }
        }
    }
}