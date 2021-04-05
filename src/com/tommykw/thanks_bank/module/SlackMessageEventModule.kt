package com.tommykw.thanks_bank.module

import com.slack.api.bolt.App
import com.slack.api.model.event.MessageEvent
import com.tommykw.thanks_bank.model.UserRequest
import com.tommykw.thanks_bank.repository.ThankRepository
import com.tommykw.thanks_bank.repository.UserRepository
import io.ktor.application.Application
import kotlinx.coroutines.launch

fun Application.slackMessageEvent(
    app: App,
    thankRepository: ThankRepository,
    userRepository: UserRepository
) {
    app.event(MessageEvent::class.java) { payload, ctx ->
        val event = payload.event

        if (event.channel == System.getenv("SLACK_THANKS_CHANNEL")) {
            launch {
                thankRepository.createThankReply(event)

                if (userRepository.getUser(event.user) == null) {
                    val members = userRepository.getSlackMembers().members

                    fun idToRealName(slackId: String): String {
                        val res = members.find { it.id == slackId }
                        return res?.realName ?: ""
                    }

                    fun idToProfileImage(slackId: String): String {
                        val res = members.find { it.id == slackId }
                        return res?.profile?.image512 ?: ""
                    }

                    userRepository.createUser(
                        UserRequest(
                            slackUserId = event.user,
                            realName = idToRealName(event.user),
                            userImage = idToProfileImage(event.user)
                        )
                    )
                }
            }
        }

        ctx.ack()
    }
}