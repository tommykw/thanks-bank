package com.tommykw.thanks_bank.module

import com.slack.api.bolt.App
import com.tommykw.thanks_bank.model.ThankRequest
import com.tommykw.thanks_bank.model.UserRequest
import com.tommykw.thanks_bank.repository.ThankRepository
import io.ktor.application.Application
import kotlinx.coroutines.launch

fun Application.slackViewSubmission(app: App, repository: ThankRepository) {
    app.viewSubmission("thanks-message") { req, ctx ->
        val stateValues = req.payload.view.state.values
        val message = stateValues["message-block"]?.get("message-action")?.value
        val targetUsers = stateValues["user-block"]?.get("user-action")?.selectedUsers

        if (message?.isNotEmpty() == true && targetUsers?.isNotEmpty() == true) {
            try {
                launch {
                    val members = repository.getSlackMembers().members

                    fun idToRealName(slackId: String): String {
                        val res = members.find { it.id == slackId }
                        return res?.realName ?: ""
                    }

                    fun idToProfileImage(slackId: String): String {
                        val res = members.find { it.id == slackId }
                        return res?.profile?.image512 ?: ""
                    }

                    targetUsers.forEach { targetUser ->
                        repository.createThank(
                            ThankRequest(
                                slackUserId = req.payload.user.id,
                                targetSlackUserId = targetUser,
                                body = message,
                            )
                        )

                        if (repository.getUser(req.payload.user.id) == null) {
                            repository.createUser(
                                UserRequest(
                                    slackUserId = req.payload.user.id,
                                    realName = idToRealName(req.payload.user.id),
                                    userImage = idToProfileImage(req.payload.user.id),
                                )
                            )
                        }

                        if (repository.getUser(targetUser) == null) {
                            repository.createUser(
                                UserRequest(
                                    slackUserId = targetUser,
                                    realName = idToRealName(targetUser),
                                    userImage = idToProfileImage(targetUser),
                                )
                            )
                        }
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