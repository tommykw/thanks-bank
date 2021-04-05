package com.tommykw.thanks_bank.module

import com.slack.api.bolt.App
import com.tommykw.thanks_bank.model.ThankRequest
import com.tommykw.thanks_bank.model.UserRequest
import com.tommykw.thanks_bank.repository.ThankRepository
import com.tommykw.thanks_bank.repository.UserRepository
import io.ktor.application.Application
import kotlinx.coroutines.launch

fun Application.slackViewSubmission(
    app: App,
    thankRepository: ThankRepository,
    userRepository: UserRepository
) {
    app.viewSubmission("thanks-message") { req, ctx ->
        val stateValues = req.payload.view.state.values
        val message = stateValues["message-block"]?.get("message-action")?.value
        val targetUsers = stateValues["user-block"]?.get("user-action")?.selectedUsers

        if (message?.isNotEmpty() == true && targetUsers?.isNotEmpty() == true) {
            val slackUserId = req.payload.user.id

            launch {
                listOf(*targetUsers.toTypedArray()).forEach { targetSlackUserId ->
                    thankRepository.createThank(
                        ThankRequest(
                            slackUserId = slackUserId,
                            targetSlackUserId = targetSlackUserId,
                            body = message,
                        )
                    )
                }

                listOf(*targetUsers.toTypedArray(), slackUserId).distinct().forEach { targetSlackUserId ->
                    if (userRepository.getUser(targetSlackUserId) == null) {
                        val slackUsersInfo = userRepository.getSlackUsersInfo(targetSlackUserId)
                        userRepository.createUser(
                            UserRequest(
                                slackUserId = targetSlackUserId,
                                realName = slackUsersInfo.user.realName,
                                userImage = slackUsersInfo.user.profile.image512,
                            )
                        )
                    }
                }
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