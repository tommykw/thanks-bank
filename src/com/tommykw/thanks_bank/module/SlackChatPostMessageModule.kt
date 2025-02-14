package com.tommykw.thanks_bank.module

import com.slack.api.Slack
import com.slack.api.methods.request.chat.ChatPostMessageRequest
import com.tommykw.thanks_bank.repository.ThankRepository
import io.ktor.application.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

private val dateFormat = SimpleDateFormat("yyyy/MM/dd")

fun Application.sendPostThanksMessages(thankRepository: ThankRepository) {
    launch {
        val slack = Slack.getInstance()
        val apiClient = slack.methods(System.getenv("SLACK_BOT_TOKEN"))
        val thanks = thankRepository.getPostThanks()

        if (thanks.isEmpty()) {
            return@launch
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
<@${thank.slackUserId}>さんから
<@${thank.targetSlackUserId}>さんへメッセージが届いてるよ！
```
${thank.body}
----✁----✁----
""".trimIndent()

            request.text = message

            val response = apiClient.chatPostMessage(request)

            if (response.isOk) {
                thankRepository.updateSlackPostId(response.ts, thank)
            }
        }
    }
}