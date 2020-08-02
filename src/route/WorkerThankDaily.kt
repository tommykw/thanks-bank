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

        thanks.forEach { thank ->
            val message = "<@${thank.targetSlackUserId}>さんから<@${thank.slackUserId}>さんへメッセージが届いています。"

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