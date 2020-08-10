package com.tommykw.thanks_bank.module

import com.slack.api.bolt.App
import com.tommykw.thanks_bank.model.ThankRequest
import com.tommykw.thanks_bank.repository.Repository
import io.ktor.application.Application
import kotlinx.coroutines.launch

fun Application.slackViewSubmission(app: App, repository: Repository) {
    app.viewSubmission("thanks-message") { req, ctx ->
        val stateValues = req.payload.view.state.values
        val message = stateValues["message-block"]?.get("message-action")?.value
        val targetUsers = stateValues["user-block"]?.get("user-action")?.selectedUsers

        if (message?.isNotEmpty() == true && targetUsers?.isNotEmpty() == true) {
            try {
                launch {
                    targetUsers.forEach { targetUser ->
                        repository.createThank(
                            ThankRequest(
                                slackUserId = req.payload.user.id,
                                targetSlackUserId = targetUser,
                                body = message
                            )
                        )
                    }
                }
            } catch (e: Throwable) {
            }
        }

        ctx.client().chatPostEphemeral {
            it.token(ctx.botToken)
            it.user(req.payload.user.id)
            it.channel("#general")
            it.text("メッセージが送信されました")
        }

        ctx.ack()
    }
}