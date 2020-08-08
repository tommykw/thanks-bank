package com.tommykw.module

import com.slack.api.bolt.App
import com.tommykw.model.ThankRequest
import com.tommykw.repository.ThankRepository
import io.ktor.application.Application
import kotlinx.coroutines.launch
import org.kodein.di.generic.instance
import org.kodein.di.ktor.kodein

fun Application.slackViewSubmission(app: App) {
    app.viewSubmission("thanks-message") { req, ctx ->
        val stateValues = req.payload.view.state.values
        val message = stateValues["message-block"]?.get("message-action")?.value
        val targetUsers = stateValues["user-block"]?.get("user-action")?.selectedUsers

        if (message?.isNotEmpty() == true && targetUsers?.isNotEmpty() == true) {
            try {
                launch {
                    val repository by kodein().instance<ThankRepository>()

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

        ctx.ack()
    }
}